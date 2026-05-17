package lt.esdc.controller;

import jakarta.validation.Valid;
import lt.esdc.model.Alchemist;
import lt.esdc.model.DataResponse;
import lt.esdc.service.AlchemistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alchemists")
public class AlchemistController {

    private final AlchemistService service;

    public AlchemistController(AlchemistService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<DataResponse<List<Alchemist>>> getAllAlchemists() {
        List<Alchemist> alchemists = service.getAllAlchemists();
        return ResponseEntity.ok(new DataResponse<>(alchemists, alchemists.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Alchemist>> getAlchemist(@PathVariable String id) {
        Alchemist alchemist = service.getAlchemistById(id);
        return ResponseEntity.ok(new DataResponse<>(alchemist, 1));
    }

    @PostMapping
    public ResponseEntity<DataResponse<Alchemist>> createAlchemist(@RequestBody @Valid Alchemist alchemist) {
        Alchemist created = service.createAlchemist(alchemist);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse<>(created, 1));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<Alchemist>> updateAlchemist(
            @PathVariable String id,
            @RequestBody @Valid Alchemist alchemist) {
        Alchemist updated = service.updateAlchemist(id, alchemist);
        return ResponseEntity.ok(new DataResponse<>(updated, 1));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<String>> deleteAlchemist(@PathVariable String id) {
        service.deleteAlchemist(id);
        return ResponseEntity.ok(new DataResponse<>("Alchemist " + id + " deleted successfully.", 1));
    }
}