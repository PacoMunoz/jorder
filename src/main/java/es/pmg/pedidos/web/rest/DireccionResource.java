package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.domain.Direccion;
import es.pmg.pedidos.service.DireccionService;
import es.pmg.pedidos.web.rest.errors.BadRequestAlertException;
import es.pmg.pedidos.service.dto.DireccionCriteria;
import es.pmg.pedidos.service.DireccionQueryService;

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
 * REST controller for managing {@link es.pmg.pedidos.domain.Direccion}.
 */
@RestController
@RequestMapping("/api")
public class DireccionResource {

    private final Logger log = LoggerFactory.getLogger(DireccionResource.class);

    private static final String ENTITY_NAME = "direccion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DireccionService direccionService;

    private final DireccionQueryService direccionQueryService;

    public DireccionResource(DireccionService direccionService, DireccionQueryService direccionQueryService) {
        this.direccionService = direccionService;
        this.direccionQueryService = direccionQueryService;
    }

    /**
     * {@code POST  /direccions} : Create a new direccion.
     *
     * @param direccion the direccion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new direccion, or with status {@code 400 (Bad Request)} if the direccion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/direccions")
    public ResponseEntity<Direccion> createDireccion(@Valid @RequestBody Direccion direccion) throws URISyntaxException {
        log.debug("REST request to save Direccion : {}", direccion);
        if (direccion.getId() != null) {
            throw new BadRequestAlertException("A new direccion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Direccion result = direccionService.save(direccion);
        return ResponseEntity.created(new URI("/api/direccions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /direccions} : Updates an existing direccion.
     *
     * @param direccion the direccion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated direccion,
     * or with status {@code 400 (Bad Request)} if the direccion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the direccion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/direccions")
    public ResponseEntity<Direccion> updateDireccion(@Valid @RequestBody Direccion direccion) throws URISyntaxException {
        log.debug("REST request to update Direccion : {}", direccion);
        if (direccion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Direccion result = direccionService.save(direccion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, direccion.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /direccions} : get all the direccions.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of direccions in body.
     */
    @GetMapping("/direccions")
    public ResponseEntity<List<Direccion>> getAllDireccions(DireccionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Direccions by criteria: {}", criteria);
        Page<Direccion> page = direccionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /direccions/count} : count all the direccions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/direccions/count")
    public ResponseEntity<Long> countDireccions(DireccionCriteria criteria) {
        log.debug("REST request to count Direccions by criteria: {}", criteria);
        return ResponseEntity.ok().body(direccionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /direccions/:id} : get the "id" direccion.
     *
     * @param id the id of the direccion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the direccion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/direccions/{id}")
    public ResponseEntity<Direccion> getDireccion(@PathVariable Long id) {
        log.debug("REST request to get Direccion : {}", id);
        Optional<Direccion> direccion = direccionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(direccion);
    }

    /**
     * {@code DELETE  /direccions/:id} : delete the "id" direccion.
     *
     * @param id the id of the direccion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/direccions/{id}")
    public ResponseEntity<Void> deleteDireccion(@PathVariable Long id) {
        log.debug("REST request to delete Direccion : {}", id);
        direccionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
