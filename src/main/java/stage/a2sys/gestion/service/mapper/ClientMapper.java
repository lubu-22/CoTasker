package stage.a2sys.gestion.service.mapper;

import org.mapstruct.*;
import stage.a2sys.gestion.domain.Client;
import stage.a2sys.gestion.service.dto.ClientDTO;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
