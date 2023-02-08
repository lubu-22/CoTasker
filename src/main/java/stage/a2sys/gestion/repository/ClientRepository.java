package stage.a2sys.gestion.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import stage.a2sys.gestion.domain.Client;

/**
 * Spring Data SQL repository for the Client entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {}
