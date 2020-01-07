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
 * Criteria class for the {@link es.pmg.pedidos.domain.LineaPedido} entity. This class is used
 * in {@link es.pmg.pedidos.web.rest.LineaPedidoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /linea-pedidos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LineaPedidoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter cantidad;

    private LongFilter pedidoId;

    private LongFilter productoId;

    public LineaPedidoCriteria(){
    }

    public LineaPedidoCriteria(LineaPedidoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.pedidoId = other.pedidoId == null ? null : other.pedidoId.copy();
        this.productoId = other.productoId == null ? null : other.productoId.copy();
    }

    @Override
    public LineaPedidoCriteria copy() {
        return new LineaPedidoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public LongFilter getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(LongFilter pedidoId) {
        this.pedidoId = pedidoId;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LineaPedidoCriteria that = (LineaPedidoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(pedidoId, that.pedidoId) &&
            Objects.equals(productoId, that.productoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        cantidad,
        pedidoId,
        productoId
        );
    }

    @Override
    public String toString() {
        return "LineaPedidoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
                (pedidoId != null ? "pedidoId=" + pedidoId + ", " : "") +
                (productoId != null ? "productoId=" + productoId + ", " : "") +
            "}";
    }

}
