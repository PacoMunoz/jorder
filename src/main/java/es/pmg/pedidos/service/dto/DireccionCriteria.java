package es.pmg.pedidos.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link es.pmg.pedidos.domain.Direccion} entity. This class is used
 * in {@link es.pmg.pedidos.web.rest.DireccionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /direccions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DireccionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreVia;

    private IntegerFilter numero;

    private StringFilter piso;

    private StringFilter bloque;

    private StringFilter puerta;

    private StringFilter escalera;

    private LongFilter usuarioId;

    private LongFilter localidadId;

    public DireccionCriteria(){
    }

    public DireccionCriteria(DireccionCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.nombreVia = other.nombreVia == null ? null : other.nombreVia.copy();
        this.numero = other.numero == null ? null : other.numero.copy();
        this.piso = other.piso == null ? null : other.piso.copy();
        this.bloque = other.bloque == null ? null : other.bloque.copy();
        this.puerta = other.puerta == null ? null : other.puerta.copy();
        this.escalera = other.escalera == null ? null : other.escalera.copy();
        this.usuarioId = other.usuarioId == null ? null : other.usuarioId.copy();
        this.localidadId = other.localidadId == null ? null : other.localidadId.copy();
    }

    @Override
    public DireccionCriteria copy() {
        return new DireccionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(StringFilter nombreVia) {
        this.nombreVia = nombreVia;
    }

    public IntegerFilter getNumero() {
        return numero;
    }

    public void setNumero(IntegerFilter numero) {
        this.numero = numero;
    }

    public StringFilter getPiso() {
        return piso;
    }

    public void setPiso(StringFilter piso) {
        this.piso = piso;
    }

    public StringFilter getBloque() {
        return bloque;
    }

    public void setBloque(StringFilter bloque) {
        this.bloque = bloque;
    }

    public StringFilter getPuerta() {
        return puerta;
    }

    public void setPuerta(StringFilter puerta) {
        this.puerta = puerta;
    }

    public StringFilter getEscalera() {
        return escalera;
    }

    public void setEscalera(StringFilter escalera) {
        this.escalera = escalera;
    }

    public LongFilter getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(LongFilter usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LongFilter getLocalidadId() {
        return localidadId;
    }

    public void setLocalidadId(LongFilter localidadId) {
        this.localidadId = localidadId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DireccionCriteria that = (DireccionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nombreVia, that.nombreVia) &&
            Objects.equals(numero, that.numero) &&
            Objects.equals(piso, that.piso) &&
            Objects.equals(bloque, that.bloque) &&
            Objects.equals(puerta, that.puerta) &&
            Objects.equals(escalera, that.escalera) &&
            Objects.equals(usuarioId, that.usuarioId) &&
            Objects.equals(localidadId, that.localidadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nombreVia,
        numero,
        piso,
        bloque,
        puerta,
        escalera,
        usuarioId,
        localidadId
        );
    }

    @Override
    public String toString() {
        return "DireccionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nombreVia != null ? "nombreVia=" + nombreVia + ", " : "") +
                (numero != null ? "numero=" + numero + ", " : "") +
                (piso != null ? "piso=" + piso + ", " : "") +
                (bloque != null ? "bloque=" + bloque + ", " : "") +
                (puerta != null ? "puerta=" + puerta + ", " : "") +
                (escalera != null ? "escalera=" + escalera + ", " : "") +
                (usuarioId != null ? "usuarioId=" + usuarioId + ", " : "") +
                (localidadId != null ? "localidadId=" + localidadId + ", " : "") +
            "}";
    }

}
