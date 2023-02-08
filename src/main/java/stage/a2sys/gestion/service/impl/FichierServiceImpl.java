package stage.a2sys.gestion.service.impl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.shaded.org.apache.commons.io.FilenameUtils;
import stage.a2sys.gestion.domain.Dossier;
import stage.a2sys.gestion.domain.Fichier;
import stage.a2sys.gestion.repository.DossierRepository;
import stage.a2sys.gestion.repository.FichierRepository;
import stage.a2sys.gestion.service.FichierService;
import stage.a2sys.gestion.service.dto.FichierDTO;
import stage.a2sys.gestion.service.mapper.FichierMapper;

/**
 * Service Implementation for managing {@link Fichier}.
 */
@Service
@Transactional
public class FichierServiceImpl implements FichierService {


    private final Logger log = LoggerFactory.getLogger(FichierServiceImpl.class);

    private final FichierRepository fichierRepository;
    private final DossierRepository dossierRepository;

    private final FichierMapper fichierMapper;

    public FichierServiceImpl(FichierRepository fichierRepository, DossierRepository dossierRepository, FichierMapper fichierMapper) {
        this.fichierRepository = fichierRepository;
        this.dossierRepository = dossierRepository;
        this.fichierMapper = fichierMapper;
    }

    @Override
    public FichierDTO save(FichierDTO fichierDTO) {
        log.debug("Request to save Fichier : {}", fichierDTO);

        Fichier fichier = fichierMapper.toEntity(fichierDTO);
        fichier.cheminF(fichier.getDossier().getCheminD()+fichier.getNomF());
        log.debug(fichierDTO.getDossier().getCheminD());
        fichier = fichierRepository.save(fichier);
        return fichierMapper.toDto(fichier);
    }

    @Override
    public int savefichier(MultipartFile file, Long id) throws IOException {

            Optional<Dossier> dossierOpt = dossierRepository.findById(id);
            log.debug("patiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii {}",dossierOpt);

            if (dossierOpt.isPresent())
            {
                Dossier dossier = dossierOpt.get();
                File directory = new File(dossier.getCheminD());
                String route = null;
                String fileextension= FilenameUtils.getExtension(file.getOriginalFilename());
                String nom = file.getOriginalFilename();
                try {

                byte[] bytes = file.getBytes();
                    assert fileextension != null;
                    route = dossier.getCheminD().concat(nom);//.concat(".").concat(fileextension);

                Path root = Paths.get(route);
                log.debug(dossier.getCheminD());
                Files.write(root.toAbsolutePath(),bytes);
            }catch (IOException e){
                    log.debug("pas passer");
                }
            }
            return 0;
        }


    @Transactional(readOnly = true)
    public byte [] avoirunfichier (Long id)  {
        Optional<Fichier> fichierOpt = fichierRepository.findById(id);
        Fichier fichier = fichierOpt.get();
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


    @Override
    public FichierDTO update(FichierDTO fichierDTO) {
        log.debug("Request to save Fichier : {}", fichierDTO);
        Fichier fichier = fichierMapper.toEntity(fichierDTO);
        fichier = fichierRepository.save(fichier);
        return fichierMapper.toDto(fichier);
    }

    @Override
    public Optional<FichierDTO> partialUpdate(FichierDTO fichierDTO) {
        log.debug("Request to partially update Fichier : {}", fichierDTO);

        return fichierRepository
            .findById(fichierDTO.getId())
            .map(existingFichier -> {
                fichierMapper.partialUpdate(existingFichier, fichierDTO);

                return existingFichier;
            })
            .map(fichierRepository::save)
            .map(fichierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FichierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Fichiers");
        return fichierRepository.findAll(pageable).map(fichierMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FichierDTO> findOne(Long id) {
        log.debug("Request to get Fichier : {}", id);
        return fichierRepository.findById(id).map(fichierMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fichier : {}", id);
        fichierRepository.deleteById(id);
    }
}
