package es.pmg.pedidos.service;

import es.pmg.pedidos.domain.Familia;
import es.pmg.pedidos.repository.FamiliaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Familia}.
 */
@Service
@Transactional
public class FamiliaService {

    private final Logger log = LoggerFactory.getLogger(FamiliaService.class);

    private final FamiliaRepository familiaRepository;

    public FamiliaService(FamiliaRepository familiaRepository) {
        this.familiaRepository = familiaRepository;
    }

    /**
     * Save a familia.
     *
     * @param familia the entity to save.
     * @return the persisted entity.
     */
    public Familia save(Familia familia) {
        log.debug("Request to save Familia : {}", familia);
        return familiaRepository.save(familia);
    }

    /**
     * Get all the familias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Familia> findAll(Pageable pageable) {
        log.debug("Request to get all Familias");
        return familiaRepository.findAll(pageable);
    }


    /**
     * Get one familia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Familia> findOne(Long id) {
        log.debug("Request to get Familia : {}", id);
        return familiaRepository.findById(id);
    }

    /**
     * Delete the familia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Familia : {}", id);
        familiaRepository.deleteById(id);
    }
}
