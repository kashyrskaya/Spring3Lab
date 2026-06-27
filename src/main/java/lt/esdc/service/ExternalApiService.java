package lt.esdc.service;

import lt.esdc.dto.fixer.FixerApiResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ExternalApiService {

    private final RestClient restClient;
    private final String apiKey;

    public ExternalApiService(RestClient restClient, @Value("${fixer.api.key}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public FixerApiResponseDTO fetchLatestRates() {
        return restClient.get()
                .uri("/latest?access_key={apiKey}", apiKey)
                .retrieve()
                .body(FixerApiResponseDTO.class);
    }
}
