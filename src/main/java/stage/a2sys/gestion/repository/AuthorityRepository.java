package stage.a2sys.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.a2sys.gestion.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
