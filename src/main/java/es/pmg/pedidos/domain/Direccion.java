package es.pmg.pedidos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Direccion.
 */
@Entity
@Table(name = "direccion")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre_via", nullable = false)
    private String nombreVia;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "piso")
    private String piso;

    @Column(name = "bloque")
    private String bloque;

    @Column(name = "puerta")
    private String puerta;

    @Column(name = "escalera")
    private String escalera;

    @ManyToOne
    @JsonIgnoreProperties("direccions")
    private Cliente usuario;

    @ManyToOne
    @JsonIgnoreProperties("direccions")
    private Localidad localidad;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreVia() {
        return nombreVia;
    }

    public Direccion nombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
        return this;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    public Integer getNumero() {
        return numero;
    }

    public Direccion numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public Direccion piso(String piso) {
        this.piso = piso;
        return this;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getBloque() {
        return bloque;
    }

    public Direccion bloque(String bloque) {
        this.bloque = bloque;
        return this;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getPuerta() {
        return puerta;
    }

    public Direccion puerta(String puerta) {
        this.puerta = puerta;
        return this;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public String getEscalera() {
        return escalera;
    }

    public Direccion escalera(String escalera) {
        this.escalera = escalera;
        return this;
    }

    public void setEscalera(String escalera) {
        this.escalera = escalera;
    }

    public Cliente getUsuario() {
        return usuario;
    }

    public Direccion usuario(Cliente cliente) {
        this.usuario = cliente;
        return this;
    }

    public void setUsuario(Cliente cliente) {
        this.usuario = cliente;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public Direccion localidad(Localidad localidad) {
        this.localidad = localidad;
        return this;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Direccion)) {
            return false;
        }
        return id != null && id.equals(((Direccion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Direccion{" +
            "id=" + getId() +
            ", nombreVia='" + getNombreVia() + "'" +
            ", numero=" + getNumero() +
            ", piso='" + getPiso() + "'" +
            ", bloque='" + getBloque() + "'" +
            ", puerta='" + getPuerta() + "'" +
            ", escalera='" + getEscalera() + "'" +
            "}";
    }
}
