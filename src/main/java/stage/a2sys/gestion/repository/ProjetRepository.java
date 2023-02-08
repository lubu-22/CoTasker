package stage.a2sys.gestion.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import stage.a2sys.gestion.domain.*;

import java.util.List;

/**
 * Spring Data SQL repository for the Projet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {

    @Query(value = "SELECT d FROM Dossier d WHERE d.projet.id = :x")
     List<Dossier> AvoirDossierReposi(@Param("x") Long x) ;
    @Query(value = "SELECT t FROM Tache t WHERE t.projet.id = :x")
    List<Tache> AvoirtacheReposi(@Param("x") Long x) ;

    @Query(value = "SELECT t FROM Tache t WHERE t.user.id = :x")
    List<Tache> AvoirprojetReposi(@Param("x") Long x) ;





}
