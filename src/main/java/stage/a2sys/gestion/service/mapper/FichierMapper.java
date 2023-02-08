package stage.a2sys.gestion.service.mapper;

import org.mapstruct.*;
import stage.a2sys.gestion.domain.Dossier;
import stage.a2sys.gestion.domain.Fichier;
import stage.a2sys.gestion.service.dto.DossierDTO;
import stage.a2sys.gestion.service.dto.FichierDTO;

/**
 * Mapper for the entity {@link Fichier} and its DTO {@link FichierDTO}.
 */
@Mapper(componentModel = "spring")
public interface FichierMapper extends EntityMapper<FichierDTO, Fichier> {
    @Mapping(target = "dossier", source = "dossier", qualifiedByName = "dossierId")
    FichierDTO toDto(Fichier s);

    @Named("dossierId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DossierDTO toDtoDossierId(Dossier dossier);
}
