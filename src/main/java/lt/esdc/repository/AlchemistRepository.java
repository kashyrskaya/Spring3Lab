package lt.esdc.repository;

import lt.esdc.model.Alchemist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlchemistRepository extends JpaRepository<Alchemist, String> {
}