package es.pmg.pedidos.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import es.pmg.pedidos.web.rest.TestUtil;

public class LocalidadTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Localidad.class);
        Localidad localidad1 = new Localidad();
        localidad1.setId(1L);
        Localidad localidad2 = new Localidad();
        localidad2.setId(localidad1.getId());
        assertThat(localidad1).isEqualTo(localidad2);
        localidad2.setId(2L);
        assertThat(localidad1).isNotEqualTo(localidad2);
        localidad1.setId(null);
        assertThat(localidad1).isNotEqualTo(localidad2);
    }
}
