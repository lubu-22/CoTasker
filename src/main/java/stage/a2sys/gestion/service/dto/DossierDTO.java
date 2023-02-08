package stage.a2sys.gestion.service.dto;

import stage.a2sys.gestion.domain.enumeration.TypeDossier;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link stage.a2sys.gestion.domain.Dossier} entity.
 */
public class DossierDTO implements Serializable {

    private Long id;

    private String nomD;

    private String cheminD;

    private ProjetDTO projet;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomD() {
        return nomD;
    }

    public void setNomD(String nomD) {
        this.nomD = nomD;
    }

    public String getCheminD() {
        return cheminD;
    }

    public void setCheminD(String cheminD) {
        this.cheminD = cheminD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DossierDTO)) {
            return false;
        }

        DossierDTO dossierDTO = (DossierDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dossierDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DossierDTO{" +
            "id=" + getId() +
            ", nomD='" + getNomD() + "'" +
            ", cheminD='" + getCheminD() + "'" +
            "}";
    }


    public ProjetDTO getProjet() {
        return projet;
    }

    public void setProjet(ProjetDTO projet) {
        this.projet = projet;
    }
}
