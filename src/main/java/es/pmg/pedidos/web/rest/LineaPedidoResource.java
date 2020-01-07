package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.domain.LineaPedido;
import es.pmg.pedidos.service.LineaPedidoService;
import es.pmg.pedidos.web.rest.errors.BadRequestAlertException;
import es.pmg.pedidos.service.dto.LineaPedidoCriteria;
import es.pmg.pedidos.service.LineaPedidoQueryService;

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
 * REST controller for managing {@link es.pmg.pedidos.domain.LineaPedido}.
 */
@RestController
@RequestMapping("/api")
public class LineaPedidoResource {

    private final Logger log = LoggerFactory.getLogger(LineaPedidoResource.class);

    private static final String ENTITY_NAME = "lineaPedido";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LineaPedidoService lineaPedidoService;

    private final LineaPedidoQueryService lineaPedidoQueryService;

    public LineaPedidoResource(LineaPedidoService lineaPedidoService, LineaPedidoQueryService lineaPedidoQueryService) {
        this.lineaPedidoService = lineaPedidoService;
        this.lineaPedidoQueryService = lineaPedidoQueryService;
    }

    /**
     * {@code POST  /linea-pedidos} : Create a new lineaPedido.
     *
     * @param lineaPedido the lineaPedido to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lineaPedido, or with status {@code 400 (Bad Request)} if the lineaPedido has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/linea-pedidos")
    public ResponseEntity<LineaPedido> createLineaPedido(@Valid @RequestBody LineaPedido lineaPedido) throws URISyntaxException {
        log.debug("REST request to save LineaPedido : {}", lineaPedido);
        if (lineaPedido.getId() != null) {
            throw new BadRequestAlertException("A new lineaPedido cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LineaPedido result = lineaPedidoService.save(lineaPedido);
        return ResponseEntity.created(new URI("/api/linea-pedidos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /linea-pedidos} : Updates an existing lineaPedido.
     *
     * @param lineaPedido the lineaPedido to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lineaPedido,
     * or with status {@code 400 (Bad Request)} if the lineaPedido is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lineaPedido couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/linea-pedidos")
    public ResponseEntity<LineaPedido> updateLineaPedido(@Valid @RequestBody LineaPedido lineaPedido) throws URISyntaxException {
        log.debug("REST request to update LineaPedido : {}", lineaPedido);
        if (lineaPedido.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LineaPedido result = lineaPedidoService.save(lineaPedido);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lineaPedido.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /linea-pedidos} : get all the lineaPedidos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lineaPedidos in body.
     */
    @GetMapping("/linea-pedidos")
    public ResponseEntity<List<LineaPedido>> getAllLineaPedidos(LineaPedidoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LineaPedidos by criteria: {}", criteria);
        Page<LineaPedido> page = lineaPedidoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /linea-pedidos/count} : count all the lineaPedidos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/linea-pedidos/count")
    public ResponseEntity<Long> countLineaPedidos(LineaPedidoCriteria criteria) {
        log.debug("REST request to count LineaPedidos by criteria: {}", criteria);
        return ResponseEntity.ok().body(lineaPedidoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /linea-pedidos/:id} : get the "id" lineaPedido.
     *
     * @param id the id of the lineaPedido to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lineaPedido, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/linea-pedidos/{id}")
    public ResponseEntity<LineaPedido> getLineaPedido(@PathVariable Long id) {
        log.debug("REST request to get LineaPedido : {}", id);
        Optional<LineaPedido> lineaPedido = lineaPedidoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lineaPedido);
    }

    /**
     * {@code DELETE  /linea-pedidos/:id} : delete the "id" lineaPedido.
     *
     * @param id the id of the lineaPedido to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/linea-pedidos/{id}")
    public ResponseEntity<Void> deleteLineaPedido(@PathVariable Long id) {
        log.debug("REST request to delete LineaPedido : {}", id);
        lineaPedidoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
