package stage.a2sys.gestion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import stage.a2sys.gestion.domain.enumeration.TypeDossier;
import stage.a2sys.gestion.domain.enumeration.TypeProjet;

/**
 * A Dossier.
 */
@Entity
@Table(name = "dossier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dossier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "nom_d")
    private String  nomD;

    @Column(name = "chemin_d")
    private String cheminD;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dossiers" }, allowSetters = true)
    private Projet projet;


    @OneToMany(mappedBy = "dossier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dossier" }, allowSetters = true)
    private Set<Fichier> fichiers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dossier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomD() {
        return this.nomD;
    }

    public Dossier nomD(String nomD) {
        this.setNomD(nomD);
        return this;
    }

    public void setNomD(String nomD) {
        this.nomD = nomD;
    }

    public String getCheminD() {
        return this.cheminD;
    }

    public Dossier cheminD(String cheminD) {
        this.setCheminD(cheminD);
        return this;
    }

    public void setCheminD(String cheminD) {
        this.cheminD = cheminD;
    }

    public Set<Fichier> getFichiers() {
        return this.fichiers;
    }

    public void setFichiers(Set<Fichier> fichiers) {
        if (this.fichiers != null)
        {
            this.fichiers.forEach(i -> i.setDossier(null));
        }
        if (fichiers != null) {
            fichiers.forEach(i -> i.setDossier(this));
        }
        this.fichiers = fichiers;
    }

    public Dossier fichiers(Set<Fichier> fichiers) {
        this.setFichiers(fichiers);
        return this;
    }

    public Dossier addFichier(Fichier fichier) {
        this.fichiers.add(fichier);
        fichier.setDossier(this);
        return this;
    }

    public Dossier removeFichier(Fichier fichier) {
        this.fichiers.remove(fichier);
        fichier.setDossier(null);
        return this;
    }

    public Projet getProjet() {
        return this.projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Dossier projet(Projet projet) {
        this.setProjet(projet);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dossier)) {
            return false;
        }
        return id != null && id.equals(((Dossier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dossier{" +
            "id=" + getId() +
            ", nomD='" + getNomD() + "'" +
            ", cheminD='" + getCheminD() + "'" +
            "}";
    }
}
