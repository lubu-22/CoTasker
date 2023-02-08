package stage.a2sys.gestion.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.a2sys.gestion.repository.FichierRepository;
import stage.a2sys.gestion.service.FichierService;
import stage.a2sys.gestion.service.dto.FichierDTO;
import stage.a2sys.gestion.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link stage.a2sys.gestion.domain.Fichier}.
 */
@RestController
@RequestMapping("/api")
public class FichierResource {

    private final Logger log = LoggerFactory.getLogger(FichierResource.class);

    private static final String ENTITY_NAME = "fichier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FichierService fichierService;

    private final FichierRepository fichierRepository;

    public FichierResource(FichierService fichierService, FichierRepository fichierRepository) {
        this.fichierService = fichierService;
        this.fichierRepository = fichierRepository;
    }

    /**
     * {@code POST  /fichiers} : Create a new fichier.
     *
     * @param fichierDTO the fichierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fichierDTO, or with status {@code 400 (Bad Request)} if the fichier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fichiers")
    public ResponseEntity<FichierDTO> createFichier(@RequestBody FichierDTO fichierDTO) throws URISyntaxException {
        log.debug("REST request to save Fichier : {}", fichierDTO);
        if (fichierDTO.getId() != null) {
            throw new BadRequestAlertException("A new fichier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FichierDTO result = fichierService.save(fichierDTO);
        return ResponseEntity
            .created(new URI("/api/fichiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fichiers/:id} : Updates an existing fichier.
     *
     * @param id the id of the fichierDTO to save.
     * @param fichierDTO the fichierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichierDTO,
     * or with status {@code 400 (Bad Request)} if the fichierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fichierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fichiers/{id}")
    public ResponseEntity<FichierDTO> updateFichier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FichierDTO fichierDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Fichier : {}, {}", id, fichierDTO);
        if (fichierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FichierDTO result = fichierService.update(fichierDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fichierDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fichiers/:id} : Partial updates given fields of an existing fichier, field will ignore if it is null
     *
     * @param id the id of the fichierDTO to save.
     * @param //fichierDTO the fichierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichierDTO,
     * or with status {@code 400 (Bad Request)} if the fichierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fichierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fichierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/fichiers/upload/{id}")
    public int uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {

            return fichierService.savefichier(file,id);

    }



    @GetMapping("/fichiers/ouvrir-fichier/{id}")
    public byte[] getDocumentById(@PathVariable Long id) {
        log.debug("REST request to get a page of fichier de d un dossier");
        return fichierService.avoirunfichier(id);
    }



    @PatchMapping(value = "/fichiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FichierDTO> partialUpdateFichier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FichierDTO fichierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Fichier partially : {}, {}", id, fichierDTO);
        if (fichierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FichierDTO> result = fichierService.partialUpdate(fichierDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fichierDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fichiers} : get all the fichiers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fichiers in body.
     */
    @GetMapping("/fichiers")
    public ResponseEntity<List<FichierDTO>> getAllFichiers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Fichiers");
        Page<FichierDTO> page = fichierService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fichiers/:id} : get the "id" fichier.
     *
     * @param id the id of the fichierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fichierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fichiers/{id}")
    public ResponseEntity<FichierDTO> getFichier(@PathVariable Long id) {
        log.debug("REST request to get Fichier : {}", id);
        Optional<FichierDTO> fichierDTO = fichierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fichierDTO);
    }

    /**
     * {@code DELETE  /fichiers/:id} : delete the "id" fichier.
     *
     * @param id the id of the fichierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fichiers/{id}")
    public ResponseEntity<Void> deleteFichier(@PathVariable Long id) {
        log.debug("REST request to delete Fichier : {}", id);
        fichierService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
