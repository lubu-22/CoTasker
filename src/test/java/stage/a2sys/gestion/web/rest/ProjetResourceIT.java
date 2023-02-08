package stage.a2sys.gestion.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
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
import stage.a2sys.gestion.domain.Projet;
import stage.a2sys.gestion.domain.enumeration.Etape;
import stage.a2sys.gestion.domain.enumeration.TypeProjet;
import stage.a2sys.gestion.repository.ProjetRepository;
import stage.a2sys.gestion.service.dto.ProjetDTO;
import stage.a2sys.gestion.service.mapper.ProjetMapper;

/**
 * Integration tests for the {@link ProjetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProjetResourceIT {

    private static final String DEFAULT_INTITULE = "AAAAAAAAAA";
    private static final String UPDATED_INTITULE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_PROJET = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_PROJET = "BBBBBBBBBB";

    private static final TypeProjet DEFAULT_TYPE = TypeProjet.FORMATION;
    private static final TypeProjet UPDATED_TYPE = TypeProjet.DEVELOPPEMENT;

    private static final Etape DEFAULT_ETAPE = Etape.EN_ATTENDE;
    private static final Etape UPDATED_ETAPE = Etape.NON_RETENU;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/projets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private ProjetMapper projetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProjetMockMvc;

    private Projet projet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projet createEntity(EntityManager em) {
        Projet projet = new Projet()
            .intitule(DEFAULT_INTITULE)
            .descriptionProjet(DEFAULT_DESCRIPTION_PROJET)
            .type(DEFAULT_TYPE)
            .etape(DEFAULT_ETAPE)
            .dateCreation(DEFAULT_DATE_CREATION)
            .dateFin(DEFAULT_DATE_FIN);
        return projet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Projet createUpdatedEntity(EntityManager em) {
        Projet projet = new Projet()
            .intitule(UPDATED_INTITULE)
            .descriptionProjet(UPDATED_DESCRIPTION_PROJET)
            .type(UPDATED_TYPE)
            .etape(UPDATED_ETAPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateFin(UPDATED_DATE_FIN);
        return projet;
    }

    @BeforeEach
    public void initTest() {
        projet = createEntity(em);
    }

    @Test
    @Transactional
    void createProjet() throws Exception {
        int databaseSizeBeforeCreate = projetRepository.findAll().size();
        // Create the Projet
        ProjetDTO projetDTO = projetMapper.toDto(projet);
        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projetDTO)))
            .andExpect(status().isCreated());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate + 1);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testProjet.getDescriptionProjet()).isEqualTo(DEFAULT_DESCRIPTION_PROJET);
        assertThat(testProjet.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProjet.getEtape()).isEqualTo(DEFAULT_ETAPE);
        assertThat(testProjet.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testProjet.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createProjetWithExistingId() throws Exception {
        // Create the Projet with an existing ID
        projet.setId(1L);
        ProjetDTO projetDTO = projetMapper.toDto(projet);

        int databaseSizeBeforeCreate = projetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProjets() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get all the projetList
        restProjetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projet.getId().intValue())))
            .andExpect(jsonPath("$.[*].intitule").value(hasItem(DEFAULT_INTITULE)))
            .andExpect(jsonPath("$.[*].descriptionProjet").value(hasItem(DEFAULT_DESCRIPTION_PROJET)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].etape").value(hasItem(DEFAULT_ETAPE.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        // Get the projet
        restProjetMockMvc
            .perform(get(ENTITY_API_URL_ID, projet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(projet.getId().intValue()))
            .andExpect(jsonPath("$.intitule").value(DEFAULT_INTITULE))
            .andExpect(jsonPath("$.descriptionProjet").value(DEFAULT_DESCRIPTION_PROJET))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.etape").value(DEFAULT_ETAPE.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProjet() throws Exception {
        // Get the projet
        restProjetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet
        Projet updatedProjet = projetRepository.findById(projet.getId()).get();
        // Disconnect from session so that the updates on updatedProjet are not directly saved in db
        em.detach(updatedProjet);
        updatedProjet
            .intitule(UPDATED_INTITULE)
            .descriptionProjet(UPDATED_DESCRIPTION_PROJET)
            .type(UPDATED_TYPE)
            .etape(UPDATED_ETAPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateFin(UPDATED_DATE_FIN);
        ProjetDTO projetDTO = projetMapper.toDto(updatedProjet);

        restProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testProjet.getDescriptionProjet()).isEqualTo(UPDATED_DESCRIPTION_PROJET);
        assertThat(testProjet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjet.getEtape()).isEqualTo(UPDATED_ETAPE);
        assertThat(testProjet.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testProjet.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // Create the Projet
        ProjetDTO projetDTO = projetMapper.toDto(projet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, projetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // Create the Projet
        ProjetDTO projetDTO = projetMapper.toDto(projet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(projetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // Create the Projet
        ProjetDTO projetDTO = projetMapper.toDto(projet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(projetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProjetWithPatch() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet using partial update
        Projet partialUpdatedProjet = new Projet();
        partialUpdatedProjet.setId(projet.getId());

        partialUpdatedProjet.type(UPDATED_TYPE);

        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjet))
            )
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getIntitule()).isEqualTo(DEFAULT_INTITULE);
        assertThat(testProjet.getDescriptionProjet()).isEqualTo(DEFAULT_DESCRIPTION_PROJET);
        assertThat(testProjet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjet.getEtape()).isEqualTo(DEFAULT_ETAPE);
        assertThat(testProjet.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testProjet.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateProjetWithPatch() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeUpdate = projetRepository.findAll().size();

        // Update the projet using partial update
        Projet partialUpdatedProjet = new Projet();
        partialUpdatedProjet.setId(projet.getId());

        partialUpdatedProjet
            .intitule(UPDATED_INTITULE)
            .descriptionProjet(UPDATED_DESCRIPTION_PROJET)
            .type(UPDATED_TYPE)
            .etape(UPDATED_ETAPE)
            .dateCreation(UPDATED_DATE_CREATION)
            .dateFin(UPDATED_DATE_FIN);

        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProjet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProjet))
            )
            .andExpect(status().isOk());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
        Projet testProjet = projetList.get(projetList.size() - 1);
        assertThat(testProjet.getIntitule()).isEqualTo(UPDATED_INTITULE);
        assertThat(testProjet.getDescriptionProjet()).isEqualTo(UPDATED_DESCRIPTION_PROJET);
        assertThat(testProjet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProjet.getEtape()).isEqualTo(UPDATED_ETAPE);
        assertThat(testProjet.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testProjet.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // Create the Projet
        ProjetDTO projetDTO = projetMapper.toDto(projet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, projetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // Create the Projet
        ProjetDTO projetDTO = projetMapper.toDto(projet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(projetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProjet() throws Exception {
        int databaseSizeBeforeUpdate = projetRepository.findAll().size();
        projet.setId(count.incrementAndGet());

        // Create the Projet
        ProjetDTO projetDTO = projetMapper.toDto(projet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProjetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(projetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Projet in the database
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProjet() throws Exception {
        // Initialize the database
        projetRepository.saveAndFlush(projet);

        int databaseSizeBeforeDelete = projetRepository.findAll().size();

        // Delete the projet
        restProjetMockMvc
            .perform(delete(ENTITY_API_URL_ID, projet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Projet> projetList = projetRepository.findAll();
        assertThat(projetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
