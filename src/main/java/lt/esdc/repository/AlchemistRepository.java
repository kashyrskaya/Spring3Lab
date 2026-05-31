package lt.esdc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lt.esdc.model.Alchemist;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlchemistRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Alchemist> findAll() {
        return entityManager.createQuery("SELECT a FROM Alchemist a", Alchemist.class).getResultList();
    }

    public Optional<Alchemist> findById(String id) {
        return Optional.ofNullable(entityManager.find(Alchemist.class, id));
    }

    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    public Alchemist save(Alchemist alchemist) {
        if (entityManager.find(Alchemist.class, alchemist.getId()) == null) {
            entityManager.persist(alchemist);
            return alchemist;
        } else {
            return entityManager.merge(alchemist);
        }
    }

    public void deleteById(String id) {
        findById(id).ifPresent(alchemist -> entityManager.remove(alchemist));
    }
}