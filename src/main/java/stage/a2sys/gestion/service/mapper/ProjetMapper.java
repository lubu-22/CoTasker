package stage.a2sys.gestion.service.mapper;

import org.mapstruct.*;
import stage.a2sys.gestion.domain.Client;
import stage.a2sys.gestion.domain.Projet;
import stage.a2sys.gestion.service.dto.ClientDTO;
import stage.a2sys.gestion.service.dto.ProjetDTO;

/**
 * Mapper for the entity {@link Projet} and its DTO {@link ProjetDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjetMapper extends EntityMapper<ProjetDTO, Projet> {
    @Mapping(target = "client", source = "client", qualifiedByName = "clientId")
    ProjetDTO toDto(Projet s);

    @Named("clientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClientDTO toDtoClientId(Client client);
}
