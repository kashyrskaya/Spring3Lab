package lt.esdc.service;

import lt.esdc.config.PotionStoreProperties;
import lt.esdc.exception.*;
import lt.esdc.model.Alchemist;
import lt.esdc.model.Potion;
import lt.esdc.repository.PotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lt.esdc.config.AdvancedSpelConfig;

import java.util.List;

@Service
@Transactional
public class PotionService {

    private final PotionRepository repository;
    private final AlchemistService alchemistService;
    private final PotionStoreProperties storeProperties;
    private final AdvancedSpelConfig spelConfig;


    public PotionService(PotionRepository repository, AlchemistService alchemistService,
                         AdvancedSpelConfig spelConfig, PotionStoreProperties storeProperties) {
        this.repository = repository;
        this.alchemistService = alchemistService;
        this.storeProperties = storeProperties;
        this.spelConfig = spelConfig;
    }

    public List<Potion> getAllPotions() {
        // Requirement: 500 error if DB is empty when getting the list
        if (repository.count() == 0) {
            throw new EmptyCauldronException("The cauldron is empty! No potions found.");
        }
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Potion> getPowerfulPotions(int minPower) {
        return repository.findPowerfulPotions(minPower);
    }

    @Transactional(readOnly = true)
    public Page<Potion> getPotionsByAlchemistPaged(String alchemistId, Pageable pageable) {
        return repository.findByAlchemist_Id(alchemistId, pageable);
    }

    @Transactional(readOnly = true)
    public Potion getPotionByCode(String code) {
        return repository.findById(code)
                .orElseThrow(() -> new PotionNotFoundException("Potion with code " + code + " not found."));
    }

    public Potion createPotion(Potion potion, String alchemistId) {
        Alchemist alchemist = alchemistService.getAlchemistById(alchemistId);

        if (repository.count() >= storeProperties.getMaxPotions()) {
            throw new StoreIsFullException("The store is full! Cannot brew more than " + storeProperties.getMaxPotions() + " potions.");
        }
        // Requirement: 400 error when trying to create an already existing object
        if (repository.existsById(potion.getCode())) {
            throw new PotionAlreadyExistsException("Potion with code " + potion.getCode() + " already exists!");
        }

        int boostedPower = (int) ((potion.getPowerLevel()) * spelConfig.getAdjustedMultiplier());
        potion.setPowerLevel(boostedPower);
        potion.setAlchemist(alchemist);
        return repository.save(potion);
    }

    public Potion updatePotion(String code, Potion potion, String alchemistId) {
        Potion existing = repository.findById(code)
                .orElseThrow(() -> new PotionNotFoundException("Potion with code " + code + " not found."));

        if (!alchemistId.equals(existing.getAlchemistId())) {
            throw new NotYourPotionException("You cannot modify a potion that belongs to another alchemist!");
        }

        existing.setName(potion.getName());
        existing.setPowerLevel(potion.getPowerLevel());
        existing.setDescription(potion.getDescription());
        // Обновляем существующий объект, которым управляет JPA
        return repository.save(existing);
    }

    public void deletePotion(String code, String alchemistId) {
        Potion existing = repository.findById(code)
                .orElseThrow(() -> new PotionNotFoundException("Cannot delete: Potion with code " + code + " not found."));

        if (!alchemistId.equals(existing.getAlchemistId())) {
            throw new NotYourPotionException("You cannot delete a potion that belongs to another alchemist!");
        }
        
        // Requirement: 50% chance to throw "Random delete error"
        if (Math.random() < 0.5) {
            throw new RandomExplosionException("Random deletion error: The potion exploded in your hands!");
        }
        repository.deleteById(code);
    }
}