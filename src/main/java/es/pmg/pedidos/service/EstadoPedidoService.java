package es.pmg.pedidos.service;

import es.pmg.pedidos.domain.EstadoPedido;
import es.pmg.pedidos.repository.EstadoPedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EstadoPedido}.
 */
@Service
@Transactional
public class EstadoPedidoService {

    private final Logger log = LoggerFactory.getLogger(EstadoPedidoService.class);

    private final EstadoPedidoRepository estadoPedidoRepository;

    public EstadoPedidoService(EstadoPedidoRepository estadoPedidoRepository) {
        this.estadoPedidoRepository = estadoPedidoRepository;
    }

    /**
     * Save a estadoPedido.
     *
     * @param estadoPedido the entity to save.
     * @return the persisted entity.
     */
    public EstadoPedido save(EstadoPedido estadoPedido) {
        log.debug("Request to save EstadoPedido : {}", estadoPedido);
        return estadoPedidoRepository.save(estadoPedido);
    }

    /**
     * Get all the estadoPedidos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EstadoPedido> findAll(Pageable pageable) {
        log.debug("Request to get all EstadoPedidos");
        return estadoPedidoRepository.findAll(pageable);
    }


    /**
     * Get one estadoPedido by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EstadoPedido> findOne(Long id) {
        log.debug("Request to get EstadoPedido : {}", id);
        return estadoPedidoRepository.findById(id);
    }

    /**
     * Delete the estadoPedido by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EstadoPedido : {}", id);
        estadoPedidoRepository.deleteById(id);
    }
}
