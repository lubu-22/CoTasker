package stage.a2sys.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Fichier.
 */
@Entity
@Table(name = "fichier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Fichier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_f")
    private String nomF;

    @Column(name = "chemin_f")
    private String cheminF;

    @Column(name = "code_f")
    private String codeF;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fichiers" }, allowSetters = true)
    private Dossier dossier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Fichier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomF() {
        return this.nomF;
    }

    public Fichier nomF(String nomF) {
        this.setNomF(nomF);
        return this;
    }

    public void setNomF(String nomF) {
        this.nomF = nomF;
    }

    public String getCheminF() {
        return this.cheminF;
    }

    public Fichier cheminF(String cheminF) {
        this.setCheminF(cheminF);
        return this;
    }

    public void setCheminF(String cheminF) {
        this.cheminF = cheminF;
    }

    public String getCodeF() {
        return this.codeF;
    }

    public Fichier codeF(String codeF) {
        this.setCodeF(codeF);
        return this;
    }

    public void setCodeF(String codeF) {
        this.codeF = codeF;
    }

    public Dossier getDossier() {
        return this.dossier;
    }

    public void setDossier(Dossier dossier) {
        this.dossier = dossier;
    }

    public Fichier dossier(Dossier dossier) {
        this.setDossier(dossier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fichier)) {
            return false;
        }
        return id != null && id.equals(((Fichier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Fichier{" +
            "id=" + getId() +
            ", nomF='" + getNomF() + "'" +
            ", cheminF='" + getCheminF() + "'" +
            ", codeF='" + getCodeF() + "'" +
            "}";
    }
}
