package stage.a2sys.gestion.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import stage.a2sys.gestion.domain.enumeration.Etape;
import stage.a2sys.gestion.domain.enumeration.TypeProjet;

/**
 * A Projet.
 */
@Entity
@Table(name = "projet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Projet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "intitule")
    private String intitule;

    @Column(name = "description_projet")
    private String descriptionProjet;

    @Column(name = "cheminP")
    private String cheminP;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeProjet type;

    @Enumerated(EnumType.STRING)
    @Column(name = "etape")
    private Etape etape;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @OneToMany(mappedBy = "projet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "projet" }, allowSetters = true)
    private Set<Dossier> dossiers = new HashSet<>();

    @OneToMany(mappedBy = "projet")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "projet" }, allowSetters = true)
    private Set<Tache> taches = new HashSet<>();

    @ManyToOne
    private Client client;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Projet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntitule() {
        return this.intitule;
    }

    public Projet intitule(String intitule) {
        this.setIntitule(intitule);
        return this;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }



    public String getDescriptionProjet() {
        return this.descriptionProjet;
    }

    public Projet descriptionProjet(String descriptionProjet) {
        this.setDescriptionProjet(descriptionProjet);
        return this;
    }

    public void setDescriptionProjet(String descriptionProjet) {
        this.descriptionProjet = descriptionProjet;
    }

    public TypeProjet getType() {
        return this.type;
    }

    public Projet type(TypeProjet type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeProjet type) {
        this.type = type;
    }

    public Etape getEtape() {
        return this.etape;
    }

    public Projet etape(Etape etape) {
        this.setEtape(etape);
        return this;
    }

    public void setEtape(Etape etape) {
        this.etape = etape;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Projet dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Projet dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Projet client(Client client) {
        this.setClient(client);
        return this;
    }



    public void setUser(User user) {
        this.user = user;
    }
    public Projet user(User user) {
        this.setUser(user);
        return this;
    }


    public Set<Dossier> getDossiers() {
        return this.dossiers;
    }

    public void setDossiers(Set<Dossier> dossiers) {
        if (this.dossiers != null) {
            this.dossiers.forEach(i -> i.setProjet(null));
        }
        if (dossiers != null) {
            dossiers.forEach(i -> i.setProjet(this));
        }
        this.dossiers = dossiers;
    }

    public Projet fichiers(Set<Dossier> dossiers) {
        this.setDossiers(dossiers);
        return this;
    }

    public Projet addDossier(Dossier dossier) {
        this.dossiers.add(dossier);
        dossier.setProjet(this);
        return this;
    }

    public Projet removeDossier(Dossier dossier) {
        this.dossiers.remove(dossier);
        dossier.setFichiers(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projet)) {
            return false;
        }
        return id != null && id.equals(((Projet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Projet{" +
            "id=" + getId() +
            ", intitule='" + getIntitule() + "'" +
            ", descriptionProjet='" + getDescriptionProjet() + "'" +
            ", type='" + getType() + "'" +
            ", etape='" + getEtape() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }

    public String getCheminP() {
        return cheminP;
    }

    public void setCheminP(String cheminP) {
        this.cheminP = cheminP;
    }

    public Set<Tache> getTaches() {
        return taches;
    }

    public void setTaches(Set<Tache> taches) {
        this.taches = taches;
    }
}
