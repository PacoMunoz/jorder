package es.pmg.pedidos.service;

import es.pmg.pedidos.domain.LineaPedido;
import es.pmg.pedidos.repository.LineaPedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link LineaPedido}.
 */
@Service
@Transactional
public class LineaPedidoService {

    private final Logger log = LoggerFactory.getLogger(LineaPedidoService.class);

    private final LineaPedidoRepository lineaPedidoRepository;

    public LineaPedidoService(LineaPedidoRepository lineaPedidoRepository) {
        this.lineaPedidoRepository = lineaPedidoRepository;
    }

    /**
     * Save a lineaPedido.
     *
     * @param lineaPedido the entity to save.
     * @return the persisted entity.
     */
    public LineaPedido save(LineaPedido lineaPedido) {
        log.debug("Request to save LineaPedido : {}", lineaPedido);
        return lineaPedidoRepository.save(lineaPedido);
    }

    /**
     * Get all the lineaPedidos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LineaPedido> findAll(Pageable pageable) {
        log.debug("Request to get all LineaPedidos");
        return lineaPedidoRepository.findAll(pageable);
    }


    /**
     * Get one lineaPedido by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LineaPedido> findOne(Long id) {
        log.debug("Request to get LineaPedido : {}", id);
        return lineaPedidoRepository.findById(id);
    }

    /**
     * Delete the lineaPedido by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete LineaPedido : {}", id);
        lineaPedidoRepository.deleteById(id);
    }
}
