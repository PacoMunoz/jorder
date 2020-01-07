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
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link es.pmg.pedidos.domain.Producto} entity. This class is used
 * in {@link es.pmg.pedidos.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private StringFilter descripcion;

    private BigDecimalFilter precio;

    private BooleanFilter disponible;

    private LongFilter familiaId;

    public ProductoCriteria(){
    }

    public ProductoCriteria(ProductoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.precio = other.precio == null ? null : other.precio.copy();
        this.disponible = other.disponible == null ? null : other.disponible.copy();
        this.familiaId = other.familiaId == null ? null : other.familiaId.copy();
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCodigo() {
        return codigo;
    }

    public void setCodigo(StringFilter codigo) {
        this.codigo = codigo;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimalFilter getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimalFilter precio) {
        this.precio = precio;
    }

    public BooleanFilter getDisponible() {
        return disponible;
    }

    public void setDisponible(BooleanFilter disponible) {
        this.disponible = disponible;
    }

    public LongFilter getFamiliaId() {
        return familiaId;
    }

    public void setFamiliaId(LongFilter familiaId) {
        this.familiaId = familiaId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductoCriteria that = (ProductoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(disponible, that.disponible) &&
            Objects.equals(familiaId, that.familiaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        codigo,
        descripcion,
        precio,
        disponible,
        familiaId
        );
    }

    @Override
    public String toString() {
        return "ProductoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (codigo != null ? "codigo=" + codigo + ", " : "") +
                (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
                (precio != null ? "precio=" + precio + ", " : "") +
                (disponible != null ? "disponible=" + disponible + ", " : "") +
                (familiaId != null ? "familiaId=" + familiaId + ", " : "") +
            "}";
    }

}
