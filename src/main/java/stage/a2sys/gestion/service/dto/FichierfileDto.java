package stage.a2sys.gestion.service.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

public class FichierfileDto implements Serializable {
    private final Long id;
    private final FichierDTO fichier;
    private final MultipartFile file;

    public FichierfileDto(Long id, FichierDTO fichier, MultipartFile file) {
        this.id = id;
        this.fichier = fichier;
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public FichierDTO getFichier() {
        return fichier;
    }

    public MultipartFile getFile() {
        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FichierfileDto entity = (FichierfileDto) o;
        return Objects.equals(this.id, entity.id) &&
            Objects.equals(this.fichier, entity.fichier) &&
            Objects.equals(this.file, entity.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fichier, file);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "fichier = " + fichier + ", " +
            "file = " + file + ")";
    }
}
