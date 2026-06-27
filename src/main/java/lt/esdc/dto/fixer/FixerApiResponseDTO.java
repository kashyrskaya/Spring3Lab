package lt.esdc.dto.fixer;

import lombok.Data;
import java.util.Map;

@Data
public class FixerApiResponseDTO {
    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;
}
