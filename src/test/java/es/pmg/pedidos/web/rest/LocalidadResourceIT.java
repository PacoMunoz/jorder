package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.PedidosApp;
import es.pmg.pedidos.domain.Localidad;
import es.pmg.pedidos.repository.LocalidadRepository;
import es.pmg.pedidos.service.LocalidadService;
import es.pmg.pedidos.web.rest.errors.ExceptionTranslator;
import es.pmg.pedidos.service.dto.LocalidadCriteria;
import es.pmg.pedidos.service.LocalidadQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static es.pmg.pedidos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link LocalidadResource} REST controller.
 */
@SpringBootTest(classes = PedidosApp.class)
public class LocalidadResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODIGO_POSTAL = 1;
    private static final Integer UPDATED_CODIGO_POSTAL = 2;
    private static final Integer SMALLER_CODIGO_POSTAL = 1 - 1;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private LocalidadService localidadService;

    @Autowired
    private LocalidadQueryService localidadQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restLocalidadMockMvc;

    private Localidad localidad;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LocalidadResource localidadResource = new LocalidadResource(localidadService, localidadQueryService);
        this.restLocalidadMockMvc = MockMvcBuilders.standaloneSetup(localidadResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localidad createEntity(EntityManager em) {
        Localidad localidad = new Localidad()
            .codigo(DEFAULT_CODIGO)
            .nombre(DEFAULT_NOMBRE)
            .codigoPostal(DEFAULT_CODIGO_POSTAL);
        return localidad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Localidad createUpdatedEntity(EntityManager em) {
        Localidad localidad = new Localidad()
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .codigoPostal(UPDATED_CODIGO_POSTAL);
        return localidad;
    }

    @BeforeEach
    public void initTest() {
        localidad = createEntity(em);
    }

    @Test
    @Transactional
    public void createLocalidad() throws Exception {
        int databaseSizeBeforeCreate = localidadRepository.findAll().size();

        // Create the Localidad
        restLocalidadMockMvc.perform(post("/api/localidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidad)))
            .andExpect(status().isCreated());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeCreate + 1);
        Localidad testLocalidad = localidadList.get(localidadList.size() - 1);
        assertThat(testLocalidad.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testLocalidad.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testLocalidad.getCodigoPostal()).isEqualTo(DEFAULT_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void createLocalidadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = localidadRepository.findAll().size();

        // Create the Localidad with an existing ID
        localidad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalidadMockMvc.perform(post("/api/localidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidad)))
            .andExpect(status().isBadRequest());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadRepository.findAll().size();
        // set the field null
        localidad.setCodigo(null);

        // Create the Localidad, which fails.

        restLocalidadMockMvc.perform(post("/api/localidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidad)))
            .andExpect(status().isBadRequest());

        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadRepository.findAll().size();
        // set the field null
        localidad.setNombre(null);

        // Create the Localidad, which fails.

        restLocalidadMockMvc.perform(post("/api/localidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidad)))
            .andExpect(status().isBadRequest());

        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodigoPostalIsRequired() throws Exception {
        int databaseSizeBeforeTest = localidadRepository.findAll().size();
        // set the field null
        localidad.setCodigoPostal(null);

        // Create the Localidad, which fails.

        restLocalidadMockMvc.perform(post("/api/localidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidad)))
            .andExpect(status().isBadRequest());

        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocalidads() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList
        restLocalidadMockMvc.perform(get("/api/localidads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)));
    }
    
    @Test
    @Transactional
    public void getLocalidad() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get the localidad
        restLocalidadMockMvc.perform(get("/api/localidads/{id}", localidad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(localidad.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.codigoPostal").value(DEFAULT_CODIGO_POSTAL));
    }


    @Test
    @Transactional
    public void getLocalidadsByIdFiltering() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        Long id = localidad.getId();

        defaultLocalidadShouldBeFound("id.equals=" + id);
        defaultLocalidadShouldNotBeFound("id.notEquals=" + id);

        defaultLocalidadShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLocalidadShouldNotBeFound("id.greaterThan=" + id);

        defaultLocalidadShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLocalidadShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLocalidadsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigo equals to DEFAULT_CODIGO
        defaultLocalidadShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the localidadList where codigo equals to UPDATED_CODIGO
        defaultLocalidadShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigo not equals to DEFAULT_CODIGO
        defaultLocalidadShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the localidadList where codigo not equals to UPDATED_CODIGO
        defaultLocalidadShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultLocalidadShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the localidadList where codigo equals to UPDATED_CODIGO
        defaultLocalidadShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigo is not null
        defaultLocalidadShouldBeFound("codigo.specified=true");

        // Get all the localidadList where codigo is null
        defaultLocalidadShouldNotBeFound("codigo.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocalidadsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigo contains DEFAULT_CODIGO
        defaultLocalidadShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the localidadList where codigo contains UPDATED_CODIGO
        defaultLocalidadShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigo does not contain DEFAULT_CODIGO
        defaultLocalidadShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the localidadList where codigo does not contain UPDATED_CODIGO
        defaultLocalidadShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }


    @Test
    @Transactional
    public void getAllLocalidadsByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where nombre equals to DEFAULT_NOMBRE
        defaultLocalidadShouldBeFound("nombre.equals=" + DEFAULT_NOMBRE);

        // Get all the localidadList where nombre equals to UPDATED_NOMBRE
        defaultLocalidadShouldNotBeFound("nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByNombreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where nombre not equals to DEFAULT_NOMBRE
        defaultLocalidadShouldNotBeFound("nombre.notEquals=" + DEFAULT_NOMBRE);

        // Get all the localidadList where nombre not equals to UPDATED_NOMBRE
        defaultLocalidadShouldBeFound("nombre.notEquals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where nombre in DEFAULT_NOMBRE or UPDATED_NOMBRE
        defaultLocalidadShouldBeFound("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE);

        // Get all the localidadList where nombre equals to UPDATED_NOMBRE
        defaultLocalidadShouldNotBeFound("nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where nombre is not null
        defaultLocalidadShouldBeFound("nombre.specified=true");

        // Get all the localidadList where nombre is null
        defaultLocalidadShouldNotBeFound("nombre.specified=false");
    }
                @Test
    @Transactional
    public void getAllLocalidadsByNombreContainsSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where nombre contains DEFAULT_NOMBRE
        defaultLocalidadShouldBeFound("nombre.contains=" + DEFAULT_NOMBRE);

        // Get all the localidadList where nombre contains UPDATED_NOMBRE
        defaultLocalidadShouldNotBeFound("nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where nombre does not contain DEFAULT_NOMBRE
        defaultLocalidadShouldNotBeFound("nombre.doesNotContain=" + DEFAULT_NOMBRE);

        // Get all the localidadList where nombre does not contain UPDATED_NOMBRE
        defaultLocalidadShouldBeFound("nombre.doesNotContain=" + UPDATED_NOMBRE);
    }


    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal equals to DEFAULT_CODIGO_POSTAL
        defaultLocalidadShouldBeFound("codigoPostal.equals=" + DEFAULT_CODIGO_POSTAL);

        // Get all the localidadList where codigoPostal equals to UPDATED_CODIGO_POSTAL
        defaultLocalidadShouldNotBeFound("codigoPostal.equals=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal not equals to DEFAULT_CODIGO_POSTAL
        defaultLocalidadShouldNotBeFound("codigoPostal.notEquals=" + DEFAULT_CODIGO_POSTAL);

        // Get all the localidadList where codigoPostal not equals to UPDATED_CODIGO_POSTAL
        defaultLocalidadShouldBeFound("codigoPostal.notEquals=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsInShouldWork() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal in DEFAULT_CODIGO_POSTAL or UPDATED_CODIGO_POSTAL
        defaultLocalidadShouldBeFound("codigoPostal.in=" + DEFAULT_CODIGO_POSTAL + "," + UPDATED_CODIGO_POSTAL);

        // Get all the localidadList where codigoPostal equals to UPDATED_CODIGO_POSTAL
        defaultLocalidadShouldNotBeFound("codigoPostal.in=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsNullOrNotNull() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal is not null
        defaultLocalidadShouldBeFound("codigoPostal.specified=true");

        // Get all the localidadList where codigoPostal is null
        defaultLocalidadShouldNotBeFound("codigoPostal.specified=false");
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal is greater than or equal to DEFAULT_CODIGO_POSTAL
        defaultLocalidadShouldBeFound("codigoPostal.greaterThanOrEqual=" + DEFAULT_CODIGO_POSTAL);

        // Get all the localidadList where codigoPostal is greater than or equal to UPDATED_CODIGO_POSTAL
        defaultLocalidadShouldNotBeFound("codigoPostal.greaterThanOrEqual=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal is less than or equal to DEFAULT_CODIGO_POSTAL
        defaultLocalidadShouldBeFound("codigoPostal.lessThanOrEqual=" + DEFAULT_CODIGO_POSTAL);

        // Get all the localidadList where codigoPostal is less than or equal to SMALLER_CODIGO_POSTAL
        defaultLocalidadShouldNotBeFound("codigoPostal.lessThanOrEqual=" + SMALLER_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsLessThanSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal is less than DEFAULT_CODIGO_POSTAL
        defaultLocalidadShouldNotBeFound("codigoPostal.lessThan=" + DEFAULT_CODIGO_POSTAL);

        // Get all the localidadList where codigoPostal is less than UPDATED_CODIGO_POSTAL
        defaultLocalidadShouldBeFound("codigoPostal.lessThan=" + UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void getAllLocalidadsByCodigoPostalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        localidadRepository.saveAndFlush(localidad);

        // Get all the localidadList where codigoPostal is greater than DEFAULT_CODIGO_POSTAL
        defaultLocalidadShouldNotBeFound("codigoPostal.greaterThan=" + DEFAULT_CODIGO_POSTAL);

        // Get all the localidadList where codigoPostal is greater than SMALLER_CODIGO_POSTAL
        defaultLocalidadShouldBeFound("codigoPostal.greaterThan=" + SMALLER_CODIGO_POSTAL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLocalidadShouldBeFound(String filter) throws Exception {
        restLocalidadMockMvc.perform(get("/api/localidads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localidad.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)));

        // Check, that the count call also returns 1
        restLocalidadMockMvc.perform(get("/api/localidads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLocalidadShouldNotBeFound(String filter) throws Exception {
        restLocalidadMockMvc.perform(get("/api/localidads?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLocalidadMockMvc.perform(get("/api/localidads/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLocalidad() throws Exception {
        // Get the localidad
        restLocalidadMockMvc.perform(get("/api/localidads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocalidad() throws Exception {
        // Initialize the database
        localidadService.save(localidad);

        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();

        // Update the localidad
        Localidad updatedLocalidad = localidadRepository.findById(localidad.getId()).get();
        // Disconnect from session so that the updates on updatedLocalidad are not directly saved in db
        em.detach(updatedLocalidad);
        updatedLocalidad
            .codigo(UPDATED_CODIGO)
            .nombre(UPDATED_NOMBRE)
            .codigoPostal(UPDATED_CODIGO_POSTAL);

        restLocalidadMockMvc.perform(put("/api/localidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLocalidad)))
            .andExpect(status().isOk());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
        Localidad testLocalidad = localidadList.get(localidadList.size() - 1);
        assertThat(testLocalidad.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testLocalidad.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testLocalidad.getCodigoPostal()).isEqualTo(UPDATED_CODIGO_POSTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingLocalidad() throws Exception {
        int databaseSizeBeforeUpdate = localidadRepository.findAll().size();

        // Create the Localidad

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalidadMockMvc.perform(put("/api/localidads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(localidad)))
            .andExpect(status().isBadRequest());

        // Validate the Localidad in the database
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLocalidad() throws Exception {
        // Initialize the database
        localidadService.save(localidad);

        int databaseSizeBeforeDelete = localidadRepository.findAll().size();

        // Delete the localidad
        restLocalidadMockMvc.perform(delete("/api/localidads/{id}", localidad.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Localidad> localidadList = localidadRepository.findAll();
        assertThat(localidadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
