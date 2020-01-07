package es.pmg.pedidos.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import es.pmg.pedidos.web.rest.TestUtil;

public class LineaPedidoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LineaPedido.class);
        LineaPedido lineaPedido1 = new LineaPedido();
        lineaPedido1.setId(1L);
        LineaPedido lineaPedido2 = new LineaPedido();
        lineaPedido2.setId(lineaPedido1.getId());
        assertThat(lineaPedido1).isEqualTo(lineaPedido2);
        lineaPedido2.setId(2L);
        assertThat(lineaPedido1).isNotEqualTo(lineaPedido2);
        lineaPedido1.setId(null);
        assertThat(lineaPedido1).isNotEqualTo(lineaPedido2);
    }
}
