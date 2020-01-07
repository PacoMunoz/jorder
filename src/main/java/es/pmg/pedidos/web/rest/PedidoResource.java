package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.domain.Pedido;
import es.pmg.pedidos.service.PedidoService;
import es.pmg.pedidos.web.rest.errors.BadRequestAlertException;
import es.pmg.pedidos.service.dto.PedidoCriteria;
import es.pmg.pedidos.service.PedidoQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link es.pmg.pedidos.domain.Pedido}.
 */
@RestController
@RequestMapping("/api")
public class PedidoResource {

    private final Logger log = LoggerFactory.getLogger(PedidoResource.class);

    private static final String ENTITY_NAME = "pedido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PedidoService pedidoService;

    private final PedidoQueryService pedidoQueryService;

    public PedidoResource(PedidoService pedidoService, PedidoQueryService pedidoQueryService) {
        this.pedidoService = pedidoService;
        this.pedidoQueryService = pedidoQueryService;
    }

    /**
     * {@code POST  /pedidos} : Create a new pedido.
     *
     * @param pedido the pedido to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pedido, or with status {@code 400 (Bad Request)} if the pedido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pedidos")
    public ResponseEntity<Pedido> createPedido(@Valid @RequestBody Pedido pedido) throws URISyntaxException {
        log.debug("REST request to save Pedido : {}", pedido);
        if (pedido.getId() != null) {
            throw new BadRequestAlertException("A new pedido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pedido result = pedidoService.save(pedido);
        return ResponseEntity.created(new URI("/api/pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pedidos} : Updates an existing pedido.
     *
     * @param pedido the pedido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pedido,
     * or with status {@code 400 (Bad Request)} if the pedido is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pedido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pedidos")
    public ResponseEntity<Pedido> updatePedido(@Valid @RequestBody Pedido pedido) throws URISyntaxException {
        log.debug("REST request to update Pedido : {}", pedido);
        if (pedido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Pedido result = pedidoService.save(pedido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pedido.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pedidos} : get all the pedidos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pedidos in body.
     */
    @GetMapping("/pedidos")
    public ResponseEntity<List<Pedido>> getAllPedidos(PedidoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Pedidos by criteria: {}", criteria);
        Page<Pedido> page = pedidoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /pedidos/count} : count all the pedidos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/pedidos/count")
    public ResponseEntity<Long> countPedidos(PedidoCriteria criteria) {
        log.debug("REST request to count Pedidos by criteria: {}", criteria);
        return ResponseEntity.ok().body(pedidoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pedidos/:id} : get the "id" pedido.
     *
     * @param id the id of the pedido to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pedido, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<Pedido> getPedido(@PathVariable Long id) {
        log.debug("REST request to get Pedido : {}", id);
        Optional<Pedido> pedido = pedidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pedido);
    }

    /**
     * {@code DELETE  /pedidos/:id} : delete the "id" pedido.
     *
     * @param id the id of the pedido to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> deletePedido(@PathVariable Long id) {
        log.debug("REST request to delete Pedido : {}", id);
        pedidoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
