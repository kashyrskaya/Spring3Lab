package lt.esdc.controller;

import lt.esdc.dto.fixer.FixerApiResponseDTO;
import lt.esdc.service.ExternalApiService;
import lt.esdc.service.RabbitMqProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/external")
public class ExternalApiController {

    private final ExternalApiService externalApiService;
    private final RabbitMqProducerService rabbitMqProducerService;

    public ExternalApiController(ExternalApiService externalApiService, RabbitMqProducerService rabbitMqProducerService) {
        this.externalApiService = externalApiService;
        this.rabbitMqProducerService = rabbitMqProducerService;
    }

    @GetMapping("/rates")
    public ResponseEntity<String> getLatestRatesAndSendToQueue() {
        FixerApiResponseDTO response = externalApiService.fetchLatestRates();
        if (response != null && response.isSuccess() && response.getRates() != null) {
            Double usdRate = response.getRates().get("USD");
            if (usdRate != null) {
                Map<String, Double> rateMessage = Map.of("USD", usdRate);
                rabbitMqProducerService.sendMessage(rateMessage);
                return ResponseEntity.ok("Successfully fetched USD rate and sent to queue: " + usdRate);
            }
        }
        return ResponseEntity.status(500).body("Failed to fetch rates or USD rate was not found.");
    }
}
