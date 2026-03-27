package com.devix.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "compania_theme")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompaniaTheme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "no_cia", nullable = false, unique = true)
    private Long noCia;

    @NotNull
    @Size(max = 50)
    @Column(name = "theme_name", nullable = false, length = 50)
    private String themeName;

    @Size(max = 256)
    @Column(name = "logo_path", length = 256)
    private String logoPath;

    @Size(max = 256)
    @Column(name = "background_path", length = 256)
    private String backgroundPath;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNoCia() {
        return this.noCia;
    }

    public void setNoCia(Long noCia) {
        this.noCia = noCia;
    }

    public String getThemeName() {
        return this.themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getLogoPath() {
        return this.logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getBackgroundPath() {
        return this.backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompaniaTheme)) {
            return false;
        }
        return getId() != null && getId().equals(((CompaniaTheme) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "CompaniaTheme{" +
            "id=" +
            getId() +
            ", noCia=" +
            getNoCia() +
            ", themeName='" +
            getThemeName() +
            "'" +
            ", logoPath='" +
            getLogoPath() +
            "'" +
            ", backgroundPath='" +
            getBackgroundPath() +
            "'" +
            "}"
        );
    }
}
