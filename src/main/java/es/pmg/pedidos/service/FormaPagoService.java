package es.pmg.pedidos.service;

import es.pmg.pedidos.domain.FormaPago;
import es.pmg.pedidos.repository.FormaPagoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FormaPago}.
 */
@Service
@Transactional
public class FormaPagoService {

    private final Logger log = LoggerFactory.getLogger(FormaPagoService.class);

    private final FormaPagoRepository formaPagoRepository;

    public FormaPagoService(FormaPagoRepository formaPagoRepository) {
        this.formaPagoRepository = formaPagoRepository;
    }

    /**
     * Save a formaPago.
     *
     * @param formaPago the entity to save.
     * @return the persisted entity.
     */
    public FormaPago save(FormaPago formaPago) {
        log.debug("Request to save FormaPago : {}", formaPago);
        return formaPagoRepository.save(formaPago);
    }

    /**
     * Get all the formaPagos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FormaPago> findAll(Pageable pageable) {
        log.debug("Request to get all FormaPagos");
        return formaPagoRepository.findAll(pageable);
    }


    /**
     * Get one formaPago by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FormaPago> findOne(Long id) {
        log.debug("Request to get FormaPago : {}", id);
        return formaPagoRepository.findById(id);
    }

    /**
     * Delete the formaPago by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FormaPago : {}", id);
        formaPagoRepository.deleteById(id);
    }
}
