package com.techfun.altrua.auth;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techfun.altrua.auth.dto.AuthResponseDTO;
import com.techfun.altrua.auth.dto.LoginRequestDTO;
import com.techfun.altrua.auth.dto.RegisterUserRequestDTO;
import com.techfun.altrua.auth.refresh.RefreshTokenService;
import com.techfun.altrua.auth.refresh.RotateResult;
import com.techfun.altrua.common.exceptions.DuplicateResourceException;
import com.techfun.altrua.common.exceptions.InvalidCredentialsException;
import com.techfun.altrua.common.exceptions.RefreshTokenException;
import com.techfun.altrua.security.jwt.JwtProvider;
import com.techfun.altrua.security.jwt.JwtValidator;
import com.techfun.altrua.security.userdetails.UserPrincipal;
import com.techfun.altrua.user.User;
import com.techfun.altrua.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

/**
 * Serviço responsável pelas regras de negócio relacionadas à autenticação
 * e registro de usuários.
 *
 * <p>
 * Orquestra o fluxo completo de autenticação — registro, login, renovação
 * de token e logout — delegando responsabilidades específicas para
 * {@link RefreshTokenService} e {@link JwtProvider}.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtValidator jwtValidator;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra um novo usuário no sistema.
     *
     * <p>
     * Verifica se o e-mail já está em uso antes de criar o usuário.
     * A senha fornecida é criptografada antes de ser persistida.
     * Caso haja violação de integridade no banco (race condition), a exceção
     * é tratada e convertida para {@link DuplicateResourceException}.
     * </p>
     *
     * @param dto objeto contendo os dados do registro (nome, e-mail e senha)
     * @return {@link AuthResponseDTO} contendo o access token e refresh token
     *         gerados
     * @throws DuplicateResourceException se o e-mail informado já estiver
     *                                    cadastrado
     * @throws RefreshTokenException      se houver erro ao gerar ou persistir o
     *                                    refresh token
     */
    @Transactional
    public AuthResponseDTO register(RegisterUserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("E-mail já cadastrado");
        }

        try {
            User user = User.create(
                    dto.getName(),
                    dto.getEmail(),
                    passwordEncoder.encode(dto.getPassword()));

            userRepository.save(user);

            String token = jwtProvider.generateToken(new UserPrincipal(user));
            String refreshToken = refreshTokenService.create(user);

            return new AuthResponseDTO(token, refreshToken);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException("E-mail já cadastrado");
        }
    }

    /**
     * Realiza a autenticação do usuário com base nas credenciais fornecidas.
     *
     * <p>
     * Utiliza o {@link AuthenticationManager} para validar e-mail e senha.
     * Em caso de sucesso, gera e retorna um par de tokens encapsulado em
     * {@link AuthResponseDTO}.
     * </p>
     *
     * @param dto objeto contendo as credenciais de acesso (e-mail e senha)
     * @return {@link AuthResponseDTO} contendo o access token e refresh token
     *         gerados
     * @throws InvalidCredentialsException se a autenticação falhar por credenciais
     *                                     inválidas
     * @throws RefreshTokenException       se houver erro ao gerar ou persistir o
     *                                     refresh token
     */
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String token = jwtProvider.generateToken(userPrincipal);
            String refreshToken = refreshTokenService.create(userPrincipal.getUser());

            return new AuthResponseDTO(token, refreshToken);
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException();
        }
    }

    /**
     * Renova o par de tokens (Access e Refresh) a partir de um Refresh Token
     * válido.
     *
     * <p>
     * Esta operação realiza as seguintes validações:
     * 1. Verifica se o token possui o claim de tipo específico para Refresh Tokens.
     * 2. Delega ao {@link RefreshTokenService} a rotação (invalidação do antigo e
     * persistência do novo).
     * </p>
     *
     * @param token O Refresh Token enviado no corpo da requisição.
     * @return {@link AuthResponseDTO} contendo o novo Access Token (curta duração)
     *         e o novo Refresh Token (longa duração).
     * @throws RefreshTokenException Se o token não for do tipo 'refresh', ou se
     *                               estiver
     *                               expirado, revogado ou inexistente na base de
     *                               dados.
     */
    @Transactional
    public AuthResponseDTO refresh(String token) {
        if (!jwtValidator.isRefreshToken(token)) {
            throw new RefreshTokenException("Token inválido");
        }

        RotateResult current = refreshTokenService.rotate(token);
        String newAccessToken = jwtProvider.generateToken(new UserPrincipal(current.user()));
        return new AuthResponseDTO(newAccessToken, current.newToken());
    }

    /**
     * Encerra a sessão ativa do usuário através da revogação do Refresh Token.
     *
     * <p>
     * Uma vez revogado, o token é invalidado na camada de persistência, impedindo
     * qualquer tentativa futura de geração de novos Access Tokens.
     * </p>
     *
     * @param token O Refresh Token a ser invalidado.
     * @throws RefreshTokenException Se o token informado não possuir o claim de
     *                               tipo 'refresh'
     *                               ou se não for encontrado no registro de sessões
     *                               ativas.
     */
    @Transactional
    public void logout(String token) {
        if (!jwtValidator.isRefreshToken(token)) {
            throw new RefreshTokenException("Token inválido");
        }

        refreshTokenService.revoke(token);
    }
}