package com.diplomado.model;

import lombok.Data;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class AsistenciaMiembroId implements Serializable {

    private int sesionId;
    private int miembroId;

    public AsistenciaMiembroId() {}

    public AsistenciaMiembroId(int sesionId, int miembroId) {
        this.sesionId = sesionId;
        this.miembroId = miembroId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsistenciaMiembroId that = (AsistenciaMiembroId) o;
        return sesionId == that.sesionId && miembroId == that.miembroId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sesionId, miembroId);
    }
}


