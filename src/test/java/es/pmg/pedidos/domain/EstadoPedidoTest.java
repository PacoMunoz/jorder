package es.pmg.pedidos.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import es.pmg.pedidos.web.rest.TestUtil;

public class EstadoPedidoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EstadoPedido.class);
        EstadoPedido estadoPedido1 = new EstadoPedido();
        estadoPedido1.setId(1L);
        EstadoPedido estadoPedido2 = new EstadoPedido();
        estadoPedido2.setId(estadoPedido1.getId());
        assertThat(estadoPedido1).isEqualTo(estadoPedido2);
        estadoPedido2.setId(2L);
        assertThat(estadoPedido1).isNotEqualTo(estadoPedido2);
        estadoPedido1.setId(null);
        assertThat(estadoPedido1).isNotEqualTo(estadoPedido2);
    }
}
