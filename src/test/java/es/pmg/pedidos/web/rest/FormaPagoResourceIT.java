package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.PedidosApp;
import es.pmg.pedidos.domain.FormaPago;
import es.pmg.pedidos.repository.FormaPagoRepository;
import es.pmg.pedidos.service.FormaPagoService;
import es.pmg.pedidos.web.rest.errors.ExceptionTranslator;
import es.pmg.pedidos.service.dto.FormaPagoCriteria;
import es.pmg.pedidos.service.FormaPagoQueryService;

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
 * Integration tests for the {@link FormaPagoResource} REST controller.
 */
@SpringBootTest(classes = PedidosApp.class)
public class FormaPagoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private FormaPagoRepository formaPagoRepository;

    @Autowired
    private FormaPagoService formaPagoService;

    @Autowired
    private FormaPagoQueryService formaPagoQueryService;

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

    private MockMvc restFormaPagoMockMvc;

    private FormaPago formaPago;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FormaPagoResource formaPagoResource = new FormaPagoResource(formaPagoService, formaPagoQueryService);
        this.restFormaPagoMockMvc = MockMvcBuilders.standaloneSetup(formaPagoResource)
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
    public static FormaPago createEntity(EntityManager em) {
        FormaPago formaPago = new FormaPago()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION);
        return formaPago;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormaPago createUpdatedEntity(EntityManager em) {
        FormaPago formaPago = new FormaPago()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);
        return formaPago;
    }

    @BeforeEach
    public void initTest() {
        formaPago = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormaPago() throws Exception {
        int databaseSizeBeforeCreate = formaPagoRepository.findAll().size();

        // Create the FormaPago
        restFormaPagoMockMvc.perform(post("/api/forma-pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPago)))
            .andExpect(status().isCreated());

        // Validate the FormaPago in the database
        List<FormaPago> formaPagoList = formaPagoRepository.findAll();
        assertThat(formaPagoList).hasSize(databaseSizeBeforeCreate + 1);
        FormaPago testFormaPago = formaPagoList.get(formaPagoList.size() - 1);
        assertThat(testFormaPago.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testFormaPago.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createFormaPagoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = formaPagoRepository.findAll().size();

        // Create the FormaPago with an existing ID
        formaPago.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormaPagoMockMvc.perform(post("/api/forma-pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPago)))
            .andExpect(status().isBadRequest());

        // Validate the FormaPago in the database
        List<FormaPago> formaPagoList = formaPagoRepository.findAll();
        assertThat(formaPagoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaPagoRepository.findAll().size();
        // set the field null
        formaPago.setCodigo(null);

        // Create the FormaPago, which fails.

        restFormaPagoMockMvc.perform(post("/api/forma-pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPago)))
            .andExpect(status().isBadRequest());

        List<FormaPago> formaPagoList = formaPagoRepository.findAll();
        assertThat(formaPagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = formaPagoRepository.findAll().size();
        // set the field null
        formaPago.setDescripcion(null);

        // Create the FormaPago, which fails.

        restFormaPagoMockMvc.perform(post("/api/forma-pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPago)))
            .andExpect(status().isBadRequest());

        List<FormaPago> formaPagoList = formaPagoRepository.findAll();
        assertThat(formaPagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFormaPagos() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList
        restFormaPagoMockMvc.perform(get("/api/forma-pagos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaPago.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getFormaPago() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get the formaPago
        restFormaPagoMockMvc.perform(get("/api/forma-pagos/{id}", formaPago.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formaPago.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }


    @Test
    @Transactional
    public void getFormaPagosByIdFiltering() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        Long id = formaPago.getId();

        defaultFormaPagoShouldBeFound("id.equals=" + id);
        defaultFormaPagoShouldNotBeFound("id.notEquals=" + id);

        defaultFormaPagoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFormaPagoShouldNotBeFound("id.greaterThan=" + id);

        defaultFormaPagoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFormaPagoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFormaPagosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where codigo equals to DEFAULT_CODIGO
        defaultFormaPagoShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the formaPagoList where codigo equals to UPDATED_CODIGO
        defaultFormaPagoShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where codigo not equals to DEFAULT_CODIGO
        defaultFormaPagoShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the formaPagoList where codigo not equals to UPDATED_CODIGO
        defaultFormaPagoShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultFormaPagoShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the formaPagoList where codigo equals to UPDATED_CODIGO
        defaultFormaPagoShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where codigo is not null
        defaultFormaPagoShouldBeFound("codigo.specified=true");

        // Get all the formaPagoList where codigo is null
        defaultFormaPagoShouldNotBeFound("codigo.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormaPagosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where codigo contains DEFAULT_CODIGO
        defaultFormaPagoShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the formaPagoList where codigo contains UPDATED_CODIGO
        defaultFormaPagoShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where codigo does not contain DEFAULT_CODIGO
        defaultFormaPagoShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the formaPagoList where codigo does not contain UPDATED_CODIGO
        defaultFormaPagoShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }


    @Test
    @Transactional
    public void getAllFormaPagosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where descripcion equals to DEFAULT_DESCRIPCION
        defaultFormaPagoShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the formaPagoList where descripcion equals to UPDATED_DESCRIPCION
        defaultFormaPagoShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultFormaPagoShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the formaPagoList where descripcion not equals to UPDATED_DESCRIPCION
        defaultFormaPagoShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultFormaPagoShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the formaPagoList where descripcion equals to UPDATED_DESCRIPCION
        defaultFormaPagoShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where descripcion is not null
        defaultFormaPagoShouldBeFound("descripcion.specified=true");

        // Get all the formaPagoList where descripcion is null
        defaultFormaPagoShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllFormaPagosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where descripcion contains DEFAULT_DESCRIPCION
        defaultFormaPagoShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the formaPagoList where descripcion contains UPDATED_DESCRIPCION
        defaultFormaPagoShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFormaPagosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        formaPagoRepository.saveAndFlush(formaPago);

        // Get all the formaPagoList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultFormaPagoShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the formaPagoList where descripcion does not contain UPDATED_DESCRIPCION
        defaultFormaPagoShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFormaPagoShouldBeFound(String filter) throws Exception {
        restFormaPagoMockMvc.perform(get("/api/forma-pagos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formaPago.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restFormaPagoMockMvc.perform(get("/api/forma-pagos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFormaPagoShouldNotBeFound(String filter) throws Exception {
        restFormaPagoMockMvc.perform(get("/api/forma-pagos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFormaPagoMockMvc.perform(get("/api/forma-pagos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFormaPago() throws Exception {
        // Get the formaPago
        restFormaPagoMockMvc.perform(get("/api/forma-pagos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormaPago() throws Exception {
        // Initialize the database
        formaPagoService.save(formaPago);

        int databaseSizeBeforeUpdate = formaPagoRepository.findAll().size();

        // Update the formaPago
        FormaPago updatedFormaPago = formaPagoRepository.findById(formaPago.getId()).get();
        // Disconnect from session so that the updates on updatedFormaPago are not directly saved in db
        em.detach(updatedFormaPago);
        updatedFormaPago
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);

        restFormaPagoMockMvc.perform(put("/api/forma-pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFormaPago)))
            .andExpect(status().isOk());

        // Validate the FormaPago in the database
        List<FormaPago> formaPagoList = formaPagoRepository.findAll();
        assertThat(formaPagoList).hasSize(databaseSizeBeforeUpdate);
        FormaPago testFormaPago = formaPagoList.get(formaPagoList.size() - 1);
        assertThat(testFormaPago.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testFormaPago.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingFormaPago() throws Exception {
        int databaseSizeBeforeUpdate = formaPagoRepository.findAll().size();

        // Create the FormaPago

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormaPagoMockMvc.perform(put("/api/forma-pagos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(formaPago)))
            .andExpect(status().isBadRequest());

        // Validate the FormaPago in the database
        List<FormaPago> formaPagoList = formaPagoRepository.findAll();
        assertThat(formaPagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFormaPago() throws Exception {
        // Initialize the database
        formaPagoService.save(formaPago);

        int databaseSizeBeforeDelete = formaPagoRepository.findAll().size();

        // Delete the formaPago
        restFormaPagoMockMvc.perform(delete("/api/forma-pagos/{id}", formaPago.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormaPago> formaPagoList = formaPagoRepository.findAll();
        assertThat(formaPagoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
