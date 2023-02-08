package stage.a2sys.gestion.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link stage.a2sys.gestion.domain.Client} entity.
 */
public class ClientDTO implements Serializable {

    private Long id;

    private String nomC;

    private String adresse;

    private Long numero;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomC() {
        return nomC;
    }

    public void setNomC(String nomC) {
        this.nomC = nomC;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientDTO)) {
            return false;
        }

        ClientDTO clientDTO = (ClientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, clientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClientDTO{" +
            "id=" + getId() +
            ", nomC='" + getNomC() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", numero=" + getNumero() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
