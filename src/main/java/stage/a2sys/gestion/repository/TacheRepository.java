package stage.a2sys.gestion.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stage.a2sys.gestion.domain.Fichier;
import stage.a2sys.gestion.domain.Tache;

import java.util.List;

/**
 * Spring Data SQL repository for the Tache entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TacheRepository extends JpaRepository<Tache, Long> {

}
