package es.pmg.pedidos.service;

import es.pmg.pedidos.domain.Localidad;
import es.pmg.pedidos.repository.LocalidadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Localidad}.
 */
@Service
@Transactional
public class LocalidadService {

    private final Logger log = LoggerFactory.getLogger(LocalidadService.class);

    private final LocalidadRepository localidadRepository;

    public LocalidadService(LocalidadRepository localidadRepository) {
        this.localidadRepository = localidadRepository;
    }

    /**
     * Save a localidad.
     *
     * @param localidad the entity to save.
     * @return the persisted entity.
     */
    public Localidad save(Localidad localidad) {
        log.debug("Request to save Localidad : {}", localidad);
        return localidadRepository.save(localidad);
    }

    /**
     * Get all the localidads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Localidad> findAll(Pageable pageable) {
        log.debug("Request to get all Localidads");
        return localidadRepository.findAll(pageable);
    }


    /**
     * Get one localidad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Localidad> findOne(Long id) {
        log.debug("Request to get Localidad : {}", id);
        return localidadRepository.findById(id);
    }

    /**
     * Delete the localidad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Localidad : {}", id);
        localidadRepository.deleteById(id);
    }
}
