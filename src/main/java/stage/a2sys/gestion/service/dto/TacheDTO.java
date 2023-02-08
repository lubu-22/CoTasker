package stage.a2sys.gestion.service.dto;

import java.io.Serializable;
import java.util.Objects;
import stage.a2sys.gestion.domain.enumeration.Role;

/**
 * A DTO for the {@link stage.a2sys.gestion.domain.Tache} entity.
 */
public class TacheDTO implements Serializable {

    private Long id;

    private Role role;

    private String descriptiontache;

    private ProjetDTO projet;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDescriptiontache() {
        return descriptiontache;
    }

    public void setDescriptiontache(String descriptiontache) {
        this.descriptiontache = descriptiontache;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TacheDTO)) {
            return false;
        }

        TacheDTO tacheDTO = (TacheDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tacheDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TacheDTO{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", descriptiontache='" + getDescriptiontache() + "'" +
            ", projet='" + getProjet() + "'" +
            ", user='" + getUser() + "'" +
            "}";
    }

    public ProjetDTO getProjet() {
        return projet;
    }

    public void setProjet(ProjetDTO projet) {
        this.projet = projet;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
