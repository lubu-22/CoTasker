package stage.a2sys.gestion.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import stage.a2sys.gestion.web.rest.TestUtil;

class DossierDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierDTO.class);
        DossierDTO dossierDTO1 = new DossierDTO();
        dossierDTO1.setId(1L);
        DossierDTO dossierDTO2 = new DossierDTO();
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
        dossierDTO2.setId(dossierDTO1.getId());
        assertThat(dossierDTO1).isEqualTo(dossierDTO2);
        dossierDTO2.setId(2L);
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
        dossierDTO1.setId(null);
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
    }
}
