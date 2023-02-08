package stage.a2sys.gestion.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import stage.a2sys.gestion.domain.enumeration.Etape;
import stage.a2sys.gestion.domain.enumeration.TypeProjet;

/**
 * A DTO for the {@link stage.a2sys.gestion.domain.Projet} entity.
 */
public class ProjetDTO implements Serializable {

    private Long id;

    private String intitule;



    private String descriptionProjet;

    private String cheminP;

    private TypeProjet type;

    private Etape etape;

    private LocalDate dateCreation;

    private LocalDate dateFin;

    private ClientDTO client;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescriptionProjet() {
        return descriptionProjet;
    }

    public void setDescriptionProjet(String descriptionProjet) {
        this.descriptionProjet = descriptionProjet;
    }

    public TypeProjet getType() {
        return type;
    }

    public void setType(TypeProjet type) {
        this.type = type;
    }

    public Etape getEtape() {
        return etape;
    }

    public void setEtape(Etape etape) {
        this.etape = etape;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProjetDTO)) {
            return false;
        }

        ProjetDTO projetDTO = (ProjetDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, projetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProjetDTO{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", descriptionProjet='" + getDescriptionProjet() + "'" +
            ", type='" + getType() + "'" +
            ", etape='" + getEtape() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", client=" + getClient() +
            "}";
    }
}
