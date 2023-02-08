package stage.a2sys.gestion.domain;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;


@Entity
public class Fichierfile implements Serializable {
    @Id
    private Long id;

    @ManyToOne
    private Fichier fichier;
    private File file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fichierfile(Fichier fichier, File file) {
        this.fichier = fichier;
        this.file = file;
    }

    public Fichierfile() {

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Fichier getFichier() {
        return fichier;
    }

    public void setFichier(Fichier fichier) {
        this.fichier = fichier;
    }
}
