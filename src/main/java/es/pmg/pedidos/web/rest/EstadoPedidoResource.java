package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.domain.EstadoPedido;
import es.pmg.pedidos.service.EstadoPedidoService;
import es.pmg.pedidos.web.rest.errors.BadRequestAlertException;
import es.pmg.pedidos.service.dto.EstadoPedidoCriteria;
import es.pmg.pedidos.service.EstadoPedidoQueryService;

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
 * REST controller for managing {@link es.pmg.pedidos.domain.EstadoPedido}.
 */
@RestController
@RequestMapping("/api")
public class EstadoPedidoResource {

    private final Logger log = LoggerFactory.getLogger(EstadoPedidoResource.class);

    private static final String ENTITY_NAME = "estadoPedido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EstadoPedidoService estadoPedidoService;

    private final EstadoPedidoQueryService estadoPedidoQueryService;

    public EstadoPedidoResource(EstadoPedidoService estadoPedidoService, EstadoPedidoQueryService estadoPedidoQueryService) {
        this.estadoPedidoService = estadoPedidoService;
        this.estadoPedidoQueryService = estadoPedidoQueryService;
    }

    /**
     * {@code POST  /estado-pedidos} : Create a new estadoPedido.
     *
     * @param estadoPedido the estadoPedido to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new estadoPedido, or with status {@code 400 (Bad Request)} if the estadoPedido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/estado-pedidos")
    public ResponseEntity<EstadoPedido> createEstadoPedido(@Valid @RequestBody EstadoPedido estadoPedido) throws URISyntaxException {
        log.debug("REST request to save EstadoPedido : {}", estadoPedido);
        if (estadoPedido.getId() != null) {
            throw new BadRequestAlertException("A new estadoPedido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EstadoPedido result = estadoPedidoService.save(estadoPedido);
        return ResponseEntity.created(new URI("/api/estado-pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /estado-pedidos} : Updates an existing estadoPedido.
     *
     * @param estadoPedido the estadoPedido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated estadoPedido,
     * or with status {@code 400 (Bad Request)} if the estadoPedido is not valid,
     * or with status {@code 500 (Internal Server Error)} if the estadoPedido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/estado-pedidos")
    public ResponseEntity<EstadoPedido> updateEstadoPedido(@Valid @RequestBody EstadoPedido estadoPedido) throws URISyntaxException {
        log.debug("REST request to update EstadoPedido : {}", estadoPedido);
        if (estadoPedido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EstadoPedido result = estadoPedidoService.save(estadoPedido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, estadoPedido.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /estado-pedidos} : get all the estadoPedidos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of estadoPedidos in body.
     */
    @GetMapping("/estado-pedidos")
    public ResponseEntity<List<EstadoPedido>> getAllEstadoPedidos(EstadoPedidoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EstadoPedidos by criteria: {}", criteria);
        Page<EstadoPedido> page = estadoPedidoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /estado-pedidos/count} : count all the estadoPedidos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/estado-pedidos/count")
    public ResponseEntity<Long> countEstadoPedidos(EstadoPedidoCriteria criteria) {
        log.debug("REST request to count EstadoPedidos by criteria: {}", criteria);
        return ResponseEntity.ok().body(estadoPedidoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /estado-pedidos/:id} : get the "id" estadoPedido.
     *
     * @param id the id of the estadoPedido to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the estadoPedido, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/estado-pedidos/{id}")
    public ResponseEntity<EstadoPedido> getEstadoPedido(@PathVariable Long id) {
        log.debug("REST request to get EstadoPedido : {}", id);
        Optional<EstadoPedido> estadoPedido = estadoPedidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(estadoPedido);
    }

    /**
     * {@code DELETE  /estado-pedidos/:id} : delete the "id" estadoPedido.
     *
     * @param id the id of the estadoPedido to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/estado-pedidos/{id}")
    public ResponseEntity<Void> deleteEstadoPedido(@PathVariable Long id) {
        log.debug("REST request to delete EstadoPedido : {}", id);
        estadoPedidoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
