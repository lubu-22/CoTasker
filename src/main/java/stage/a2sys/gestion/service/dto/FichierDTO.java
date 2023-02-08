package stage.a2sys.gestion.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link stage.a2sys.gestion.domain.Fichier} entity.
 */
public class FichierDTO implements Serializable {

    private Long id;

    private String nomF;

    private String cheminF;

    private String codeF;

    private DossierDTO dossier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomF() {
        return nomF;
    }

    public void setNomF(String nomF) {
        this.nomF = nomF;
    }

    public String getCheminF() {
        return cheminF;
    }

    public void setCheminF(String cheminF) {
        this.cheminF = cheminF;
    }

    public String getCodeF() {
        return codeF;
    }

    public void setCodeF(String codeF) {
        this.codeF = codeF;
    }

    public DossierDTO getDossier() {
        return dossier;
    }

    public void setDossier(DossierDTO dossier) {
        this.dossier = dossier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FichierDTO)) {
            return false;
        }

        FichierDTO fichierDTO = (FichierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fichierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichierDTO{" +
            "id=" + getId() +
            ", nomF='" + getNomF() + "'" +
            ", cheminF='" + getCheminF() + "'" +
            ", codeF='" + getCodeF() + "'" +
            ", dossier=" + getDossier() +
            "}";
    }
}
