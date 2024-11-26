package stage.a2sys.gestion.service.impl;

import java.awt.Desktop;
import java.io.*;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stage.a2sys.gestion.domain.Dossier;
import stage.a2sys.gestion.domain.Fichier;
import stage.a2sys.gestion.repository.DossierRepository;
import stage.a2sys.gestion.service.DossierService;
import stage.a2sys.gestion.service.dto.DossierDTO;
import stage.a2sys.gestion.service.dto.FichierDTO;
import stage.a2sys.gestion.service.mapper.DossierMapper;
import stage.a2sys.gestion.service.mapper.FichierMapper;

/**
 * Service Implementation for managing {@link Dossier}.
 */
@Service
@Transactional
public class DossierServiceImpl implements DossierService {

    private final Logger log = LoggerFactory.getLogger(DossierServiceImpl.class);

    private final DossierRepository dossierRepository;

    private final DossierMapper dossierMapper;
    private final FichierMapper fichiermapper;

    public DossierServiceImpl(DossierRepository dossierRepository, DossierMapper dossierMapper, FichierMapper fichiermapper) {
        this.dossierRepository = dossierRepository;
        this.dossierMapper = dossierMapper;
        this.fichiermapper = fichiermapper;
    }

    @Override
    public DossierDTO save(DossierDTO dossierDTO) {
        log.debug("Request to save Dossier : {}", dossierDTO);
        Dossier dossier = dossierMapper.toEntity(dossierDTO);
        dossier = dossierRepository.save(dossier);
        return dossierMapper.toDto(dossier);
    }



    @Override
    public DossierDTO update(DossierDTO dossierDTO) {
        log.debug("Request to save Dossier : {}", dossierDTO);
        Dossier dossier = dossierMapper.toEntity(dossierDTO);
        dossier = dossierRepository.save(dossier);
        return dossierMapper.toDto(dossier);
    }

    @Override
    public Optional<DossierDTO> partialUpdate(DossierDTO dossierDTO) {
        log.debug("Request to partially update Dossier : {}", dossierDTO);

        return dossierRepository
            .findById(dossierDTO.getId())
            .map(existingDossier -> {
                dossierMapper.partialUpdate(existingDossier, dossierDTO);

                return existingDossier;
            })
            .map(dossierRepository::save)
            .map(dossierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DossierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dossiers");
        return dossierRepository.findAll(pageable).map(dossierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DossierDTO> findOne(Long id) {
        log.debug("Request to get Dossier : {}", id);
        return dossierRepository.findById(id).map(dossierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<FichierDTO>avoirfichierserviceimpl (Long id) {
        return fichiermapper.toDto(dossierRepository.avoirfichierreposi(id));
    }

    @Transactional(readOnly = true)
    public byte [] avoirunfichier (Long id)  {
        FichierDTO fichier =fichiermapper.toDto(dossierRepository.avoirunfichierreposi(id));
        try{
            File file = new File(fichier.getCheminF());
            return getFileBytes(file);
        }catch (Exception ex){
            //l'exception
        }
        return null;
    }

    public byte[] getFileBytes(File file) throws IOException {

        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1)
                ous.write(buffer, 0, read);
        }
        finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e){
                //swallow ...
            }
            try {
                if (ios != null)
                    ios.close();
            }catch (IOException e) {
                //swallow....
            }
        }return ous.toByteArray();

    }




}
