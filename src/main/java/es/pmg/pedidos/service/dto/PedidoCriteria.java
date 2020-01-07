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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link es.pmg.pedidos.domain.Pedido} entity. This class is used
 * in {@link es.pmg.pedidos.web.rest.PedidoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pedidos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PedidoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter codigo;

    private BooleanFilter aDomicilio;

    private InstantFilter fechaPedido;

    private LongFilter direccionId;

    private LongFilter clienteId;

    private LongFilter formaPagoId;

    private LongFilter estadoPedidoId;

    public PedidoCriteria(){
    }

    public PedidoCriteria(PedidoCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.aDomicilio = other.aDomicilio == null ? null : other.aDomicilio.copy();
        this.fechaPedido = other.fechaPedido == null ? null : other.fechaPedido.copy();
        this.direccionId = other.direccionId == null ? null : other.direccionId.copy();
        this.clienteId = other.clienteId == null ? null : other.clienteId.copy();
        this.formaPagoId = other.formaPagoId == null ? null : other.formaPagoId.copy();
        this.estadoPedidoId = other.estadoPedidoId == null ? null : other.estadoPedidoId.copy();
    }

    @Override
    public PedidoCriteria copy() {
        return new PedidoCriteria(this);
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

    public BooleanFilter getaDomicilio() {
        return aDomicilio;
    }

    public void setaDomicilio(BooleanFilter aDomicilio) {
        this.aDomicilio = aDomicilio;
    }

    public InstantFilter getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(InstantFilter fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LongFilter getDireccionId() {
        return direccionId;
    }

    public void setDireccionId(LongFilter direccionId) {
        this.direccionId = direccionId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    public LongFilter getFormaPagoId() {
        return formaPagoId;
    }

    public void setFormaPagoId(LongFilter formaPagoId) {
        this.formaPagoId = formaPagoId;
    }

    public LongFilter getEstadoPedidoId() {
        return estadoPedidoId;
    }

    public void setEstadoPedidoId(LongFilter estadoPedidoId) {
        this.estadoPedidoId = estadoPedidoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PedidoCriteria that = (PedidoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(aDomicilio, that.aDomicilio) &&
            Objects.equals(fechaPedido, that.fechaPedido) &&
            Objects.equals(direccionId, that.direccionId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(formaPagoId, that.formaPagoId) &&
            Objects.equals(estadoPedidoId, that.estadoPedidoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        codigo,
        aDomicilio,
        fechaPedido,
        direccionId,
        clienteId,
        formaPagoId,
        estadoPedidoId
        );
    }

    @Override
    public String toString() {
        return "PedidoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (codigo != null ? "codigo=" + codigo + ", " : "") +
                (aDomicilio != null ? "aDomicilio=" + aDomicilio + ", " : "") +
                (fechaPedido != null ? "fechaPedido=" + fechaPedido + ", " : "") +
                (direccionId != null ? "direccionId=" + direccionId + ", " : "") +
                (clienteId != null ? "clienteId=" + clienteId + ", " : "") +
                (formaPagoId != null ? "formaPagoId=" + formaPagoId + ", " : "") +
                (estadoPedidoId != null ? "estadoPedidoId=" + estadoPedidoId + ", " : "") +
            "}";
    }

}
