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

import es.pmg.pedidos.domain.Localidad;
import es.pmg.pedidos.domain.*; // for static metamodels
import es.pmg.pedidos.repository.LocalidadRepository;
import es.pmg.pedidos.service.dto.LocalidadCriteria;

/**
 * Service for executing complex queries for {@link Localidad} entities in the database.
 * The main input is a {@link LocalidadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Localidad} or a {@link Page} of {@link Localidad} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LocalidadQueryService extends QueryService<Localidad> {

    private final Logger log = LoggerFactory.getLogger(LocalidadQueryService.class);

    private final LocalidadRepository localidadRepository;

    public LocalidadQueryService(LocalidadRepository localidadRepository) {
        this.localidadRepository = localidadRepository;
    }

    /**
     * Return a {@link List} of {@link Localidad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Localidad> findByCriteria(LocalidadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Localidad> specification = createSpecification(criteria);
        return localidadRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Localidad} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Localidad> findByCriteria(LocalidadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Localidad> specification = createSpecification(criteria);
        return localidadRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LocalidadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Localidad> specification = createSpecification(criteria);
        return localidadRepository.count(specification);
    }

    /**
     * Function to convert {@link LocalidadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Localidad> createSpecification(LocalidadCriteria criteria) {
        Specification<Localidad> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Localidad_.id));
            }
            if (criteria.getCodigo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCodigo(), Localidad_.codigo));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Localidad_.nombre));
            }
            if (criteria.getCodigoPostal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCodigoPostal(), Localidad_.codigoPostal));
            }
        }
        return specification;
    }
}
