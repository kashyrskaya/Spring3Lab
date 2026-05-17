package lt.esdc.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lt.esdc.exception.RandomExplosionException;
import lt.esdc.model.DataResponse;
import lt.esdc.model.Potion;
import lt.esdc.service.PotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/potions")
// Requirement: Use @Validated at the controller level to validate path variables and query params
@Validated 
public class PotionController {

    private final PotionService service;

    public PotionController(PotionService service) {
        this.service = service;
    }

    // Requirement: GET /rates -> GET /potions
    // Requirement: Pass parameters via query param
    // Requirement: Pass parameters via header
    // Requirement: Wrap in response entity AND a response object with data and count
    @GetMapping
    public ResponseEntity<DataResponse<List<Potion>>> getAllPotions(
            @RequestParam(required = false, defaultValue = "0") int minPower,
            @RequestHeader(value = "Alchemist-ID", required = false) String alchemistId) {
        
        List<Potion> potions;
        
        if (minPower > 0) {
            potions = service.getPowerfulPotions(minPower);
        } else {
            potions = service.getAllPotions();
        }
        
        if (alchemistId != null && !alchemistId.isEmpty()) {
            potions = potions.stream().filter(p -> alchemistId.equals(p.getAlchemistId())).toList();
        }

        return ResponseEntity.ok(new DataResponse<>(potions, potions.size()));
    }

    // Extra Task: Демонстрация пагинации
    @GetMapping("/paged")
    public ResponseEntity<DataResponse<List<Potion>>> getPotionsPaged(
            @RequestHeader(value = "Alchemist-ID") String alchemistId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {
        
        Page<Potion> potionPage = service.getPotionsByAlchemistPaged(alchemistId, PageRequest.of(page, size));
        return ResponseEntity.ok(new DataResponse<>(potionPage.getContent(), (int) potionPage.getTotalElements()));
    }

    // Requirement: GET /rates/{currency} -> GET /potions/{code}
    // Requirement: Pass parameter via path variable
    @GetMapping("/{code}")
    public ResponseEntity<DataResponse<Potion>> getPotion(
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$", message = "Code must be 3 uppercase letters") String code) {
        Potion potion = service.getPotionByCode(code);
        return ResponseEntity.ok(new DataResponse<>(potion, 1));
    }

    // Requirement: POST /rates -> POST /potions
    // Requirement: Validation on incoming data (@Valid)
    // Requirement: Pass parameter via request body
    @PostMapping
    public ResponseEntity<DataResponse<Potion>> createPotion(
            @RequestHeader(value = "Alchemist-ID") String alchemistId,
            @RequestBody @Valid Potion potion) {
        Potion created = service.createPotion(potion, alchemistId);
        return ResponseEntity.status(HttpStatus.CREATED).body(new DataResponse<>(created, 1));
    }

    // Requirement: PUT /rates/{currency} -> PUT /potions/{code}
    @PutMapping("/{code}")
    public ResponseEntity<DataResponse<Potion>> updatePotion(
            @RequestHeader(value = "Alchemist-ID") String alchemistId,
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code,
            @RequestBody @Valid Potion potion) {
        Potion updated = service.updatePotion(code, potion, alchemistId);
        return ResponseEntity.ok(new DataResponse<>(updated, 1));
    }

    // Requirement: DELETE /rates/{currency} -> DELETE /potions/{code}
    @DeleteMapping("/{code}")
    public ResponseEntity<DataResponse<String>> deletePotion(
            @RequestHeader(value = "Alchemist-ID") String alchemistId,
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code) {
        service.deletePotion(code, alchemistId);
        return ResponseEntity.ok(new DataResponse<>("Potion " + code + " deleted successfully.", 1));
    }

    // Requirement: Handle the custom random exception in the controller using @ExceptionHandler
    @ExceptionHandler(RandomExplosionException.class)
    public ResponseEntity<String> handleRandomExplosion(RandomExplosionException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}