package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.domain.FormaPago;
import es.pmg.pedidos.service.FormaPagoService;
import es.pmg.pedidos.web.rest.errors.BadRequestAlertException;
import es.pmg.pedidos.service.dto.FormaPagoCriteria;
import es.pmg.pedidos.service.FormaPagoQueryService;

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
 * REST controller for managing {@link es.pmg.pedidos.domain.FormaPago}.
 */
@RestController
@RequestMapping("/api")
public class FormaPagoResource {

    private final Logger log = LoggerFactory.getLogger(FormaPagoResource.class);

    private static final String ENTITY_NAME = "formaPago";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormaPagoService formaPagoService;

    private final FormaPagoQueryService formaPagoQueryService;

    public FormaPagoResource(FormaPagoService formaPagoService, FormaPagoQueryService formaPagoQueryService) {
        this.formaPagoService = formaPagoService;
        this.formaPagoQueryService = formaPagoQueryService;
    }

    /**
     * {@code POST  /forma-pagos} : Create a new formaPago.
     *
     * @param formaPago the formaPago to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formaPago, or with status {@code 400 (Bad Request)} if the formaPago has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/forma-pagos")
    public ResponseEntity<FormaPago> createFormaPago(@Valid @RequestBody FormaPago formaPago) throws URISyntaxException {
        log.debug("REST request to save FormaPago : {}", formaPago);
        if (formaPago.getId() != null) {
            throw new BadRequestAlertException("A new formaPago cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FormaPago result = formaPagoService.save(formaPago);
        return ResponseEntity.created(new URI("/api/forma-pagos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /forma-pagos} : Updates an existing formaPago.
     *
     * @param formaPago the formaPago to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formaPago,
     * or with status {@code 400 (Bad Request)} if the formaPago is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formaPago couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/forma-pagos")
    public ResponseEntity<FormaPago> updateFormaPago(@Valid @RequestBody FormaPago formaPago) throws URISyntaxException {
        log.debug("REST request to update FormaPago : {}", formaPago);
        if (formaPago.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FormaPago result = formaPagoService.save(formaPago);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, formaPago.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /forma-pagos} : get all the formaPagos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formaPagos in body.
     */
    @GetMapping("/forma-pagos")
    public ResponseEntity<List<FormaPago>> getAllFormaPagos(FormaPagoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FormaPagos by criteria: {}", criteria);
        Page<FormaPago> page = formaPagoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /forma-pagos/count} : count all the formaPagos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/forma-pagos/count")
    public ResponseEntity<Long> countFormaPagos(FormaPagoCriteria criteria) {
        log.debug("REST request to count FormaPagos by criteria: {}", criteria);
        return ResponseEntity.ok().body(formaPagoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /forma-pagos/:id} : get the "id" formaPago.
     *
     * @param id the id of the formaPago to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formaPago, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/forma-pagos/{id}")
    public ResponseEntity<FormaPago> getFormaPago(@PathVariable Long id) {
        log.debug("REST request to get FormaPago : {}", id);
        Optional<FormaPago> formaPago = formaPagoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formaPago);
    }

    /**
     * {@code DELETE  /forma-pagos/:id} : delete the "id" formaPago.
     *
     * @param id the id of the formaPago to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/forma-pagos/{id}")
    public ResponseEntity<Void> deleteFormaPago(@PathVariable Long id) {
        log.debug("REST request to delete FormaPago : {}", id);
        formaPagoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
