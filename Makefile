# Cores
RESET  = \033[0m
BOLD   = \033[1m
RED    = \033[31m
GREEN  = \033[32m
YELLOW = \033[33m
BLUE   = \033[34m
CYAN   = \033[36m
WHITE  = \033[97m
DIM    = \033[2m

-include .env
export

.PHONY: help run build test dev clean docker-up docker-down logs setup

## Exibe esta mensagem de ajuda
help:
	@printf "$(RESET)\n"
	@printf "$(BOLD)$(CYAN)╔══════════════════════════════════════════╗$(RESET)\n"
	@printf "$(BOLD)$(CYAN)║        🍃  Spring App — Comandos          ║$(RESET)\n"
	@printf "$(BOLD)$(CYAN)╚══════════════════════════════════════════╝$(RESET)\n"
	@printf "\n"
	@printf "$(BOLD)$(WHITE)  DESENVOLVIMENTO$(RESET)\n"
	@printf "  $(GREEN)make setup$(RESET)       $(DIM)Configura o projeto pela primeira vez$(RESET)\n"
	@printf "  $(GREEN)make dev$(RESET)         $(DIM)Sobe o banco e inicia a aplicação$(RESET)\n"
	@printf "  $(GREEN)make run$(RESET)         $(DIM)Inicia apenas a aplicação$(RESET)\n"
	@printf "\n"
	@printf "$(BOLD)$(WHITE)  BUILD & TESTES$(RESET)\n"
	@printf "  $(GREEN)make build$(RESET)       $(DIM)Compila o projeto (sem rodar testes)$(RESET)\n"
	@printf "  $(GREEN)make test$(RESET)        $(DIM)Executa os testes automatizados$(RESET)\n"
	@printf "  $(GREEN)make clean$(RESET)       $(DIM)Remove arquivos de build gerados$(RESET)\n"
	@printf "\n"
	@printf "$(BOLD)$(WHITE)  DOCKER$(RESET)\n"
	@printf "  $(GREEN)make docker-up$(RESET)   $(DIM)Sobe os containers em background$(RESET)\n"
	@printf "  $(GREEN)make docker-down$(RESET) $(DIM)Para e remove os containers$(RESET)\n"
	@printf "  $(GREEN)make logs$(RESET)        $(DIM)Exibe os logs do PostgreSQL em tempo real$(RESET)\n"
	@printf "\n"

## Configura o projeto: cria o .env a partir do .env.example
setup:
	@printf "$(CYAN)⚙️  Configurando o projeto...$(RESET)\n"
	@if [ -f .env ]; then \
		printf "$(YELLOW)⚠️  Arquivo .env já existe — nenhuma alteração feita.$(RESET)\n"; \
	else \
		cp .env.example .env; \
		printf "$(GREEN)✅ .env criado a partir do .env.example$(RESET)\n"; \
		printf "$(DIM)   Preencha as credenciais antes de rodar o projeto.$(RESET)\n"; \
	fi

## Inicia apenas a aplicação Spring
run:
	@printf "$(CYAN)🚀 Iniciando a aplicação...$(RESET)\n"
	@./mvnw spring-boot:run

## Compila o projeto sem rodar os testes
build:
	@printf "$(CYAN)🔨 Compilando o projeto...$(RESET)\n"
	@./mvnw clean package -DskipTests
	@printf "$(GREEN)✅ Build concluído.$(RESET)\n"

## Executa os testes automatizados
test:
	@printf "$(CYAN)🧪 Executando testes...$(RESET)\n"
	@./mvnw test

## Sobe o banco e inicia a aplicação
dev: docker-up run

## Remove os arquivos de build
clean:
	@printf "$(YELLOW)🧹 Limpando arquivos de build...$(RESET)\n"
	@./mvnw clean
	@printf "$(GREEN)✅ Limpeza concluída.$(RESET)\n"

## Sobe os containers Docker em background
docker-up:
	@printf "$(CYAN)🐳 Subindo containers...$(RESET)\n"
	@docker compose up -d
	@printf "$(GREEN)✅ Containers no ar.$(RESET)\n"

## Para e remove os containers Docker
docker-down:
	@printf "$(YELLOW)🛑 Parando containers...$(RESET)\n"
	@docker compose down
	@printf "$(GREEN)✅ Containers encerrados.$(RESET)\n"

## Exibe os logs do PostgreSQL em tempo real
logs:
	@printf "$(CYAN)📋 Logs do PostgreSQL (Ctrl+C para sair)...$(RESET)\n"
	@docker compose logs -f postgres