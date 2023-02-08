package stage.a2sys.gestion.domain;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import stage.a2sys.gestion.domain.enumeration.Role;

/**
 * A Tache.
 */
@Entity
@Table(name = "tache")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tache implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "descriptiontache")
    private String descriptiontache;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tache id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Role getRole() {
        return this.role;
    }

    public Tache role(Role role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getDescriptiontache() {
        return this.descriptiontache;
    }

    public Tache descriptiontache(String descriptiontache) {
        this.setDescriptiontache(descriptiontache);
        return this;
    }

    public void setDescriptiontache(String descriptiontache) {
        this.descriptiontache = descriptiontache;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tache)) {
            return false;
        }
        return id != null && id.equals(((Tache) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tache{" +
            "id=" + getId() +
            ", role='" + getRole() + "'" +
            ", descriptiontache='" + getDescriptiontache() + "'" +
            "}";
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Tache projet(Projet projet) {
        this.setProjet(projet);
        return this;
    }
    public Tache user(User user) {
        this.setUser(user);
        return this;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
