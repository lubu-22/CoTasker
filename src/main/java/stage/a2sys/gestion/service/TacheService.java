package stage.a2sys.gestion.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import stage.a2sys.gestion.service.dto.TacheDTO;

/**
 * Service Interface for managing {@link stage.a2sys.gestion.domain.Tache}.
 */
public interface TacheService {
    /**
     * Save a tache.
     *
     * @param tacheDTO the entity to save.
     * @return the persisted entity.
     */
    TacheDTO save(TacheDTO tacheDTO);

    /**
     * Updates a tache.
     *
     * @param tacheDTO the entity to update.
     * @return the persisted entity.
     */
    TacheDTO update(TacheDTO tacheDTO);

    /**
     * Partially updates a tache.
     *
     * @param tacheDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TacheDTO> partialUpdate(TacheDTO tacheDTO);

    /**
     * Get all the taches.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TacheDTO> findAll(Pageable pageable);

    /**
     * Get the "id" tache.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TacheDTO> findOne(Long id);

    /**
     * Delete the "id" tache.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
