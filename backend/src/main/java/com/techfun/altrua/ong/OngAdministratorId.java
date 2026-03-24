package com.techfun.altrua.ong;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class OngAdministratorId implements Serializable {

    private UUID userId;
    private UUID ongId;

    protected OngAdministratorId() {
    }

    public OngAdministratorId(UUID userId, UUID ongId) {
        this.userId = userId;
        this.ongId = ongId;
    }

}
