package com.devix.service.historiaclinica.dto;

import java.io.Serializable;
import java.util.Objects;

public class CiudadRefDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CiudadRefDTO)) return false;
        CiudadRefDTO that = (CiudadRefDTO) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "CiudadRefDTO{id=" + id + "}";
    }
}
