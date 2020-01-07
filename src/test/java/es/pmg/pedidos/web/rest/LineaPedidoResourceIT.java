package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.PedidosApp;
import es.pmg.pedidos.domain.LineaPedido;
import es.pmg.pedidos.domain.Pedido;
import es.pmg.pedidos.domain.Producto;
import es.pmg.pedidos.repository.LineaPedidoRepository;
import es.pmg.pedidos.service.LineaPedidoService;
import es.pmg.pedidos.web.rest.errors.ExceptionTranslator;
import es.pmg.pedidos.service.dto.LineaPedidoCriteria;
import es.pmg.pedidos.service.LineaPedidoQueryService;

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
 * Integration tests for the {@link LineaPedidoResource} REST controller.
 */
@SpringBootTest(classes = PedidosApp.class)
public class LineaPedidoResourceIT {

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    @Autowired
    private LineaPedidoRepository lineaPedidoRepository;

    @Autowired
    private LineaPedidoService lineaPedidoService;

    @Autowired
    private LineaPedidoQueryService lineaPedidoQueryService;

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

    private MockMvc restLineaPedidoMockMvc;

    private LineaPedido lineaPedido;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LineaPedidoResource lineaPedidoResource = new LineaPedidoResource(lineaPedidoService, lineaPedidoQueryService);
        this.restLineaPedidoMockMvc = MockMvcBuilders.standaloneSetup(lineaPedidoResource)
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
    public static LineaPedido createEntity(EntityManager em) {
        LineaPedido lineaPedido = new LineaPedido()
            .cantidad(DEFAULT_CANTIDAD);
        return lineaPedido;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineaPedido createUpdatedEntity(EntityManager em) {
        LineaPedido lineaPedido = new LineaPedido()
            .cantidad(UPDATED_CANTIDAD);
        return lineaPedido;
    }

    @BeforeEach
    public void initTest() {
        lineaPedido = createEntity(em);
    }

    @Test
    @Transactional
    public void createLineaPedido() throws Exception {
        int databaseSizeBeforeCreate = lineaPedidoRepository.findAll().size();

        // Create the LineaPedido
        restLineaPedidoMockMvc.perform(post("/api/linea-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineaPedido)))
            .andExpect(status().isCreated());

        // Validate the LineaPedido in the database
        List<LineaPedido> lineaPedidoList = lineaPedidoRepository.findAll();
        assertThat(lineaPedidoList).hasSize(databaseSizeBeforeCreate + 1);
        LineaPedido testLineaPedido = lineaPedidoList.get(lineaPedidoList.size() - 1);
        assertThat(testLineaPedido.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    public void createLineaPedidoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineaPedidoRepository.findAll().size();

        // Create the LineaPedido with an existing ID
        lineaPedido.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineaPedidoMockMvc.perform(post("/api/linea-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineaPedido)))
            .andExpect(status().isBadRequest());

        // Validate the LineaPedido in the database
        List<LineaPedido> lineaPedidoList = lineaPedidoRepository.findAll();
        assertThat(lineaPedidoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCantidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = lineaPedidoRepository.findAll().size();
        // set the field null
        lineaPedido.setCantidad(null);

        // Create the LineaPedido, which fails.

        restLineaPedidoMockMvc.perform(post("/api/linea-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineaPedido)))
            .andExpect(status().isBadRequest());

        List<LineaPedido> lineaPedidoList = lineaPedidoRepository.findAll();
        assertThat(lineaPedidoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLineaPedidos() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList
        restLineaPedidoMockMvc.perform(get("/api/linea-pedidos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineaPedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)));
    }
    
    @Test
    @Transactional
    public void getLineaPedido() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get the lineaPedido
        restLineaPedidoMockMvc.perform(get("/api/linea-pedidos/{id}", lineaPedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineaPedido.getId().intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD));
    }


    @Test
    @Transactional
    public void getLineaPedidosByIdFiltering() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        Long id = lineaPedido.getId();

        defaultLineaPedidoShouldBeFound("id.equals=" + id);
        defaultLineaPedidoShouldNotBeFound("id.notEquals=" + id);

        defaultLineaPedidoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLineaPedidoShouldNotBeFound("id.greaterThan=" + id);

        defaultLineaPedidoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLineaPedidoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad equals to DEFAULT_CANTIDAD
        defaultLineaPedidoShouldBeFound("cantidad.equals=" + DEFAULT_CANTIDAD);

        // Get all the lineaPedidoList where cantidad equals to UPDATED_CANTIDAD
        defaultLineaPedidoShouldNotBeFound("cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsNotEqualToSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad not equals to DEFAULT_CANTIDAD
        defaultLineaPedidoShouldNotBeFound("cantidad.notEquals=" + DEFAULT_CANTIDAD);

        // Get all the lineaPedidoList where cantidad not equals to UPDATED_CANTIDAD
        defaultLineaPedidoShouldBeFound("cantidad.notEquals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad in DEFAULT_CANTIDAD or UPDATED_CANTIDAD
        defaultLineaPedidoShouldBeFound("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD);

        // Get all the lineaPedidoList where cantidad equals to UPDATED_CANTIDAD
        defaultLineaPedidoShouldNotBeFound("cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad is not null
        defaultLineaPedidoShouldBeFound("cantidad.specified=true");

        // Get all the lineaPedidoList where cantidad is null
        defaultLineaPedidoShouldNotBeFound("cantidad.specified=false");
    }

    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad is greater than or equal to DEFAULT_CANTIDAD
        defaultLineaPedidoShouldBeFound("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the lineaPedidoList where cantidad is greater than or equal to UPDATED_CANTIDAD
        defaultLineaPedidoShouldNotBeFound("cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad is less than or equal to DEFAULT_CANTIDAD
        defaultLineaPedidoShouldBeFound("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD);

        // Get all the lineaPedidoList where cantidad is less than or equal to SMALLER_CANTIDAD
        defaultLineaPedidoShouldNotBeFound("cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad is less than DEFAULT_CANTIDAD
        defaultLineaPedidoShouldNotBeFound("cantidad.lessThan=" + DEFAULT_CANTIDAD);

        // Get all the lineaPedidoList where cantidad is less than UPDATED_CANTIDAD
        defaultLineaPedidoShouldBeFound("cantidad.lessThan=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void getAllLineaPedidosByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);

        // Get all the lineaPedidoList where cantidad is greater than DEFAULT_CANTIDAD
        defaultLineaPedidoShouldNotBeFound("cantidad.greaterThan=" + DEFAULT_CANTIDAD);

        // Get all the lineaPedidoList where cantidad is greater than SMALLER_CANTIDAD
        defaultLineaPedidoShouldBeFound("cantidad.greaterThan=" + SMALLER_CANTIDAD);
    }


    @Test
    @Transactional
    public void getAllLineaPedidosByPedidoIsEqualToSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);
        Pedido pedido = PedidoResourceIT.createEntity(em);
        em.persist(pedido);
        em.flush();
        lineaPedido.setPedido(pedido);
        lineaPedidoRepository.saveAndFlush(lineaPedido);
        Long pedidoId = pedido.getId();

        // Get all the lineaPedidoList where pedido equals to pedidoId
        defaultLineaPedidoShouldBeFound("pedidoId.equals=" + pedidoId);

        // Get all the lineaPedidoList where pedido equals to pedidoId + 1
        defaultLineaPedidoShouldNotBeFound("pedidoId.equals=" + (pedidoId + 1));
    }


    @Test
    @Transactional
    public void getAllLineaPedidosByProductoIsEqualToSomething() throws Exception {
        // Initialize the database
        lineaPedidoRepository.saveAndFlush(lineaPedido);
        Producto producto = ProductoResourceIT.createEntity(em);
        em.persist(producto);
        em.flush();
        lineaPedido.setProducto(producto);
        lineaPedidoRepository.saveAndFlush(lineaPedido);
        Long productoId = producto.getId();

        // Get all the lineaPedidoList where producto equals to productoId
        defaultLineaPedidoShouldBeFound("productoId.equals=" + productoId);

        // Get all the lineaPedidoList where producto equals to productoId + 1
        defaultLineaPedidoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLineaPedidoShouldBeFound(String filter) throws Exception {
        restLineaPedidoMockMvc.perform(get("/api/linea-pedidos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineaPedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)));

        // Check, that the count call also returns 1
        restLineaPedidoMockMvc.perform(get("/api/linea-pedidos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLineaPedidoShouldNotBeFound(String filter) throws Exception {
        restLineaPedidoMockMvc.perform(get("/api/linea-pedidos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLineaPedidoMockMvc.perform(get("/api/linea-pedidos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLineaPedido() throws Exception {
        // Get the lineaPedido
        restLineaPedidoMockMvc.perform(get("/api/linea-pedidos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineaPedido() throws Exception {
        // Initialize the database
        lineaPedidoService.save(lineaPedido);

        int databaseSizeBeforeUpdate = lineaPedidoRepository.findAll().size();

        // Update the lineaPedido
        LineaPedido updatedLineaPedido = lineaPedidoRepository.findById(lineaPedido.getId()).get();
        // Disconnect from session so that the updates on updatedLineaPedido are not directly saved in db
        em.detach(updatedLineaPedido);
        updatedLineaPedido
            .cantidad(UPDATED_CANTIDAD);

        restLineaPedidoMockMvc.perform(put("/api/linea-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLineaPedido)))
            .andExpect(status().isOk());

        // Validate the LineaPedido in the database
        List<LineaPedido> lineaPedidoList = lineaPedidoRepository.findAll();
        assertThat(lineaPedidoList).hasSize(databaseSizeBeforeUpdate);
        LineaPedido testLineaPedido = lineaPedidoList.get(lineaPedidoList.size() - 1);
        assertThat(testLineaPedido.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    public void updateNonExistingLineaPedido() throws Exception {
        int databaseSizeBeforeUpdate = lineaPedidoRepository.findAll().size();

        // Create the LineaPedido

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineaPedidoMockMvc.perform(put("/api/linea-pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lineaPedido)))
            .andExpect(status().isBadRequest());

        // Validate the LineaPedido in the database
        List<LineaPedido> lineaPedidoList = lineaPedidoRepository.findAll();
        assertThat(lineaPedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLineaPedido() throws Exception {
        // Initialize the database
        lineaPedidoService.save(lineaPedido);

        int databaseSizeBeforeDelete = lineaPedidoRepository.findAll().size();

        // Delete the lineaPedido
        restLineaPedidoMockMvc.perform(delete("/api/linea-pedidos/{id}", lineaPedido.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LineaPedido> lineaPedidoList = lineaPedidoRepository.findAll();
        assertThat(lineaPedidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
