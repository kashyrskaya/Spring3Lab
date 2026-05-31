package lt.esdc.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lt.esdc.model.Potion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PotionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public long count() {
        return entityManager.createQuery("SELECT COUNT(p) FROM Potion p", Long.class).getSingleResult();
    }

    public List<Potion> findAll() {
        return entityManager.createQuery("SELECT p FROM Potion p", Potion.class).getResultList();
    }

    public List<Potion> findPowerfulPotions(int minPower) {
        return entityManager.createQuery("SELECT p FROM Potion p WHERE p.powerLevel >= :minPower", Potion.class)
                .setParameter("minPower", minPower)
                .getResultList();
    }

    public Page<Potion> findByAlchemist_Id(String alchemistId, Pageable pageable) {
        TypedQuery<Potion> query = entityManager.createQuery(
                "SELECT p FROM Potion p WHERE p.alchemist.id = :alchemistId", Potion.class);
        query.setParameter("alchemistId", alchemistId);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        
        List<Potion> content = query.getResultList();

        Long total = entityManager.createQuery(
                "SELECT COUNT(p) FROM Potion p WHERE p.alchemist.id = :alchemistId", Long.class)
                .setParameter("alchemistId", alchemistId)
                .getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    public Optional<Potion> findById(String code) {
        return Optional.ofNullable(entityManager.find(Potion.class, code));
    }

    public boolean existsById(String code) {
        return findById(code).isPresent();
    }

    public Potion save(Potion potion) {
        if (entityManager.find(Potion.class, potion.getCode()) == null) {
            entityManager.persist(potion);
            return potion;
        } else {
            return entityManager.merge(potion);
        }
    }

    public void deleteById(String code) {
        findById(code).ifPresent(potion -> entityManager.remove(potion));
    }
}