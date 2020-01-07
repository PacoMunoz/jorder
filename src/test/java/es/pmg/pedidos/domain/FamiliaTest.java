package es.pmg.pedidos.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import es.pmg.pedidos.web.rest.TestUtil;

public class FamiliaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Familia.class);
        Familia familia1 = new Familia();
        familia1.setId(1L);
        Familia familia2 = new Familia();
        familia2.setId(familia1.getId());
        assertThat(familia1).isEqualTo(familia2);
        familia2.setId(2L);
        assertThat(familia1).isNotEqualTo(familia2);
        familia1.setId(null);
        assertThat(familia1).isNotEqualTo(familia2);
    }
}
