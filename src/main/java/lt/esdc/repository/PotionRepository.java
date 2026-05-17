package lt.esdc.repository;

import lt.esdc.model.Potion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PotionRepository extends JpaRepository<Potion, String> {

    @Query("SELECT p FROM Potion p WHERE p.powerLevel >= :minPower")
    List<Potion> findPowerfulPotions(@Param("minPower") int minPower);

    Page<Potion> findByAlchemist_Id(String alchemistId, Pageable pageable);
}