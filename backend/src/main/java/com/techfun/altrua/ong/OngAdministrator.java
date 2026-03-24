package com.techfun.altrua.ong;

import java.time.Instant;

import com.techfun.altrua.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "ongs_administrators")
@Getter
public class OngAdministrator {

    @EmbeddedId
    private OngAdministratorId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("ongId")
    @JoinColumn(name = "ong_id")
    private Ong ong;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    @Column(name = "is_creator", nullable = false)
    private boolean creator;

    protected OngAdministrator() {
    }

    public OngAdministrator(User user, Ong ong, boolean creator) {
        this.id = new OngAdministratorId(user.getId(), ong.getId());
        this.user = user;
        this.ong = ong;
        this.creator = creator;
    }

    @PrePersist
    public void onPersist() {
        this.assignedAt = Instant.now();
    }

}
