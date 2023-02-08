package stage.a2sys.gestion.service.impl;

import java.io.File;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.a2sys.gestion.domain.Projet;
import stage.a2sys.gestion.domain.Tache;
import stage.a2sys.gestion.repository.ProjetRepository;
import stage.a2sys.gestion.repository.TacheRepository;
import stage.a2sys.gestion.service.ProjetService;
import stage.a2sys.gestion.service.TacheService;
import stage.a2sys.gestion.service.dto.DossierDTO;
import stage.a2sys.gestion.service.dto.ProjetDTO;
import stage.a2sys.gestion.service.dto.TacheDTO;
import stage.a2sys.gestion.service.mapper.DossierMapper;
import stage.a2sys.gestion.service.mapper.ProjetMapper;
import stage.a2sys.gestion.service.mapper.TacheMapper;

/**
 * Service Implementation for managing {@link Projet}.
 */
@Service
@Transactional
public class ProjetServiceImpl implements ProjetService {

    private final Logger log = LoggerFactory.getLogger(ProjetServiceImpl.class);

    private final ProjetRepository projetRepository;
    private final DossierMapper dossierMapper;

    private final TacheRepository tacheRepository;

    private final TacheService tacheService;
    private final TacheMapper tacheMapper;
    private final ProjetMapper projetMapper;

    public ProjetServiceImpl(ProjetRepository projetRepository, ProjetMapper projetMapper, DossierMapper dossierMapper, TacheRepository tacheRepository, TacheServiceImpl tacheService, TacheMapper tacheMapper) {
        this.projetRepository = projetRepository;
        this.projetMapper = projetMapper;
        this.dossierMapper = dossierMapper;
        this.tacheRepository = tacheRepository;
        this.tacheService = tacheService;
        this.tacheMapper = tacheMapper;
    }

    @Override
    public ProjetDTO save(ProjetDTO projetDTO) {

        log.debug("Request to save Projet : {}", projetDTO);
        Projet projet = projetMapper.toEntity(projetDTO);
        projet.setCheminP("C:/Users/HP/Desktop/Projets/"+projet.getIntitule());
        projet = projetRepository.save(projet);

        File dir1 = new File("C:/Users/HP/Desktop/Projets/"+projet.getIntitule());
        dir1.mkdir();
        return projetMapper.toDto(projet);
    }



    @Override
    public ProjetDTO update(ProjetDTO projetDTO) {
        log.debug("Request to save Projet : {}", projetDTO);
        Projet projet = projetMapper.toEntity(projetDTO);
        projet = projetRepository.save(projet);
        return projetMapper.toDto(projet);
    }

    @Override
    public Optional<ProjetDTO> partialUpdate(ProjetDTO projetDTO) {
        log.debug("Request to partially update Projet : {}", projetDTO);

        return projetRepository
            .findById(projetDTO.getId())
            .map(existingProjet -> {
                projetMapper.partialUpdate(existingProjet, projetDTO);

                return existingProjet;
            })
            .map(projetRepository::save)
            .map(projetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Projets");
        return projetRepository.findAll(pageable).map(projetMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjetDTO> findOne(Long id) {
        log.debug("Request to get Projet : {}", id);
        return projetRepository.findById(id).map(projetMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Projet : {}", id);
        projetRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<DossierDTO> AvoirDossierServiceImpl (Long id) {
        return dossierMapper.toDto(projetRepository.AvoirDossierReposi(id));
    }

    @Transactional(readOnly = true)
    public List<TacheDTO> AvoirTacheServiceImpl (Long id) {
        return tacheMapper.toDto(projetRepository.AvoirtacheReposi(id));
    }

    @Transactional(readOnly = true)
    public List<TacheDTO> AvoirProjetServiceImpl (Long id) {
        return tacheMapper.toDto(projetRepository.AvoirprojetReposi(id));
    }


}
