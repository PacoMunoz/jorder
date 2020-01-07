package es.pmg.pedidos.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Pedido.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 8)
    @Column(name = "codigo", length = 8, nullable = false)
    private String codigo;

    @Column(name = "a_domicilio")
    private Boolean aDomicilio;

    @NotNull
    @Column(name = "fecha_pedido", nullable = false)
    private Instant fechaPedido;

    @OneToOne
    @JoinColumn(unique = true)
    private Direccion direccion;

    @ManyToOne
    @JsonIgnoreProperties("pedidos")
    private Cliente cliente;

    @ManyToOne
    @JsonIgnoreProperties("pedidos")
    private FormaPago formaPago;

    @ManyToOne
    @JsonIgnoreProperties("pedidos")
    private EstadoPedido estadoPedido;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Pedido codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean isaDomicilio() {
        return aDomicilio;
    }

    public Pedido aDomicilio(Boolean aDomicilio) {
        this.aDomicilio = aDomicilio;
        return this;
    }

    public void setaDomicilio(Boolean aDomicilio) {
        this.aDomicilio = aDomicilio;
    }

    public Instant getFechaPedido() {
        return fechaPedido;
    }

    public Pedido fechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
        return this;
    }

    public void setFechaPedido(Instant fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public Pedido direccion(Direccion direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Pedido cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public Pedido formaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
        return this;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public Pedido estadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
        return this;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido)) {
            return false;
        }
        return id != null && id.equals(((Pedido) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", aDomicilio='" + isaDomicilio() + "'" +
            ", fechaPedido='" + getFechaPedido() + "'" +
            "}";
    }
}
