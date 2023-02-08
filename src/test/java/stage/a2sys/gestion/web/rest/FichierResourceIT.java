package stage.a2sys.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import stage.a2sys.gestion.IntegrationTest;
import stage.a2sys.gestion.domain.Fichier;
import stage.a2sys.gestion.repository.FichierRepository;
import stage.a2sys.gestion.service.dto.FichierDTO;
import stage.a2sys.gestion.service.mapper.FichierMapper;

/**
 * Integration tests for the {@link FichierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FichierResourceIT {

    private static final String DEFAULT_NOM_F = "AAAAAAAAAA";
    private static final String UPDATED_NOM_F = "BBBBBBBBBB";

    private static final String DEFAULT_CHEMIN_F = "AAAAAAAAAA";
    private static final String UPDATED_CHEMIN_F = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_F = "AAAAAAAAAA";
    private static final String UPDATED_CODE_F = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/fichiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FichierRepository fichierRepository;

    @Autowired
    private FichierMapper fichierMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFichierMockMvc;

    private Fichier fichier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fichier createEntity(EntityManager em) {
        Fichier fichier = new Fichier().nomF(DEFAULT_NOM_F).cheminF(DEFAULT_CHEMIN_F).codeF(DEFAULT_CODE_F);
        return fichier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Fichier createUpdatedEntity(EntityManager em) {
        Fichier fichier = new Fichier().nomF(UPDATED_NOM_F).cheminF(UPDATED_CHEMIN_F).codeF(UPDATED_CODE_F);
        return fichier;
    }

    @BeforeEach
    public void initTest() {
        fichier = createEntity(em);
    }

    @Test
    @Transactional
    void createFichier() throws Exception {
        int databaseSizeBeforeCreate = fichierRepository.findAll().size();
        // Create the Fichier
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);
        restFichierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichierDTO)))
            .andExpect(status().isCreated());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeCreate + 1);
        Fichier testFichier = fichierList.get(fichierList.size() - 1);
        assertThat(testFichier.getNomF()).isEqualTo(DEFAULT_NOM_F);
        assertThat(testFichier.getCheminF()).isEqualTo(DEFAULT_CHEMIN_F);
        assertThat(testFichier.getCodeF()).isEqualTo(DEFAULT_CODE_F);
    }

    @Test
    @Transactional
    void createFichierWithExistingId() throws Exception {
        // Create the Fichier with an existing ID
        fichier.setId(1L);
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);

        int databaseSizeBeforeCreate = fichierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFichierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFichiers() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        // Get all the fichierList
        restFichierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichier.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomF").value(hasItem(DEFAULT_NOM_F)))
            .andExpect(jsonPath("$.[*].cheminF").value(hasItem(DEFAULT_CHEMIN_F)))
            .andExpect(jsonPath("$.[*].codeF").value(hasItem(DEFAULT_CODE_F)));
    }

    @Test
    @Transactional
    void getFichier() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        // Get the fichier
        restFichierMockMvc
            .perform(get(ENTITY_API_URL_ID, fichier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fichier.getId().intValue()))
            .andExpect(jsonPath("$.nomF").value(DEFAULT_NOM_F))
            .andExpect(jsonPath("$.cheminF").value(DEFAULT_CHEMIN_F))
            .andExpect(jsonPath("$.codeF").value(DEFAULT_CODE_F));
    }

    @Test
    @Transactional
    void getNonExistingFichier() throws Exception {
        // Get the fichier
        restFichierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFichier() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();

        // Update the fichier
        Fichier updatedFichier = fichierRepository.findById(fichier.getId()).get();
        // Disconnect from session so that the updates on updatedFichier are not directly saved in db
        em.detach(updatedFichier);
        updatedFichier.nomF(UPDATED_NOM_F).cheminF(UPDATED_CHEMIN_F).codeF(UPDATED_CODE_F);
        FichierDTO fichierDTO = fichierMapper.toDto(updatedFichier);

        restFichierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fichierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
        Fichier testFichier = fichierList.get(fichierList.size() - 1);
        assertThat(testFichier.getNomF()).isEqualTo(UPDATED_NOM_F);
        assertThat(testFichier.getCheminF()).isEqualTo(UPDATED_CHEMIN_F);
        assertThat(testFichier.getCodeF()).isEqualTo(UPDATED_CODE_F);
    }

    @Test
    @Transactional
    void putNonExistingFichier() throws Exception {
        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();
        fichier.setId(count.incrementAndGet());

        // Create the Fichier
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fichierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFichier() throws Exception {
        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();
        fichier.setId(count.incrementAndGet());

        // Create the Fichier
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFichier() throws Exception {
        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();
        fichier.setId(count.incrementAndGet());

        // Create the Fichier
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFichierWithPatch() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();

        // Update the fichier using partial update
        Fichier partialUpdatedFichier = new Fichier();
        partialUpdatedFichier.setId(fichier.getId());

        partialUpdatedFichier.nomF(UPDATED_NOM_F).codeF(UPDATED_CODE_F);

        restFichierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichier))
            )
            .andExpect(status().isOk());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
        Fichier testFichier = fichierList.get(fichierList.size() - 1);
        assertThat(testFichier.getNomF()).isEqualTo(UPDATED_NOM_F);
        assertThat(testFichier.getCheminF()).isEqualTo(DEFAULT_CHEMIN_F);
        assertThat(testFichier.getCodeF()).isEqualTo(UPDATED_CODE_F);
    }

    @Test
    @Transactional
    void fullUpdateFichierWithPatch() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();

        // Update the fichier using partial update
        Fichier partialUpdatedFichier = new Fichier();
        partialUpdatedFichier.setId(fichier.getId());

        partialUpdatedFichier.nomF(UPDATED_NOM_F).cheminF(UPDATED_CHEMIN_F).codeF(UPDATED_CODE_F);

        restFichierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichier))
            )
            .andExpect(status().isOk());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
        Fichier testFichier = fichierList.get(fichierList.size() - 1);
        assertThat(testFichier.getNomF()).isEqualTo(UPDATED_NOM_F);
        assertThat(testFichier.getCheminF()).isEqualTo(UPDATED_CHEMIN_F);
        assertThat(testFichier.getCodeF()).isEqualTo(UPDATED_CODE_F);
    }

    @Test
    @Transactional
    void patchNonExistingFichier() throws Exception {
        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();
        fichier.setId(count.incrementAndGet());

        // Create the Fichier
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fichierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFichier() throws Exception {
        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();
        fichier.setId(count.incrementAndGet());

        // Create the Fichier
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFichier() throws Exception {
        int databaseSizeBeforeUpdate = fichierRepository.findAll().size();
        fichier.setId(count.incrementAndGet());

        // Create the Fichier
        FichierDTO fichierDTO = fichierMapper.toDto(fichier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fichierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Fichier in the database
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFichier() throws Exception {
        // Initialize the database
        fichierRepository.saveAndFlush(fichier);

        int databaseSizeBeforeDelete = fichierRepository.findAll().size();

        // Delete the fichier
        restFichierMockMvc
            .perform(delete(ENTITY_API_URL_ID, fichier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Fichier> fichierList = fichierRepository.findAll();
        assertThat(fichierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
