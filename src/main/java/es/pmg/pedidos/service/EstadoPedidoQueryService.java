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

import es.pmg.pedidos.domain.EstadoPedido;
import es.pmg.pedidos.domain.*; // for static metamodels
import es.pmg.pedidos.repository.EstadoPedidoRepository;
import es.pmg.pedidos.service.dto.EstadoPedidoCriteria;

/**
 * Service for executing complex queries for {@link EstadoPedido} entities in the database.
 * The main input is a {@link EstadoPedidoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EstadoPedido} or a {@link Page} of {@link EstadoPedido} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EstadoPedidoQueryService extends QueryService<EstadoPedido> {

    private final Logger log = LoggerFactory.getLogger(EstadoPedidoQueryService.class);

    private final EstadoPedidoRepository estadoPedidoRepository;

    public EstadoPedidoQueryService(EstadoPedidoRepository estadoPedidoRepository) {
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    /**
     * Return a {@link List} of {@link EstadoPedido} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EstadoPedido> findByCriteria(EstadoPedidoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EstadoPedido> specification = createSpecification(criteria);
        return estadoPedidoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EstadoPedido} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EstadoPedido> findByCriteria(EstadoPedidoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EstadoPedido> specification = createSpecification(criteria);
        return estadoPedidoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EstadoPedidoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EstadoPedido> specification = createSpecification(criteria);
        return estadoPedidoRepository.count(specification);
    }

    /**
     * Function to convert {@link EstadoPedidoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EstadoPedido> createSpecification(EstadoPedidoCriteria criteria) {
        Specification<EstadoPedido> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EstadoPedido_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), EstadoPedido_.codigo));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), EstadoPedido_.descripcion));
            }
        }
        return specification;
    }
}
