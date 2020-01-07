package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.PedidosApp;
import es.pmg.pedidos.domain.Direccion;
import es.pmg.pedidos.domain.Cliente;
import es.pmg.pedidos.domain.Localidad;
import es.pmg.pedidos.repository.DireccionRepository;
import es.pmg.pedidos.service.DireccionService;
import es.pmg.pedidos.web.rest.errors.ExceptionTranslator;
import es.pmg.pedidos.service.dto.DireccionCriteria;
import es.pmg.pedidos.service.DireccionQueryService;

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
 * Integration tests for the {@link DireccionResource} REST controller.
 */
@SpringBootTest(classes = PedidosApp.class)
public class DireccionResourceIT {

    private static final String DEFAULT_NOMBRE_VIA = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_VIA = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final Integer SMALLER_NUMERO = 1 - 1;

    private static final String DEFAULT_PISO = "AAAAAAAAAA";
    private static final String UPDATED_PISO = "BBBBBBBBBB";

    private static final String DEFAULT_BLOQUE = "AAAAAAAAAA";
    private static final String UPDATED_BLOQUE = "BBBBBBBBBB";

    private static final String DEFAULT_PUERTA = "AAAAAAAAAA";
    private static final String UPDATED_PUERTA = "BBBBBBBBBB";

    private static final String DEFAULT_ESCALERA = "AAAAAAAAAA";
    private static final String UPDATED_ESCALERA = "BBBBBBBBBB";

    @Autowired
    private DireccionRepository direccionRepository;

    @Autowired
    private DireccionService direccionService;

    @Autowired
    private DireccionQueryService direccionQueryService;

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

    private MockMvc restDireccionMockMvc;

    private Direccion direccion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DireccionResource direccionResource = new DireccionResource(direccionService, direccionQueryService);
        this.restDireccionMockMvc = MockMvcBuilders.standaloneSetup(direccionResource)
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
    public static Direccion createEntity(EntityManager em) {
        Direccion direccion = new Direccion()
            .nombreVia(DEFAULT_NOMBRE_VIA)
            .numero(DEFAULT_NUMERO)
            .piso(DEFAULT_PISO)
            .bloque(DEFAULT_BLOQUE)
            .puerta(DEFAULT_PUERTA)
            .escalera(DEFAULT_ESCALERA);
        return direccion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Direccion createUpdatedEntity(EntityManager em) {
        Direccion direccion = new Direccion()
            .nombreVia(UPDATED_NOMBRE_VIA)
            .numero(UPDATED_NUMERO)
            .piso(UPDATED_PISO)
            .bloque(UPDATED_BLOQUE)
            .puerta(UPDATED_PUERTA)
            .escalera(UPDATED_ESCALERA);
        return direccion;
    }

    @BeforeEach
    public void initTest() {
        direccion = createEntity(em);
    }

    @Test
    @Transactional
    public void createDireccion() throws Exception {
        int databaseSizeBeforeCreate = direccionRepository.findAll().size();

        // Create the Direccion
        restDireccionMockMvc.perform(post("/api/direccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direccion)))
            .andExpect(status().isCreated());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeCreate + 1);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getNombreVia()).isEqualTo(DEFAULT_NOMBRE_VIA);
        assertThat(testDireccion.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testDireccion.getPiso()).isEqualTo(DEFAULT_PISO);
        assertThat(testDireccion.getBloque()).isEqualTo(DEFAULT_BLOQUE);
        assertThat(testDireccion.getPuerta()).isEqualTo(DEFAULT_PUERTA);
        assertThat(testDireccion.getEscalera()).isEqualTo(DEFAULT_ESCALERA);
    }

    @Test
    @Transactional
    public void createDireccionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = direccionRepository.findAll().size();

        // Create the Direccion with an existing ID
        direccion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDireccionMockMvc.perform(post("/api/direccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direccion)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNombreViaIsRequired() throws Exception {
        int databaseSizeBeforeTest = direccionRepository.findAll().size();
        // set the field null
        direccion.setNombreVia(null);

        // Create the Direccion, which fails.

        restDireccionMockMvc.perform(post("/api/direccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direccion)))
            .andExpect(status().isBadRequest());

        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDireccions() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList
        restDireccionMockMvc.perform(get("/api/direccions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreVia").value(hasItem(DEFAULT_NOMBRE_VIA)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].piso").value(hasItem(DEFAULT_PISO)))
            .andExpect(jsonPath("$.[*].bloque").value(hasItem(DEFAULT_BLOQUE)))
            .andExpect(jsonPath("$.[*].puerta").value(hasItem(DEFAULT_PUERTA)))
            .andExpect(jsonPath("$.[*].escalera").value(hasItem(DEFAULT_ESCALERA)));
    }
    
    @Test
    @Transactional
    public void getDireccion() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get the direccion
        restDireccionMockMvc.perform(get("/api/direccions/{id}", direccion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(direccion.getId().intValue()))
            .andExpect(jsonPath("$.nombreVia").value(DEFAULT_NOMBRE_VIA))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.piso").value(DEFAULT_PISO))
            .andExpect(jsonPath("$.bloque").value(DEFAULT_BLOQUE))
            .andExpect(jsonPath("$.puerta").value(DEFAULT_PUERTA))
            .andExpect(jsonPath("$.escalera").value(DEFAULT_ESCALERA));
    }


    @Test
    @Transactional
    public void getDireccionsByIdFiltering() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        Long id = direccion.getId();

        defaultDireccionShouldBeFound("id.equals=" + id);
        defaultDireccionShouldNotBeFound("id.notEquals=" + id);

        defaultDireccionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDireccionShouldNotBeFound("id.greaterThan=" + id);

        defaultDireccionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDireccionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDireccionsByNombreViaIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where nombreVia equals to DEFAULT_NOMBRE_VIA
        defaultDireccionShouldBeFound("nombreVia.equals=" + DEFAULT_NOMBRE_VIA);

        // Get all the direccionList where nombreVia equals to UPDATED_NOMBRE_VIA
        defaultDireccionShouldNotBeFound("nombreVia.equals=" + UPDATED_NOMBRE_VIA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNombreViaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where nombreVia not equals to DEFAULT_NOMBRE_VIA
        defaultDireccionShouldNotBeFound("nombreVia.notEquals=" + DEFAULT_NOMBRE_VIA);

        // Get all the direccionList where nombreVia not equals to UPDATED_NOMBRE_VIA
        defaultDireccionShouldBeFound("nombreVia.notEquals=" + UPDATED_NOMBRE_VIA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNombreViaIsInShouldWork() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where nombreVia in DEFAULT_NOMBRE_VIA or UPDATED_NOMBRE_VIA
        defaultDireccionShouldBeFound("nombreVia.in=" + DEFAULT_NOMBRE_VIA + "," + UPDATED_NOMBRE_VIA);

        // Get all the direccionList where nombreVia equals to UPDATED_NOMBRE_VIA
        defaultDireccionShouldNotBeFound("nombreVia.in=" + UPDATED_NOMBRE_VIA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNombreViaIsNullOrNotNull() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where nombreVia is not null
        defaultDireccionShouldBeFound("nombreVia.specified=true");

        // Get all the direccionList where nombreVia is null
        defaultDireccionShouldNotBeFound("nombreVia.specified=false");
    }
                @Test
    @Transactional
    public void getAllDireccionsByNombreViaContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where nombreVia contains DEFAULT_NOMBRE_VIA
        defaultDireccionShouldBeFound("nombreVia.contains=" + DEFAULT_NOMBRE_VIA);

        // Get all the direccionList where nombreVia contains UPDATED_NOMBRE_VIA
        defaultDireccionShouldNotBeFound("nombreVia.contains=" + UPDATED_NOMBRE_VIA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNombreViaNotContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where nombreVia does not contain DEFAULT_NOMBRE_VIA
        defaultDireccionShouldNotBeFound("nombreVia.doesNotContain=" + DEFAULT_NOMBRE_VIA);

        // Get all the direccionList where nombreVia does not contain UPDATED_NOMBRE_VIA
        defaultDireccionShouldBeFound("nombreVia.doesNotContain=" + UPDATED_NOMBRE_VIA);
    }


    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero equals to DEFAULT_NUMERO
        defaultDireccionShouldBeFound("numero.equals=" + DEFAULT_NUMERO);

        // Get all the direccionList where numero equals to UPDATED_NUMERO
        defaultDireccionShouldNotBeFound("numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsNotEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero not equals to DEFAULT_NUMERO
        defaultDireccionShouldNotBeFound("numero.notEquals=" + DEFAULT_NUMERO);

        // Get all the direccionList where numero not equals to UPDATED_NUMERO
        defaultDireccionShouldBeFound("numero.notEquals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero in DEFAULT_NUMERO or UPDATED_NUMERO
        defaultDireccionShouldBeFound("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO);

        // Get all the direccionList where numero equals to UPDATED_NUMERO
        defaultDireccionShouldNotBeFound("numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero is not null
        defaultDireccionShouldBeFound("numero.specified=true");

        // Get all the direccionList where numero is null
        defaultDireccionShouldNotBeFound("numero.specified=false");
    }

    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero is greater than or equal to DEFAULT_NUMERO
        defaultDireccionShouldBeFound("numero.greaterThanOrEqual=" + DEFAULT_NUMERO);

        // Get all the direccionList where numero is greater than or equal to UPDATED_NUMERO
        defaultDireccionShouldNotBeFound("numero.greaterThanOrEqual=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero is less than or equal to DEFAULT_NUMERO
        defaultDireccionShouldBeFound("numero.lessThanOrEqual=" + DEFAULT_NUMERO);

        // Get all the direccionList where numero is less than or equal to SMALLER_NUMERO
        defaultDireccionShouldNotBeFound("numero.lessThanOrEqual=" + SMALLER_NUMERO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsLessThanSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero is less than DEFAULT_NUMERO
        defaultDireccionShouldNotBeFound("numero.lessThan=" + DEFAULT_NUMERO);

        // Get all the direccionList where numero is less than UPDATED_NUMERO
        defaultDireccionShouldBeFound("numero.lessThan=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByNumeroIsGreaterThanSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where numero is greater than DEFAULT_NUMERO
        defaultDireccionShouldNotBeFound("numero.greaterThan=" + DEFAULT_NUMERO);

        // Get all the direccionList where numero is greater than SMALLER_NUMERO
        defaultDireccionShouldBeFound("numero.greaterThan=" + SMALLER_NUMERO);
    }


    @Test
    @Transactional
    public void getAllDireccionsByPisoIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where piso equals to DEFAULT_PISO
        defaultDireccionShouldBeFound("piso.equals=" + DEFAULT_PISO);

        // Get all the direccionList where piso equals to UPDATED_PISO
        defaultDireccionShouldNotBeFound("piso.equals=" + UPDATED_PISO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPisoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where piso not equals to DEFAULT_PISO
        defaultDireccionShouldNotBeFound("piso.notEquals=" + DEFAULT_PISO);

        // Get all the direccionList where piso not equals to UPDATED_PISO
        defaultDireccionShouldBeFound("piso.notEquals=" + UPDATED_PISO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPisoIsInShouldWork() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where piso in DEFAULT_PISO or UPDATED_PISO
        defaultDireccionShouldBeFound("piso.in=" + DEFAULT_PISO + "," + UPDATED_PISO);

        // Get all the direccionList where piso equals to UPDATED_PISO
        defaultDireccionShouldNotBeFound("piso.in=" + UPDATED_PISO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPisoIsNullOrNotNull() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where piso is not null
        defaultDireccionShouldBeFound("piso.specified=true");

        // Get all the direccionList where piso is null
        defaultDireccionShouldNotBeFound("piso.specified=false");
    }
                @Test
    @Transactional
    public void getAllDireccionsByPisoContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where piso contains DEFAULT_PISO
        defaultDireccionShouldBeFound("piso.contains=" + DEFAULT_PISO);

        // Get all the direccionList where piso contains UPDATED_PISO
        defaultDireccionShouldNotBeFound("piso.contains=" + UPDATED_PISO);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPisoNotContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where piso does not contain DEFAULT_PISO
        defaultDireccionShouldNotBeFound("piso.doesNotContain=" + DEFAULT_PISO);

        // Get all the direccionList where piso does not contain UPDATED_PISO
        defaultDireccionShouldBeFound("piso.doesNotContain=" + UPDATED_PISO);
    }


    @Test
    @Transactional
    public void getAllDireccionsByBloqueIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where bloque equals to DEFAULT_BLOQUE
        defaultDireccionShouldBeFound("bloque.equals=" + DEFAULT_BLOQUE);

        // Get all the direccionList where bloque equals to UPDATED_BLOQUE
        defaultDireccionShouldNotBeFound("bloque.equals=" + UPDATED_BLOQUE);
    }

    @Test
    @Transactional
    public void getAllDireccionsByBloqueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where bloque not equals to DEFAULT_BLOQUE
        defaultDireccionShouldNotBeFound("bloque.notEquals=" + DEFAULT_BLOQUE);

        // Get all the direccionList where bloque not equals to UPDATED_BLOQUE
        defaultDireccionShouldBeFound("bloque.notEquals=" + UPDATED_BLOQUE);
    }

    @Test
    @Transactional
    public void getAllDireccionsByBloqueIsInShouldWork() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where bloque in DEFAULT_BLOQUE or UPDATED_BLOQUE
        defaultDireccionShouldBeFound("bloque.in=" + DEFAULT_BLOQUE + "," + UPDATED_BLOQUE);

        // Get all the direccionList where bloque equals to UPDATED_BLOQUE
        defaultDireccionShouldNotBeFound("bloque.in=" + UPDATED_BLOQUE);
    }

    @Test
    @Transactional
    public void getAllDireccionsByBloqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where bloque is not null
        defaultDireccionShouldBeFound("bloque.specified=true");

        // Get all the direccionList where bloque is null
        defaultDireccionShouldNotBeFound("bloque.specified=false");
    }
                @Test
    @Transactional
    public void getAllDireccionsByBloqueContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where bloque contains DEFAULT_BLOQUE
        defaultDireccionShouldBeFound("bloque.contains=" + DEFAULT_BLOQUE);

        // Get all the direccionList where bloque contains UPDATED_BLOQUE
        defaultDireccionShouldNotBeFound("bloque.contains=" + UPDATED_BLOQUE);
    }

    @Test
    @Transactional
    public void getAllDireccionsByBloqueNotContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where bloque does not contain DEFAULT_BLOQUE
        defaultDireccionShouldNotBeFound("bloque.doesNotContain=" + DEFAULT_BLOQUE);

        // Get all the direccionList where bloque does not contain UPDATED_BLOQUE
        defaultDireccionShouldBeFound("bloque.doesNotContain=" + UPDATED_BLOQUE);
    }


    @Test
    @Transactional
    public void getAllDireccionsByPuertaIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where puerta equals to DEFAULT_PUERTA
        defaultDireccionShouldBeFound("puerta.equals=" + DEFAULT_PUERTA);

        // Get all the direccionList where puerta equals to UPDATED_PUERTA
        defaultDireccionShouldNotBeFound("puerta.equals=" + UPDATED_PUERTA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPuertaIsNotEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where puerta not equals to DEFAULT_PUERTA
        defaultDireccionShouldNotBeFound("puerta.notEquals=" + DEFAULT_PUERTA);

        // Get all the direccionList where puerta not equals to UPDATED_PUERTA
        defaultDireccionShouldBeFound("puerta.notEquals=" + UPDATED_PUERTA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPuertaIsInShouldWork() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where puerta in DEFAULT_PUERTA or UPDATED_PUERTA
        defaultDireccionShouldBeFound("puerta.in=" + DEFAULT_PUERTA + "," + UPDATED_PUERTA);

        // Get all the direccionList where puerta equals to UPDATED_PUERTA
        defaultDireccionShouldNotBeFound("puerta.in=" + UPDATED_PUERTA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPuertaIsNullOrNotNull() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where puerta is not null
        defaultDireccionShouldBeFound("puerta.specified=true");

        // Get all the direccionList where puerta is null
        defaultDireccionShouldNotBeFound("puerta.specified=false");
    }
                @Test
    @Transactional
    public void getAllDireccionsByPuertaContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where puerta contains DEFAULT_PUERTA
        defaultDireccionShouldBeFound("puerta.contains=" + DEFAULT_PUERTA);

        // Get all the direccionList where puerta contains UPDATED_PUERTA
        defaultDireccionShouldNotBeFound("puerta.contains=" + UPDATED_PUERTA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByPuertaNotContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where puerta does not contain DEFAULT_PUERTA
        defaultDireccionShouldNotBeFound("puerta.doesNotContain=" + DEFAULT_PUERTA);

        // Get all the direccionList where puerta does not contain UPDATED_PUERTA
        defaultDireccionShouldBeFound("puerta.doesNotContain=" + UPDATED_PUERTA);
    }


    @Test
    @Transactional
    public void getAllDireccionsByEscaleraIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where escalera equals to DEFAULT_ESCALERA
        defaultDireccionShouldBeFound("escalera.equals=" + DEFAULT_ESCALERA);

        // Get all the direccionList where escalera equals to UPDATED_ESCALERA
        defaultDireccionShouldNotBeFound("escalera.equals=" + UPDATED_ESCALERA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByEscaleraIsNotEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where escalera not equals to DEFAULT_ESCALERA
        defaultDireccionShouldNotBeFound("escalera.notEquals=" + DEFAULT_ESCALERA);

        // Get all the direccionList where escalera not equals to UPDATED_ESCALERA
        defaultDireccionShouldBeFound("escalera.notEquals=" + UPDATED_ESCALERA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByEscaleraIsInShouldWork() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where escalera in DEFAULT_ESCALERA or UPDATED_ESCALERA
        defaultDireccionShouldBeFound("escalera.in=" + DEFAULT_ESCALERA + "," + UPDATED_ESCALERA);

        // Get all the direccionList where escalera equals to UPDATED_ESCALERA
        defaultDireccionShouldNotBeFound("escalera.in=" + UPDATED_ESCALERA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByEscaleraIsNullOrNotNull() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where escalera is not null
        defaultDireccionShouldBeFound("escalera.specified=true");

        // Get all the direccionList where escalera is null
        defaultDireccionShouldNotBeFound("escalera.specified=false");
    }
                @Test
    @Transactional
    public void getAllDireccionsByEscaleraContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where escalera contains DEFAULT_ESCALERA
        defaultDireccionShouldBeFound("escalera.contains=" + DEFAULT_ESCALERA);

        // Get all the direccionList where escalera contains UPDATED_ESCALERA
        defaultDireccionShouldNotBeFound("escalera.contains=" + UPDATED_ESCALERA);
    }

    @Test
    @Transactional
    public void getAllDireccionsByEscaleraNotContainsSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);

        // Get all the direccionList where escalera does not contain DEFAULT_ESCALERA
        defaultDireccionShouldNotBeFound("escalera.doesNotContain=" + DEFAULT_ESCALERA);

        // Get all the direccionList where escalera does not contain UPDATED_ESCALERA
        defaultDireccionShouldBeFound("escalera.doesNotContain=" + UPDATED_ESCALERA);
    }


    @Test
    @Transactional
    public void getAllDireccionsByUsuarioIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);
        Cliente usuario = ClienteResourceIT.createEntity(em);
        em.persist(usuario);
        em.flush();
        direccion.setUsuario(usuario);
        direccionRepository.saveAndFlush(direccion);
        Long usuarioId = usuario.getId();

        // Get all the direccionList where usuario equals to usuarioId
        defaultDireccionShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the direccionList where usuario equals to usuarioId + 1
        defaultDireccionShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }


    @Test
    @Transactional
    public void getAllDireccionsByLocalidadIsEqualToSomething() throws Exception {
        // Initialize the database
        direccionRepository.saveAndFlush(direccion);
        Localidad localidad = LocalidadResourceIT.createEntity(em);
        em.persist(localidad);
        em.flush();
        direccion.setLocalidad(localidad);
        direccionRepository.saveAndFlush(direccion);
        Long localidadId = localidad.getId();

        // Get all the direccionList where localidad equals to localidadId
        defaultDireccionShouldBeFound("localidadId.equals=" + localidadId);

        // Get all the direccionList where localidad equals to localidadId + 1
        defaultDireccionShouldNotBeFound("localidadId.equals=" + (localidadId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDireccionShouldBeFound(String filter) throws Exception {
        restDireccionMockMvc.perform(get("/api/direccions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direccion.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombreVia").value(hasItem(DEFAULT_NOMBRE_VIA)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].piso").value(hasItem(DEFAULT_PISO)))
            .andExpect(jsonPath("$.[*].bloque").value(hasItem(DEFAULT_BLOQUE)))
            .andExpect(jsonPath("$.[*].puerta").value(hasItem(DEFAULT_PUERTA)))
            .andExpect(jsonPath("$.[*].escalera").value(hasItem(DEFAULT_ESCALERA)));

        // Check, that the count call also returns 1
        restDireccionMockMvc.perform(get("/api/direccions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDireccionShouldNotBeFound(String filter) throws Exception {
        restDireccionMockMvc.perform(get("/api/direccions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDireccionMockMvc.perform(get("/api/direccions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDireccion() throws Exception {
        // Get the direccion
        restDireccionMockMvc.perform(get("/api/direccions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDireccion() throws Exception {
        // Initialize the database
        direccionService.save(direccion);

        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Update the direccion
        Direccion updatedDireccion = direccionRepository.findById(direccion.getId()).get();
        // Disconnect from session so that the updates on updatedDireccion are not directly saved in db
        em.detach(updatedDireccion);
        updatedDireccion
            .nombreVia(UPDATED_NOMBRE_VIA)
            .numero(UPDATED_NUMERO)
            .piso(UPDATED_PISO)
            .bloque(UPDATED_BLOQUE)
            .puerta(UPDATED_PUERTA)
            .escalera(UPDATED_ESCALERA);

        restDireccionMockMvc.perform(put("/api/direccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDireccion)))
            .andExpect(status().isOk());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
        Direccion testDireccion = direccionList.get(direccionList.size() - 1);
        assertThat(testDireccion.getNombreVia()).isEqualTo(UPDATED_NOMBRE_VIA);
        assertThat(testDireccion.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testDireccion.getPiso()).isEqualTo(UPDATED_PISO);
        assertThat(testDireccion.getBloque()).isEqualTo(UPDATED_BLOQUE);
        assertThat(testDireccion.getPuerta()).isEqualTo(UPDATED_PUERTA);
        assertThat(testDireccion.getEscalera()).isEqualTo(UPDATED_ESCALERA);
    }

    @Test
    @Transactional
    public void updateNonExistingDireccion() throws Exception {
        int databaseSizeBeforeUpdate = direccionRepository.findAll().size();

        // Create the Direccion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDireccionMockMvc.perform(put("/api/direccions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direccion)))
            .andExpect(status().isBadRequest());

        // Validate the Direccion in the database
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDireccion() throws Exception {
        // Initialize the database
        direccionService.save(direccion);

        int databaseSizeBeforeDelete = direccionRepository.findAll().size();

        // Delete the direccion
        restDireccionMockMvc.perform(delete("/api/direccions/{id}", direccion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Direccion> direccionList = direccionRepository.findAll();
        assertThat(direccionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
