package stage.a2sys.gestion.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stage.a2sys.gestion.domain.Dossier;
import stage.a2sys.gestion.domain.Fichier;

/**
 * Spring Data SQL repository for the Fichier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FichierRepository extends JpaRepository<Fichier, Long> {



}
