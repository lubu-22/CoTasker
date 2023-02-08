package stage.a2sys.gestion.service;

import java.io.IOException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import stage.a2sys.gestion.service.dto.FichierDTO;

/**
 * Service Interface for managing {@link stage.a2sys.gestion.domain.Fichier}.
 */
public interface FichierService {
    /**
     * Save a fichier.
     *
     * @param fichierDTO the entity to save.
     * @return the persisted entity.
     */
    FichierDTO save(FichierDTO fichierDTO);
    int savefichier(MultipartFile file, Long id) throws IOException;

    byte [] avoirunfichier(Long id);

    /**
     * Updates a fichier.
     *
     * @param fichierDTO the entity to update.
     * @return the persisted entity.
     */
    FichierDTO update(FichierDTO fichierDTO);

    /**
     * Partially updates a fichier.
     *
     * @param fichierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FichierDTO> partialUpdate(FichierDTO fichierDTO);

    /**
     * Get all the fichiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FichierDTO> findAll(Pageable pageable);

    /**
     * Get the "id" fichier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FichierDTO> findOne(Long id);

    /**
     * Delete the "id" fichier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
