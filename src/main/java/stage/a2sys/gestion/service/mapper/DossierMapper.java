package stage.a2sys.gestion.service.mapper;

import org.mapstruct.*;
import stage.a2sys.gestion.domain.Dossier;
import stage.a2sys.gestion.domain.Fichier;
import stage.a2sys.gestion.domain.Projet;
import stage.a2sys.gestion.service.dto.DossierDTO;
import stage.a2sys.gestion.service.dto.FichierDTO;
import stage.a2sys.gestion.service.dto.ProjetDTO;

/**
 * Mapper for the entity {@link Dossier} and its DTO {@link DossierDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierMapper extends EntityMapper<DossierDTO, Dossier> {

    @Mapping(target = "projet", source = "projet", qualifiedByName = "projetId")
    DossierDTO toDto(Dossier s);

    @Named("projetId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjetDTO toDtoProjetId(Projet projet);

}
