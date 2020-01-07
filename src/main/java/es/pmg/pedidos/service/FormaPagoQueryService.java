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

import es.pmg.pedidos.domain.FormaPago;
import es.pmg.pedidos.domain.*; // for static metamodels
import es.pmg.pedidos.repository.FormaPagoRepository;
import es.pmg.pedidos.service.dto.FormaPagoCriteria;

/**
 * Service for executing complex queries for {@link FormaPago} entities in the database.
 * The main input is a {@link FormaPagoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FormaPago} or a {@link Page} of {@link FormaPago} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FormaPagoQueryService extends QueryService<FormaPago> {

    private final Logger log = LoggerFactory.getLogger(FormaPagoQueryService.class);

    private final FormaPagoRepository formaPagoRepository;

    public FormaPagoQueryService(FormaPagoRepository formaPagoRepository) {
        this.formaPagoRepository = formaPagoRepository;
    }

    /**
     * Return a {@link List} of {@link FormaPago} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FormaPago> findByCriteria(FormaPagoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FormaPago> specification = createSpecification(criteria);
        return formaPagoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link FormaPago} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FormaPago> findByCriteria(FormaPagoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FormaPago> specification = createSpecification(criteria);
        return formaPagoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FormaPagoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FormaPago> specification = createSpecification(criteria);
        return formaPagoRepository.count(specification);
    }

    /**
     * Function to convert {@link FormaPagoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FormaPago> createSpecification(FormaPagoCriteria criteria) {
        Specification<FormaPago> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FormaPago_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), FormaPago_.codigo));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), FormaPago_.descripcion));
            }
        }
        return specification;
    }
}
