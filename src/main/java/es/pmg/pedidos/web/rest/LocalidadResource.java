package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.domain.Localidad;
import es.pmg.pedidos.service.LocalidadService;
import es.pmg.pedidos.web.rest.errors.BadRequestAlertException;
import es.pmg.pedidos.service.dto.LocalidadCriteria;
import es.pmg.pedidos.service.LocalidadQueryService;

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
 * REST controller for managing {@link es.pmg.pedidos.domain.Localidad}.
 */
@RestController
@RequestMapping("/api")
public class LocalidadResource {

    private final Logger log = LoggerFactory.getLogger(LocalidadResource.class);

    private static final String ENTITY_NAME = "localidad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalidadService localidadService;

    private final LocalidadQueryService localidadQueryService;

    public LocalidadResource(LocalidadService localidadService, LocalidadQueryService localidadQueryService) {
        this.localidadService = localidadService;
        this.localidadQueryService = localidadQueryService;
    }

    /**
     * {@code POST  /localidads} : Create a new localidad.
     *
     * @param localidad the localidad to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localidad, or with status {@code 400 (Bad Request)} if the localidad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/localidads")
    public ResponseEntity<Localidad> createLocalidad(@Valid @RequestBody Localidad localidad) throws URISyntaxException {
        log.debug("REST request to save Localidad : {}", localidad);
        if (localidad.getId() != null) {
            throw new BadRequestAlertException("A new localidad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Localidad result = localidadService.save(localidad);
        return ResponseEntity.created(new URI("/api/localidads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /localidads} : Updates an existing localidad.
     *
     * @param localidad the localidad to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localidad,
     * or with status {@code 400 (Bad Request)} if the localidad is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localidad couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/localidads")
    public ResponseEntity<Localidad> updateLocalidad(@Valid @RequestBody Localidad localidad) throws URISyntaxException {
        log.debug("REST request to update Localidad : {}", localidad);
        if (localidad.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Localidad result = localidadService.save(localidad);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, localidad.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /localidads} : get all the localidads.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of localidads in body.
     */
    @GetMapping("/localidads")
    public ResponseEntity<List<Localidad>> getAllLocalidads(LocalidadCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Localidads by criteria: {}", criteria);
        Page<Localidad> page = localidadQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /localidads/count} : count all the localidads.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/localidads/count")
    public ResponseEntity<Long> countLocalidads(LocalidadCriteria criteria) {
        log.debug("REST request to count Localidads by criteria: {}", criteria);
        return ResponseEntity.ok().body(localidadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /localidads/:id} : get the "id" localidad.
     *
     * @param id the id of the localidad to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the localidad, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/localidads/{id}")
    public ResponseEntity<Localidad> getLocalidad(@PathVariable Long id) {
        log.debug("REST request to get Localidad : {}", id);
        Optional<Localidad> localidad = localidadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(localidad);
    }

    /**
     * {@code DELETE  /localidads/:id} : delete the "id" localidad.
     *
     * @param id the id of the localidad to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/localidads/{id}")
    public ResponseEntity<Void> deleteLocalidad(@PathVariable Long id) {
        log.debug("REST request to delete Localidad : {}", id);
        localidadService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
