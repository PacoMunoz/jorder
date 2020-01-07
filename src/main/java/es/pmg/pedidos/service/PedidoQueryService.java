package es.pmg.pedidos.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import es.pmg.pedidos.domain.Pedido;
import es.pmg.pedidos.domain.*; // for static metamodels
import es.pmg.pedidos.repository.PedidoRepository;
import es.pmg.pedidos.service.dto.PedidoCriteria;

/**
 * Service for executing complex queries for {@link Pedido} entities in the database.
 * The main input is a {@link PedidoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Pedido} or a {@link Page} of {@link Pedido} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PedidoQueryService extends QueryService<Pedido> {

    private final Logger log = LoggerFactory.getLogger(PedidoQueryService.class);

    private final PedidoRepository pedidoRepository;

    public PedidoQueryService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    /**
     * Return a {@link List} of {@link Pedido} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Pedido> findByCriteria(PedidoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Pedido} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Pedido> findByCriteria(PedidoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PedidoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Pedido> specification = createSpecification(criteria);
        return pedidoRepository.count(specification);
    }

    /**
     * Function to convert {@link PedidoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pedido> createSpecification(PedidoCriteria criteria) {
        Specification<Pedido> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pedido_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), Pedido_.codigo));
            }
            if (criteria.getaDomicilio() != null) {
                specification = specification.and(buildSpecification(criteria.getaDomicilio(), Pedido_.aDomicilio));
            }
            if (criteria.getFechaPedido() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaPedido(), Pedido_.fechaPedido));
            }
            if (criteria.getDireccionId() != null) {
                specification = specification.and(buildSpecification(criteria.getDireccionId(),
                    root -> root.join(Pedido_.direccion, JoinType.LEFT).get(Direccion_.id)));
            }
            if (criteria.getClienteId() != null) {
                specification = specification.and(buildSpecification(criteria.getClienteId(),
                    root -> root.join(Pedido_.cliente, JoinType.LEFT).get(Cliente_.id)));
            }
            if (criteria.getFormaPagoId() != null) {
                specification = specification.and(buildSpecification(criteria.getFormaPagoId(),
                    root -> root.join(Pedido_.formaPago, JoinType.LEFT).get(FormaPago_.id)));
            }
            if (criteria.getEstadoPedidoId() != null) {
                specification = specification.and(buildSpecification(criteria.getEstadoPedidoId(),
                    root -> root.join(Pedido_.estadoPedido, JoinType.LEFT).get(EstadoPedido_.id)));
            }
        }
        return specification;
    }
}
