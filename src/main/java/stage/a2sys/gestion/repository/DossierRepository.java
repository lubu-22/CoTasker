package stage.a2sys.gestion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stage.a2sys.gestion.domain.Dossier;
import stage.a2sys.gestion.domain.Fichier;

/**
 * Spring Data SQL repository for the Dossier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long> {

    @Query(value = "SELECT f FROM Fichier f WHERE f.dossier.id = :x")
    List<Fichier> avoirfichierreposi (@Param("x") Long x ) ;

    @Query(value = "SELECT f FROM Fichier f WHERE f.id = :x")
    Fichier avoirunfichierreposi (@Param("x") Long x ) ;

}

