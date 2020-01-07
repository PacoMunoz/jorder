package es.pmg.pedidos.web.rest;

import es.pmg.pedidos.PedidosApp;
import es.pmg.pedidos.domain.Pedido;
import es.pmg.pedidos.domain.Direccion;
import es.pmg.pedidos.domain.Cliente;
import es.pmg.pedidos.domain.FormaPago;
import es.pmg.pedidos.domain.EstadoPedido;
import es.pmg.pedidos.repository.PedidoRepository;
import es.pmg.pedidos.service.PedidoService;
import es.pmg.pedidos.web.rest.errors.ExceptionTranslator;
import es.pmg.pedidos.service.dto.PedidoCriteria;
import es.pmg.pedidos.service.PedidoQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static es.pmg.pedidos.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PedidoResource} REST controller.
 */
@SpringBootTest(classes = PedidosApp.class)
public class PedidoResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBB";

    private static final Boolean DEFAULT_A_DOMICILIO = false;
    private static final Boolean UPDATED_A_DOMICILIO = true;

    private static final Instant DEFAULT_FECHA_PEDIDO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_PEDIDO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoQueryService pedidoQueryService;

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

    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PedidoResource pedidoResource = new PedidoResource(pedidoService, pedidoQueryService);
        this.restPedidoMockMvc = MockMvcBuilders.standaloneSetup(pedidoResource)
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
    public static Pedido createEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .codigo(DEFAULT_CODIGO)
            .aDomicilio(DEFAULT_A_DOMICILIO)
            .fechaPedido(DEFAULT_FECHA_PEDIDO);
        return pedido;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createUpdatedEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .codigo(UPDATED_CODIGO)
            .aDomicilio(UPDATED_A_DOMICILIO)
            .fechaPedido(UPDATED_FECHA_PEDIDO);
        return pedido;
    }

    @BeforeEach
    public void initTest() {
        pedido = createEntity(em);
    }

    @Test
    @Transactional
    public void createPedido() throws Exception {
        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();

        // Create the Pedido
        restPedidoMockMvc.perform(post("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isCreated());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeCreate + 1);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testPedido.isaDomicilio()).isEqualTo(DEFAULT_A_DOMICILIO);
        assertThat(testPedido.getFechaPedido()).isEqualTo(DEFAULT_FECHA_PEDIDO);
    }

    @Test
    @Transactional
    public void createPedidoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pedidoRepository.findAll().size();

        // Create the Pedido with an existing ID
        pedido.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidoMockMvc.perform(post("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodigoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidoRepository.findAll().size();
        // set the field null
        pedido.setCodigo(null);

        // Create the Pedido, which fails.

        restPedidoMockMvc.perform(post("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isBadRequest());

        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFechaPedidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pedidoRepository.findAll().size();
        // set the field null
        pedido.setFechaPedido(null);

        // Create the Pedido, which fails.

        restPedidoMockMvc.perform(post("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isBadRequest());

        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPedidos() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList
        restPedidoMockMvc.perform(get("/api/pedidos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].aDomicilio").value(hasItem(DEFAULT_A_DOMICILIO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaPedido").value(hasItem(DEFAULT_FECHA_PEDIDO.toString())));
    }
    
    @Test
    @Transactional
    public void getPedido() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.aDomicilio").value(DEFAULT_A_DOMICILIO.booleanValue()))
            .andExpect(jsonPath("$.fechaPedido").value(DEFAULT_FECHA_PEDIDO.toString()));
    }


    @Test
    @Transactional
    public void getPedidosByIdFiltering() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        Long id = pedido.getId();

        defaultPedidoShouldBeFound("id.equals=" + id);
        defaultPedidoShouldNotBeFound("id.notEquals=" + id);

        defaultPedidoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPedidoShouldNotBeFound("id.greaterThan=" + id);

        defaultPedidoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPedidoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPedidosByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where codigo equals to DEFAULT_CODIGO
        defaultPedidoShouldBeFound("codigo.equals=" + DEFAULT_CODIGO);

        // Get all the pedidoList where codigo equals to UPDATED_CODIGO
        defaultPedidoShouldNotBeFound("codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllPedidosByCodigoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where codigo not equals to DEFAULT_CODIGO
        defaultPedidoShouldNotBeFound("codigo.notEquals=" + DEFAULT_CODIGO);

        // Get all the pedidoList where codigo not equals to UPDATED_CODIGO
        defaultPedidoShouldBeFound("codigo.notEquals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllPedidosByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where codigo in DEFAULT_CODIGO or UPDATED_CODIGO
        defaultPedidoShouldBeFound("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO);

        // Get all the pedidoList where codigo equals to UPDATED_CODIGO
        defaultPedidoShouldNotBeFound("codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllPedidosByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where codigo is not null
        defaultPedidoShouldBeFound("codigo.specified=true");

        // Get all the pedidoList where codigo is null
        defaultPedidoShouldNotBeFound("codigo.specified=false");
    }
                @Test
    @Transactional
    public void getAllPedidosByCodigoContainsSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where codigo contains DEFAULT_CODIGO
        defaultPedidoShouldBeFound("codigo.contains=" + DEFAULT_CODIGO);

        // Get all the pedidoList where codigo contains UPDATED_CODIGO
        defaultPedidoShouldNotBeFound("codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    public void getAllPedidosByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where codigo does not contain DEFAULT_CODIGO
        defaultPedidoShouldNotBeFound("codigo.doesNotContain=" + DEFAULT_CODIGO);

        // Get all the pedidoList where codigo does not contain UPDATED_CODIGO
        defaultPedidoShouldBeFound("codigo.doesNotContain=" + UPDATED_CODIGO);
    }


    @Test
    @Transactional
    public void getAllPedidosByaDomicilioIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where aDomicilio equals to DEFAULT_A_DOMICILIO
        defaultPedidoShouldBeFound("aDomicilio.equals=" + DEFAULT_A_DOMICILIO);

        // Get all the pedidoList where aDomicilio equals to UPDATED_A_DOMICILIO
        defaultPedidoShouldNotBeFound("aDomicilio.equals=" + UPDATED_A_DOMICILIO);
    }

    @Test
    @Transactional
    public void getAllPedidosByaDomicilioIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where aDomicilio not equals to DEFAULT_A_DOMICILIO
        defaultPedidoShouldNotBeFound("aDomicilio.notEquals=" + DEFAULT_A_DOMICILIO);

        // Get all the pedidoList where aDomicilio not equals to UPDATED_A_DOMICILIO
        defaultPedidoShouldBeFound("aDomicilio.notEquals=" + UPDATED_A_DOMICILIO);
    }

    @Test
    @Transactional
    public void getAllPedidosByaDomicilioIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where aDomicilio in DEFAULT_A_DOMICILIO or UPDATED_A_DOMICILIO
        defaultPedidoShouldBeFound("aDomicilio.in=" + DEFAULT_A_DOMICILIO + "," + UPDATED_A_DOMICILIO);

        // Get all the pedidoList where aDomicilio equals to UPDATED_A_DOMICILIO
        defaultPedidoShouldNotBeFound("aDomicilio.in=" + UPDATED_A_DOMICILIO);
    }

    @Test
    @Transactional
    public void getAllPedidosByaDomicilioIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where aDomicilio is not null
        defaultPedidoShouldBeFound("aDomicilio.specified=true");

        // Get all the pedidoList where aDomicilio is null
        defaultPedidoShouldNotBeFound("aDomicilio.specified=false");
    }

    @Test
    @Transactional
    public void getAllPedidosByFechaPedidoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaPedido equals to DEFAULT_FECHA_PEDIDO
        defaultPedidoShouldBeFound("fechaPedido.equals=" + DEFAULT_FECHA_PEDIDO);

        // Get all the pedidoList where fechaPedido equals to UPDATED_FECHA_PEDIDO
        defaultPedidoShouldNotBeFound("fechaPedido.equals=" + UPDATED_FECHA_PEDIDO);
    }

    @Test
    @Transactional
    public void getAllPedidosByFechaPedidoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaPedido not equals to DEFAULT_FECHA_PEDIDO
        defaultPedidoShouldNotBeFound("fechaPedido.notEquals=" + DEFAULT_FECHA_PEDIDO);

        // Get all the pedidoList where fechaPedido not equals to UPDATED_FECHA_PEDIDO
        defaultPedidoShouldBeFound("fechaPedido.notEquals=" + UPDATED_FECHA_PEDIDO);
    }

    @Test
    @Transactional
    public void getAllPedidosByFechaPedidoIsInShouldWork() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaPedido in DEFAULT_FECHA_PEDIDO or UPDATED_FECHA_PEDIDO
        defaultPedidoShouldBeFound("fechaPedido.in=" + DEFAULT_FECHA_PEDIDO + "," + UPDATED_FECHA_PEDIDO);

        // Get all the pedidoList where fechaPedido equals to UPDATED_FECHA_PEDIDO
        defaultPedidoShouldNotBeFound("fechaPedido.in=" + UPDATED_FECHA_PEDIDO);
    }

    @Test
    @Transactional
    public void getAllPedidosByFechaPedidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaPedido is not null
        defaultPedidoShouldBeFound("fechaPedido.specified=true");

        // Get all the pedidoList where fechaPedido is null
        defaultPedidoShouldNotBeFound("fechaPedido.specified=false");
    }

    @Test
    @Transactional
    public void getAllPedidosByDireccionIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        Direccion direccion = DireccionResourceIT.createEntity(em);
        em.persist(direccion);
        em.flush();
        pedido.setDireccion(direccion);
        pedidoRepository.saveAndFlush(pedido);
        Long direccionId = direccion.getId();

        // Get all the pedidoList where direccion equals to direccionId
        defaultPedidoShouldBeFound("direccionId.equals=" + direccionId);

        // Get all the pedidoList where direccion equals to direccionId + 1
        defaultPedidoShouldNotBeFound("direccionId.equals=" + (direccionId + 1));
    }


    @Test
    @Transactional
    public void getAllPedidosByClienteIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        Cliente cliente = ClienteResourceIT.createEntity(em);
        em.persist(cliente);
        em.flush();
        pedido.setCliente(cliente);
        pedidoRepository.saveAndFlush(pedido);
        Long clienteId = cliente.getId();

        // Get all the pedidoList where cliente equals to clienteId
        defaultPedidoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the pedidoList where cliente equals to clienteId + 1
        defaultPedidoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }


    @Test
    @Transactional
    public void getAllPedidosByFormaPagoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        FormaPago formaPago = FormaPagoResourceIT.createEntity(em);
        em.persist(formaPago);
        em.flush();
        pedido.setFormaPago(formaPago);
        pedidoRepository.saveAndFlush(pedido);
        Long formaPagoId = formaPago.getId();

        // Get all the pedidoList where formaPago equals to formaPagoId
        defaultPedidoShouldBeFound("formaPagoId.equals=" + formaPagoId);

        // Get all the pedidoList where formaPago equals to formaPagoId + 1
        defaultPedidoShouldNotBeFound("formaPagoId.equals=" + (formaPagoId + 1));
    }


    @Test
    @Transactional
    public void getAllPedidosByEstadoPedidoIsEqualToSomething() throws Exception {
        // Initialize the database
        pedidoRepository.saveAndFlush(pedido);
        EstadoPedido estadoPedido = EstadoPedidoResourceIT.createEntity(em);
        em.persist(estadoPedido);
        em.flush();
        pedido.setEstadoPedido(estadoPedido);
        pedidoRepository.saveAndFlush(pedido);
        Long estadoPedidoId = estadoPedido.getId();

        // Get all the pedidoList where estadoPedido equals to estadoPedidoId
        defaultPedidoShouldBeFound("estadoPedidoId.equals=" + estadoPedidoId);

        // Get all the pedidoList where estadoPedido equals to estadoPedidoId + 1
        defaultPedidoShouldNotBeFound("estadoPedidoId.equals=" + (estadoPedidoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPedidoShouldBeFound(String filter) throws Exception {
        restPedidoMockMvc.perform(get("/api/pedidos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].aDomicilio").value(hasItem(DEFAULT_A_DOMICILIO.booleanValue())))
            .andExpect(jsonPath("$.[*].fechaPedido").value(hasItem(DEFAULT_FECHA_PEDIDO.toString())));

        // Check, that the count call also returns 1
        restPedidoMockMvc.perform(get("/api/pedidos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPedidoShouldNotBeFound(String filter) throws Exception {
        restPedidoMockMvc.perform(get("/api/pedidos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPedidoMockMvc.perform(get("/api/pedidos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get("/api/pedidos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePedido() throws Exception {
        // Initialize the database
        pedidoService.save(pedido);

        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Update the pedido
        Pedido updatedPedido = pedidoRepository.findById(pedido.getId()).get();
        // Disconnect from session so that the updates on updatedPedido are not directly saved in db
        em.detach(updatedPedido);
        updatedPedido
            .codigo(UPDATED_CODIGO)
            .aDomicilio(UPDATED_A_DOMICILIO)
            .fechaPedido(UPDATED_FECHA_PEDIDO);

        restPedidoMockMvc.perform(put("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPedido)))
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
        Pedido testPedido = pedidoList.get(pedidoList.size() - 1);
        assertThat(testPedido.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testPedido.isaDomicilio()).isEqualTo(UPDATED_A_DOMICILIO);
        assertThat(testPedido.getFechaPedido()).isEqualTo(UPDATED_FECHA_PEDIDO);
    }

    @Test
    @Transactional
    public void updateNonExistingPedido() throws Exception {
        int databaseSizeBeforeUpdate = pedidoRepository.findAll().size();

        // Create the Pedido

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc.perform(put("/api/pedidos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pedido)))
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePedido() throws Exception {
        // Initialize the database
        pedidoService.save(pedido);

        int databaseSizeBeforeDelete = pedidoRepository.findAll().size();

        // Delete the pedido
        restPedidoMockMvc.perform(delete("/api/pedidos/{id}", pedido.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pedido> pedidoList = pedidoRepository.findAll();
        assertThat(pedidoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
