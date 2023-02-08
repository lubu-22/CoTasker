package stage.a2sys.gestion.service;

import java.io.IOException;
import java.util.Optional;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import stage.a2sys.gestion.service.dto.DossierDTO;
import stage.a2sys.gestion.service.dto.FichierDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
/**
 * Service Interface for managing {@link stage.a2sys.gestion.domain.Dossier}.
 */
public interface DossierService {

    /**
     * Save a dossier.
     *
     * @param dossierDTO the entity to save.
     * @return the persisted entity.
     */
    DossierDTO save(DossierDTO dossierDTO);


    byte [] avoirunfichier(Long id);


    /**
     * Updates a dossier.
     *
     * @param dossierDTO the entity to update.
     * @return the persisted entity.
     */
    DossierDTO update(DossierDTO dossierDTO);

    /**
     * Partially updates a dossier.
     *
     * @param dossierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DossierDTO> partialUpdate(DossierDTO dossierDTO);

    /**
     * Get all the dossiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DossierDTO> findAll(Pageable pageable);

    /**
     * Get the "id" dossier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DossierDTO> findOne(Long id);

    /**
     * Delete the "id" dossier.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    List<FichierDTO> avoirfichierserviceimpl(Long id);




}
