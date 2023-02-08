package stage.a2sys.gestion.service.mapper;

import org.mapstruct.*;
import stage.a2sys.gestion.domain.Dossier;
import stage.a2sys.gestion.domain.Projet;
import stage.a2sys.gestion.domain.Tache;
import stage.a2sys.gestion.domain.User;
import stage.a2sys.gestion.service.dto.DossierDTO;
import stage.a2sys.gestion.service.dto.ProjetDTO;
import stage.a2sys.gestion.service.dto.TacheDTO;
import stage.a2sys.gestion.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Tache} and its DTO {@link TacheDTO}.
 */
@Mapper(componentModel = "spring")
public interface TacheMapper extends EntityMapper<TacheDTO, Tache> {


}
