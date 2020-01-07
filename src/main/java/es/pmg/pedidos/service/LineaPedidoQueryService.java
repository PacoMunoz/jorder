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

import es.pmg.pedidos.domain.LineaPedido;
import es.pmg.pedidos.domain.*; // for static metamodels
import es.pmg.pedidos.repository.LineaPedidoRepository;
import es.pmg.pedidos.service.dto.LineaPedidoCriteria;

/**
 * Service for executing complex queries for {@link LineaPedido} entities in the database.
 * The main input is a {@link LineaPedidoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LineaPedido} or a {@link Page} of {@link LineaPedido} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LineaPedidoQueryService extends QueryService<LineaPedido> {

    private final Logger log = LoggerFactory.getLogger(LineaPedidoQueryService.class);

    private final LineaPedidoRepository lineaPedidoRepository;

    public LineaPedidoQueryService(LineaPedidoRepository lineaPedidoRepository) {
        this.lineaPedidoRepository = lineaPedidoRepository;
    }

    /**
     * Return a {@link List} of {@link LineaPedido} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LineaPedido> findByCriteria(LineaPedidoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LineaPedido> specification = createSpecification(criteria);
        return lineaPedidoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link LineaPedido} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LineaPedido> findByCriteria(LineaPedidoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LineaPedido> specification = createSpecification(criteria);
        return lineaPedidoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LineaPedidoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LineaPedido> specification = createSpecification(criteria);
        return lineaPedidoRepository.count(specification);
    }

    /**
     * Function to convert {@link LineaPedidoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LineaPedido> createSpecification(LineaPedidoCriteria criteria) {
        Specification<LineaPedido> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LineaPedido_.id));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), LineaPedido_.cantidad));
            }
            if (criteria.getPedidoId() != null) {
                specification = specification.and(buildSpecification(criteria.getPedidoId(),
                    root -> root.join(LineaPedido_.pedido, JoinType.LEFT).get(Pedido_.id)));
            }
            if (criteria.getProductoId() != null) {
                specification = specification.and(buildSpecification(criteria.getProductoId(),
                    root -> root.join(LineaPedido_.producto, JoinType.LEFT).get(Producto_.id)));
            }
        }
        return specification;
    }
}
