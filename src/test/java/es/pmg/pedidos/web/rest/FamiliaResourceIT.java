package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.PedidosApp;
import es.pmg.pedidos.domain.Familia;
import es.pmg.pedidos.repository.FamiliaRepository;
import es.pmg.pedidos.service.FamiliaService;
import es.pmg.pedidos.web.rest.errors.ExceptionTranslator;
import es.pmg.pedidos.service.dto.FamiliaCriteria;
import es.pmg.pedidos.service.FamiliaQueryService;

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
 * Integration tests for the {@link FamiliaResource} REST controller.
 */
@SpringBootTest(classes = PedidosApp.class)
public class FamiliaResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private FamiliaService familiaService;

    @Autowired
    private FamiliaQueryService familiaQueryService;

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

    private MockMvc restFamiliaMockMvc;

    private Familia familia;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FamiliaResource familiaResource = new FamiliaResource(familiaService, familiaQueryService);
        this.restFamiliaMockMvc = MockMvcBuilders.standaloneSetup(familiaResource)
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
    public static Familia createEntity(EntityManager em) {
        Familia familia = new Familia()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION);
        return familia;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Familia createUpdatedEntity(EntityManager em) {
        Familia familia = new Familia()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);
        return familia;
    }

    @BeforeEach
    public void initTest() {
        familia = createEntity(em);
    }

    @Test
    @Transactional
    public void createFamilia() throws Exception {
        int databaseSizeBeforeCreate = familiaRepository.findAll().size();

        // Create the Familia
        restFamiliaMockMvc.perform(post("/api/familias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isCreated());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeCreate + 1);
        Familia testFamilia = familiaList.get(familiaList.size() - 1);
        assertThat(testFamilia.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testFamilia.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createFamiliaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = familiaRepository.findAll().size();

        // Create the Familia with an existing ID
        familia.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamiliaMockMvc.perform(post("/api/familias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isBadRequest());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiaRepository.findAll().size();
        // set the field null
        familia.setCodigo(null);

        // Create the Familia, which fails.

        restFamiliaMockMvc.perform(post("/api/familias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isBadRequest());

        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFamilias() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList
        restFamiliaMockMvc.perform(get("/api/familias?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familia.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getFamilia() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get the familia
        restFamiliaMockMvc.perform(get("/api/familias/{id}", familia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(familia.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }


    @Test
    @Transactional
    public void getFamiliasByIdFiltering() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        Long id = familia.getId();

        defaultFamiliaShouldBeFound("id.equals=" + id);
        defaultFamiliaShouldNotBeFound("id.notEquals=" + id);

        defaultFamiliaShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFamiliaShouldNotBeFound("id.greaterThan=" + id);

        defaultFamiliaShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFamiliaShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllFamiliasByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where codigo equals to DEFAULT_CODIGO
        defaultFamiliaShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the familiaList where codigo equals to UPDATED_CODIGO
        defaultFamiliaShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFamiliasByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where codigo not equals to DEFAULT_CODIGO
        defaultFamiliaShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the familiaList where codigo not equals to UPDATED_CODIGO
        defaultFamiliaShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFamiliasByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultFamiliaShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the familiaList where codigo equals to UPDATED_CODIGO
        defaultFamiliaShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFamiliasByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where codigo is not null
        defaultFamiliaShouldBeFound("codigo.specified=true");

        // Get all the familiaList where codigo is null
        defaultFamiliaShouldNotBeFound("codigo.specified=false");
    }
                @Test
    @Transactional
    public void getAllFamiliasByCodigoContainsSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where codigo contains DEFAULT_CODIGO
        defaultFamiliaShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the familiaList where codigo contains UPDATED_CODIGO
        defaultFamiliaShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllFamiliasByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where codigo does not contain DEFAULT_CODIGO
        defaultFamiliaShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the familiaList where codigo does not contain UPDATED_CODIGO
        defaultFamiliaShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }


    @Test
    @Transactional
    public void getAllFamiliasByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where descripcion equals to DEFAULT_DESCRIPCION
        defaultFamiliaShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the familiaList where descripcion equals to UPDATED_DESCRIPCION
        defaultFamiliaShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFamiliasByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultFamiliaShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the familiaList where descripcion not equals to UPDATED_DESCRIPCION
        defaultFamiliaShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFamiliasByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultFamiliaShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the familiaList where descripcion equals to UPDATED_DESCRIPCION
        defaultFamiliaShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFamiliasByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where descripcion is not null
        defaultFamiliaShouldBeFound("descripcion.specified=true");

        // Get all the familiaList where descripcion is null
        defaultFamiliaShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllFamiliasByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where descripcion contains DEFAULT_DESCRIPCION
        defaultFamiliaShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the familiaList where descripcion contains UPDATED_DESCRIPCION
        defaultFamiliaShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllFamiliasByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultFamiliaShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the familiaList where descripcion does not contain UPDATED_DESCRIPCION
        defaultFamiliaShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFamiliaShouldBeFound(String filter) throws Exception {
        restFamiliaMockMvc.perform(get("/api/familias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familia.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restFamiliaMockMvc.perform(get("/api/familias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFamiliaShouldNotBeFound(String filter) throws Exception {
        restFamiliaMockMvc.perform(get("/api/familias?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFamiliaMockMvc.perform(get("/api/familias/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFamilia() throws Exception {
        // Get the familia
        restFamiliaMockMvc.perform(get("/api/familias/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFamilia() throws Exception {
        // Initialize the database
        familiaService.save(familia);

        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();

        // Update the familia
        Familia updatedFamilia = familiaRepository.findById(familia.getId()).get();
        // Disconnect from session so that the updates on updatedFamilia are not directly saved in db
        em.detach(updatedFamilia);
        updatedFamilia
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);

        restFamiliaMockMvc.perform(put("/api/familias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFamilia)))
            .andExpect(status().isOk());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
        Familia testFamilia = familiaList.get(familiaList.size() - 1);
        assertThat(testFamilia.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testFamilia.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingFamilia() throws Exception {
        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();

        // Create the Familia

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamiliaMockMvc.perform(put("/api/familias")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isBadRequest());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFamilia() throws Exception {
        // Initialize the database
        familiaService.save(familia);

        int databaseSizeBeforeDelete = familiaRepository.findAll().size();

        // Delete the familia
        restFamiliaMockMvc.perform(delete("/api/familias/{id}", familia.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
