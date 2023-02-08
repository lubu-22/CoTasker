package stage.a2sys.gestion.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import stage.a2sys.gestion.web.rest.TestUtil;

class TacheDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TacheDTO.class);
        TacheDTO tacheDTO1 = new TacheDTO();
        tacheDTO1.setId(1L);
        TacheDTO tacheDTO2 = new TacheDTO();
        assertThat(tacheDTO1).isNotEqualTo(tacheDTO2);
        tacheDTO2.setId(tacheDTO1.getId());
        assertThat(tacheDTO1).isEqualTo(tacheDTO2);
        tacheDTO2.setId(2L);
        assertThat(tacheDTO1).isNotEqualTo(tacheDTO2);
        tacheDTO1.setId(null);
        assertThat(tacheDTO1).isNotEqualTo(tacheDTO2);
    }
}
