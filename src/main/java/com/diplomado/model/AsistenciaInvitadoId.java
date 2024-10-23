package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class AsistenciaInvitadoId implements Serializable {

    private int sesionId; // Corresponde a SESION_IDSESION
    private int invitadoId; // Corresponde a INVITADO_IDINVITADO

    // Constructor vacío para JPA
    public AsistenciaInvitadoId() {}

    // Constructor con parámetros
    public AsistenciaInvitadoId(int sesionId, int invitadoId) {
        this.sesionId = sesionId;
        this.invitadoId = invitadoId;
    }

    // Sobrescribir equals() y hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsistenciaInvitadoId that = (AsistenciaInvitadoId) o;
        return sesionId == that.sesionId && invitadoId == that.invitadoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sesionId, invitadoId);
    }
}

