package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.PedidosApp;
import es.pmg.pedidos.domain.EstadoPedido;
import es.pmg.pedidos.repository.EstadoPedidoRepository;
import es.pmg.pedidos.service.EstadoPedidoService;
import es.pmg.pedidos.web.rest.errors.ExceptionTranslator;
import es.pmg.pedidos.service.dto.EstadoPedidoCriteria;
import es.pmg.pedidos.service.EstadoPedidoQueryService;

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
 * Integration tests for the {@link EstadoPedidoResource} REST controller.
 */
@SpringBootTest(classes = PedidosApp.class)
public class EstadoPedidoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    @Autowired
    private EstadoPedidoService estadoPedidoService;

    @Autowired
    private EstadoPedidoQueryService estadoPedidoQueryService;

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

    private MockMvc restEstadoPedidoMockMvc;

    private EstadoPedido estadoPedido;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EstadoPedidoResource estadoPedidoResource = new EstadoPedidoResource(estadoPedidoService, estadoPedidoQueryService);
        this.restEstadoPedidoMockMvc = MockMvcBuilders.standaloneSetup(estadoPedidoResource)
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
    public static EstadoPedido createEntity(EntityManager em) {
        EstadoPedido estadoPedido = new EstadoPedido()
            .codigo(DEFAULT_CODIGO)
            .descripcion(DEFAULT_DESCRIPCION);
        return estadoPedido;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EstadoPedido createUpdatedEntity(EntityManager em) {
        EstadoPedido estadoPedido = new EstadoPedido()
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);
        return estadoPedido;
    }

    @BeforeEach
    public void initTest() {
        estadoPedido = createEntity(em);
    }

    @Test
    @Transactional
    public void createEstadoPedido() throws Exception {
        int databaseSizeBeforeCreate = estadoPedidoRepository.findAll().size();

        // Create the EstadoPedido
        restEstadoPedidoMockMvc.perform(post("/api/estado-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estadoPedido)))
            .andExpect(status().isCreated());

        // Validate the EstadoPedido in the database
        List<EstadoPedido> estadoPedidoList = estadoPedidoRepository.findAll();
        assertThat(estadoPedidoList).hasSize(databaseSizeBeforeCreate + 1);
        EstadoPedido testEstadoPedido = estadoPedidoList.get(estadoPedidoList.size() - 1);
        assertThat(testEstadoPedido.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testEstadoPedido.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createEstadoPedidoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = estadoPedidoRepository.findAll().size();

        // Create the EstadoPedido with an existing ID
        estadoPedido.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEstadoPedidoMockMvc.perform(post("/api/estado-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estadoPedido)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoPedido in the database
        List<EstadoPedido> estadoPedidoList = estadoPedidoRepository.findAll();
        assertThat(estadoPedidoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoPedidoRepository.findAll().size();
        // set the field null
        estadoPedido.setCodigo(null);

        // Create the EstadoPedido, which fails.

        restEstadoPedidoMockMvc.perform(post("/api/estado-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estadoPedido)))
            .andExpect(status().isBadRequest());

        List<EstadoPedido> estadoPedidoList = estadoPedidoRepository.findAll();
        assertThat(estadoPedidoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescripcionIsRequired() throws Exception {
        int databaseSizeBeforeTest = estadoPedidoRepository.findAll().size();
        // set the field null
        estadoPedido.setDescripcion(null);

        // Create the EstadoPedido, which fails.

        restEstadoPedidoMockMvc.perform(post("/api/estado-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estadoPedido)))
            .andExpect(status().isBadRequest());

        List<EstadoPedido> estadoPedidoList = estadoPedidoRepository.findAll();
        assertThat(estadoPedidoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidos() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList
        restEstadoPedidoMockMvc.perform(get("/api/estado-pedidos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoPedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));
    }
    
    @Test
    @Transactional
    public void getEstadoPedido() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get the estadoPedido
        restEstadoPedidoMockMvc.perform(get("/api/estado-pedidos/{id}", estadoPedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(estadoPedido.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION));
    }


    @Test
    @Transactional
    public void getEstadoPedidosByIdFiltering() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        Long id = estadoPedido.getId();

        defaultEstadoPedidoShouldBeFound("id.equals=" + id);
        defaultEstadoPedidoShouldNotBeFound("id.notEquals=" + id);

        defaultEstadoPedidoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEstadoPedidoShouldNotBeFound("id.greaterThan=" + id);

        defaultEstadoPedidoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEstadoPedidoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEstadoPedidosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where codigo equals to DEFAULT_CODIGO
        defaultEstadoPedidoShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the estadoPedidoList where codigo equals to UPDATED_CODIGO
        defaultEstadoPedidoShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where codigo not equals to DEFAULT_CODIGO
        defaultEstadoPedidoShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the estadoPedidoList where codigo not equals to UPDATED_CODIGO
        defaultEstadoPedidoShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultEstadoPedidoShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the estadoPedidoList where codigo equals to UPDATED_CODIGO
        defaultEstadoPedidoShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where codigo is not null
        defaultEstadoPedidoShouldBeFound("codigo.specified=true");

        // Get all the estadoPedidoList where codigo is null
        defaultEstadoPedidoShouldNotBeFound("codigo.specified=false");
    }
                @Test
    @Transactional
    public void getAllEstadoPedidosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where codigo contains DEFAULT_CODIGO
        defaultEstadoPedidoShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the estadoPedidoList where codigo contains UPDATED_CODIGO
        defaultEstadoPedidoShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where codigo does not contain DEFAULT_CODIGO
        defaultEstadoPedidoShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the estadoPedidoList where codigo does not contain UPDATED_CODIGO
        defaultEstadoPedidoShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }


    @Test
    @Transactional
    public void getAllEstadoPedidosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where descripcion equals to DEFAULT_DESCRIPCION
        defaultEstadoPedidoShouldBeFound("descripcion.equals=" + DEFAULT_DESCRIPCION);

        // Get all the estadoPedidoList where descripcion equals to UPDATED_DESCRIPCION
        defaultEstadoPedidoShouldNotBeFound("descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByDescripcionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where descripcion not equals to DEFAULT_DESCRIPCION
        defaultEstadoPedidoShouldNotBeFound("descripcion.notEquals=" + DEFAULT_DESCRIPCION);

        // Get all the estadoPedidoList where descripcion not equals to UPDATED_DESCRIPCION
        defaultEstadoPedidoShouldBeFound("descripcion.notEquals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where descripcion in DEFAULT_DESCRIPCION or UPDATED_DESCRIPCION
        defaultEstadoPedidoShouldBeFound("descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION);

        // Get all the estadoPedidoList where descripcion equals to UPDATED_DESCRIPCION
        defaultEstadoPedidoShouldNotBeFound("descripcion.in=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where descripcion is not null
        defaultEstadoPedidoShouldBeFound("descripcion.specified=true");

        // Get all the estadoPedidoList where descripcion is null
        defaultEstadoPedidoShouldNotBeFound("descripcion.specified=false");
    }
                @Test
    @Transactional
    public void getAllEstadoPedidosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where descripcion contains DEFAULT_DESCRIPCION
        defaultEstadoPedidoShouldBeFound("descripcion.contains=" + DEFAULT_DESCRIPCION);

        // Get all the estadoPedidoList where descripcion contains UPDATED_DESCRIPCION
        defaultEstadoPedidoShouldNotBeFound("descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllEstadoPedidosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        estadoPedidoRepository.saveAndFlush(estadoPedido);

        // Get all the estadoPedidoList where descripcion does not contain DEFAULT_DESCRIPCION
        defaultEstadoPedidoShouldNotBeFound("descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);

        // Get all the estadoPedidoList where descripcion does not contain UPDATED_DESCRIPCION
        defaultEstadoPedidoShouldBeFound("descripcion.doesNotContain=" + UPDATED_DESCRIPCION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEstadoPedidoShouldBeFound(String filter) throws Exception {
        restEstadoPedidoMockMvc.perform(get("/api/estado-pedidos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(estadoPedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)));

        // Check, that the count call also returns 1
        restEstadoPedidoMockMvc.perform(get("/api/estado-pedidos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEstadoPedidoShouldNotBeFound(String filter) throws Exception {
        restEstadoPedidoMockMvc.perform(get("/api/estado-pedidos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEstadoPedidoMockMvc.perform(get("/api/estado-pedidos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEstadoPedido() throws Exception {
        // Get the estadoPedido
        restEstadoPedidoMockMvc.perform(get("/api/estado-pedidos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEstadoPedido() throws Exception {
        // Initialize the database
        estadoPedidoService.save(estadoPedido);

        int databaseSizeBeforeUpdate = estadoPedidoRepository.findAll().size();

        // Update the estadoPedido
        EstadoPedido updatedEstadoPedido = estadoPedidoRepository.findById(estadoPedido.getId()).get();
        // Disconnect from session so that the updates on updatedEstadoPedido are not directly saved in db
        em.detach(updatedEstadoPedido);
        updatedEstadoPedido
            .codigo(UPDATED_CODIGO)
            .descripcion(UPDATED_DESCRIPCION);

        restEstadoPedidoMockMvc.perform(put("/api/estado-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEstadoPedido)))
            .andExpect(status().isOk());

        // Validate the EstadoPedido in the database
        List<EstadoPedido> estadoPedidoList = estadoPedidoRepository.findAll();
        assertThat(estadoPedidoList).hasSize(databaseSizeBeforeUpdate);
        EstadoPedido testEstadoPedido = estadoPedidoList.get(estadoPedidoList.size() - 1);
        assertThat(testEstadoPedido.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testEstadoPedido.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void updateNonExistingEstadoPedido() throws Exception {
        int databaseSizeBeforeUpdate = estadoPedidoRepository.findAll().size();

        // Create the EstadoPedido

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEstadoPedidoMockMvc.perform(put("/api/estado-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(estadoPedido)))
            .andExpect(status().isBadRequest());

        // Validate the EstadoPedido in the database
        List<EstadoPedido> estadoPedidoList = estadoPedidoRepository.findAll();
        assertThat(estadoPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEstadoPedido() throws Exception {
        // Initialize the database
        estadoPedidoService.save(estadoPedido);

        int databaseSizeBeforeDelete = estadoPedidoRepository.findAll().size();

        // Delete the estadoPedido
        restEstadoPedidoMockMvc.perform(delete("/api/estado-pedidos/{id}", estadoPedido.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EstadoPedido> estadoPedidoList = estadoPedidoRepository.findAll();
        assertThat(estadoPedidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
