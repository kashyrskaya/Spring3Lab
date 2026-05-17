package lt.esdc.service;

import lt.esdc.exception.AlchemistAlreadyExistsException;
import lt.esdc.exception.AlchemistNotFoundException;
import lt.esdc.model.Alchemist;
import lt.esdc.repository.AlchemistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AlchemistService {

    private final AlchemistRepository repository;

    public AlchemistService(AlchemistRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Alchemist> getAllAlchemists() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Alchemist getAlchemistById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new AlchemistNotFoundException("Alchemist with ID " + id + " not found."));
    }

    public Alchemist createAlchemist(Alchemist alchemist) {
        if (repository.existsById(alchemist.getId())) {
            throw new AlchemistAlreadyExistsException("Alchemist with ID " + alchemist.getId() + " already exists!");
        }
        return repository.save(alchemist);
    }

    public Alchemist updateAlchemist(String id, Alchemist alchemist) {
        Alchemist existing = repository.findById(id)
                .orElseThrow(() -> new AlchemistNotFoundException("Cannot update: Alchemist with ID " + id + " not found."));
        
        existing.setName(alchemist.getName());
        existing.setExperienceLevel(alchemist.getExperienceLevel());
        existing.setSpecialty(alchemist.getSpecialty());
        return repository.save(existing);
    }

    public void deleteAlchemist(String id) {
        if (!repository.existsById(id)) {
            throw new AlchemistNotFoundException("Cannot delete: Alchemist with ID " + id + " not found.");
        }
        repository.deleteById(id);
    }
}