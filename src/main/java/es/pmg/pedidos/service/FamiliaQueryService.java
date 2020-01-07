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

import es.pmg.pedidos.domain.Familia;
import es.pmg.pedidos.domain.*; // for static metamodels
import es.pmg.pedidos.repository.FamiliaRepository;
import es.pmg.pedidos.service.dto.FamiliaCriteria;

/**
 * Service for executing complex queries for {@link Familia} entities in the database.
 * The main input is a {@link FamiliaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Familia} or a {@link Page} of {@link Familia} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FamiliaQueryService extends QueryService<Familia> {

    private final Logger log = LoggerFactory.getLogger(FamiliaQueryService.class);

    private final FamiliaRepository familiaRepository;

    public FamiliaQueryService(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    /**
     * Return a {@link List} of {@link Familia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Familia> findByCriteria(FamiliaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Familia> specification = createSpecification(criteria);
        return familiaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Familia} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Familia> findByCriteria(FamiliaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Familia> specification = createSpecification(criteria);
        return familiaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FamiliaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Familia> specification = createSpecification(criteria);
        return familiaRepository.count(specification);
    }

    /**
     * Function to convert {@link FamiliaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Familia> createSpecification(FamiliaCriteria criteria) {
        Specification<Familia> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Familia_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), Familia_.codigo));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Familia_.descripcion));
            }
        }
        return specification;
    }
}
