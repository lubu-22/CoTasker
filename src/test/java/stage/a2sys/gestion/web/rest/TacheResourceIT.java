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
import stage.a2sys.gestion.domain.Tache;
import stage.a2sys.gestion.domain.enumeration.Role;
import stage.a2sys.gestion.repository.TacheRepository;
import stage.a2sys.gestion.service.dto.TacheDTO;
import stage.a2sys.gestion.service.mapper.TacheMapper;

/**
 * Integration tests for the {@link TacheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TacheResourceIT {

    private static final Role DEFAULT_ROLE = Role.CHEF_DE_PROJET;
    private static final Role UPDATED_ROLE = Role.DEVELOPPEUR;

    private static final String DEFAULT_DESCRIPTIONTACHE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTIONTACHE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/taches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private TacheMapper tacheMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTacheMockMvc;

    private Tache tache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tache createEntity(EntityManager em) {
        Tache tache = new Tache().role(DEFAULT_ROLE).descriptiontache(DEFAULT_DESCRIPTIONTACHE);
        return tache;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tache createUpdatedEntity(EntityManager em) {
        Tache tache = new Tache().role(UPDATED_ROLE).descriptiontache(UPDATED_DESCRIPTIONTACHE);
        return tache;
    }

    @BeforeEach
    public void initTest() {
        tache = createEntity(em);
    }

    @Test
    @Transactional
    void createTache() throws Exception {
        int databaseSizeBeforeCreate = tacheRepository.findAll().size();
        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);
        restTacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tacheDTO)))
            .andExpect(status().isCreated());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeCreate + 1);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRole()).isEqualTo(DEFAULT_ROLE);
        assertThat(testTache.getDescriptiontache()).isEqualTo(DEFAULT_DESCRIPTIONTACHE);
    }

    @Test
    @Transactional
    void createTacheWithExistingId() throws Exception {
        // Create the Tache with an existing ID
        tache.setId(1L);
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        int databaseSizeBeforeCreate = tacheRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tacheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTaches() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get all the tacheList
        restTacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tache.getId().intValue())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].descriptiontache").value(hasItem(DEFAULT_DESCRIPTIONTACHE)));
    }

    @Test
    @Transactional
    void getTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        // Get the tache
        restTacheMockMvc
            .perform(get(ENTITY_API_URL_ID, tache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tache.getId().intValue()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.descriptiontache").value(DEFAULT_DESCRIPTIONTACHE));
    }

    @Test
    @Transactional
    void getNonExistingTache() throws Exception {
        // Get the tache
        restTacheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache
        Tache updatedTache = tacheRepository.findById(tache.getId()).get();
        // Disconnect from session so that the updates on updatedTache are not directly saved in db
        em.detach(updatedTache);
        updatedTache.role(UPDATED_ROLE).descriptiontache(UPDATED_DESCRIPTIONTACHE);
        TacheDTO tacheDTO = tacheMapper.toDto(updatedTache);

        restTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tacheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTache.getDescriptiontache()).isEqualTo(UPDATED_DESCRIPTIONTACHE);
    }

    @Test
    @Transactional
    void putNonExistingTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tacheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTacheWithPatch() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache using partial update
        Tache partialUpdatedTache = new Tache();
        partialUpdatedTache.setId(tache.getId());

        partialUpdatedTache.role(UPDATED_ROLE).descriptiontache(UPDATED_DESCRIPTIONTACHE);

        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTache.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTache))
            )
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTache.getDescriptiontache()).isEqualTo(UPDATED_DESCRIPTIONTACHE);
    }

    @Test
    @Transactional
    void fullUpdateTacheWithPatch() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();

        // Update the tache using partial update
        Tache partialUpdatedTache = new Tache();
        partialUpdatedTache.setId(tache.getId());

        partialUpdatedTache.role(UPDATED_ROLE).descriptiontache(UPDATED_DESCRIPTIONTACHE);

        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTache.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTache))
            )
            .andExpect(status().isOk());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
        Tache testTache = tacheList.get(tacheList.size() - 1);
        assertThat(testTache.getRole()).isEqualTo(UPDATED_ROLE);
        assertThat(testTache.getDescriptiontache()).isEqualTo(UPDATED_DESCRIPTIONTACHE);
    }

    @Test
    @Transactional
    void patchNonExistingTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tacheDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTache() throws Exception {
        int databaseSizeBeforeUpdate = tacheRepository.findAll().size();
        tache.setId(count.incrementAndGet());

        // Create the Tache
        TacheDTO tacheDTO = tacheMapper.toDto(tache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTacheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tache in the database
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTache() throws Exception {
        // Initialize the database
        tacheRepository.saveAndFlush(tache);

        int databaseSizeBeforeDelete = tacheRepository.findAll().size();

        // Delete the tache
        restTacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, tache.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tache> tacheList = tacheRepository.findAll();
        assertThat(tacheList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
