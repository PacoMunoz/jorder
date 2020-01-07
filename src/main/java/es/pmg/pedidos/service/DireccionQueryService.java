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

import es.pmg.pedidos.domain.Direccion;
import es.pmg.pedidos.domain.*; // for static metamodels
import es.pmg.pedidos.repository.DireccionRepository;
import es.pmg.pedidos.service.dto.DireccionCriteria;

/**
 * Service for executing complex queries for {@link Direccion} entities in the database.
 * The main input is a {@link DireccionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Direccion} or a {@link Page} of {@link Direccion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DireccionQueryService extends QueryService<Direccion> {

    private final Logger log = LoggerFactory.getLogger(DireccionQueryService.class);

    private final DireccionRepository direccionRepository;

    public DireccionQueryService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    /**
     * Return a {@link List} of {@link Direccion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Direccion> findByCriteria(DireccionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Direccion> specification = createSpecification(criteria);
        return direccionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Direccion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Direccion> findByCriteria(DireccionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Direccion> specification = createSpecification(criteria);
        return direccionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DireccionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Direccion> specification = createSpecification(criteria);
        return direccionRepository.count(specification);
    }

    /**
     * Function to convert {@link DireccionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Direccion> createSpecification(DireccionCriteria criteria) {
        Specification<Direccion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Direccion_.id));
            }
            if (criteria.getNombreVia() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombreVia(), Direccion_.nombreVia));
            }
            if (criteria.getNumero() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumero(), Direccion_.numero));
            }
            if (criteria.getPiso() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPiso(), Direccion_.piso));
            }
            if (criteria.getBloque() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBloque(), Direccion_.bloque));
            }
            if (criteria.getPuerta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPuerta(), Direccion_.puerta));
            }
            if (criteria.getEscalera() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEscalera(), Direccion_.escalera));
            }
            if (criteria.getUsuarioId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsuarioId(),
                    root -> root.join(Direccion_.usuario, JoinType.LEFT).get(Cliente_.id)));
            }
            if (criteria.getLocalidadId() != null) {
                specification = specification.and(buildSpecification(criteria.getLocalidadId(),
                    root -> root.join(Direccion_.localidad, JoinType.LEFT).get(Localidad_.id)));
            }
        }
        return specification;
    }
}
