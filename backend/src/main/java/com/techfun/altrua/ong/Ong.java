package com.techfun.altrua.ong;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ongs")
@SQLDelete(sql = "UPDATE ongs SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
public class Ong {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    private UUID id;

    @OneToMany(mappedBy = "ong", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<OngAdministrator> administrators = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @Setter(AccessLevel.NONE)
    private String slug;

    @Column(unique = true, length = 14)
    private String cnpj;

    private String description;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String category;

    private String status;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "banner_url", length = 500)
    private String bannerUrl;

    @Column(name = "donation_info")
    private String donationInfo;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @Setter(AccessLevel.NONE)
    private Instant updatedAt;

    @Column(name = "deleted_at")
    @Setter(AccessLevel.NONE)
    private Instant deletedAt;

    protected Ong() {
    }

    private Ong(String name, String slug, String email, String category) {
        this.name = name;
        this.slug = slug;
        this.email = email;
        this.category = category;
    }

    private Ong(String name, String slug, String email, String category,
            String cnpj, String description, String phone,
            String logoUrl, String bannerUrl, String donationInfo,
            String status, BigDecimal latitude, BigDecimal longitude,
            Instant createdAt, Instant updatedAt, Instant deletedAt) {
        this(name, slug, email, category);
        this.cnpj = cnpj;
        this.description = description;
        this.phone = phone;
        this.logoUrl = logoUrl;
        this.bannerUrl = bannerUrl;
        this.donationInfo = donationInfo;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public void addAdministrator(OngAdministrator administrator) {
        this.administrators.add(administrator);
    }

    public void removeAdministrator(OngAdministrator administrator) {
        if (administrator.isCreator()) {
            throw new IllegalStateException("O criador da ONG não pode ser removido.");
        }
        this.administrators.remove(administrator);
    }

    public static Ong create(String name, String slug, String email, String category) {
        return new Ong(name, slug, email, category);
    }

    public static Ong reconstitute(String name, String slug, String email, String category,
            String cnpj, String description, String phone,
            String logoUrl, String bannerUrl, String donationInfo,
            String status, BigDecimal latitude, BigDecimal longitude,
            Instant createdAt, Instant updatedAt, Instant deletedAt) {
        return new Ong(name, slug, email, category, cnpj, description, phone,
                logoUrl, bannerUrl, donationInfo, status, latitude, longitude,
                createdAt, updatedAt, deletedAt);
    }

    @PrePersist
    public void onPersist() {
        if (this.createdAt == null)
            this.createdAt = Instant.now();
        if (this.updatedAt == null)
            this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

}