package stage.a2sys.gestion.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import stage.a2sys.gestion.web.rest.TestUtil;

class TacheTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tache.class);
        Tache tache1 = new Tache();
        tache1.setId(1L);
        Tache tache2 = new Tache();
        tache2.setId(tache1.getId());
        assertThat(tache1).isEqualTo(tache2);
        tache2.setId(2L);
        assertThat(tache1).isNotEqualTo(tache2);
        tache1.setId(null);
        assertThat(tache1).isNotEqualTo(tache2);
    }
}
