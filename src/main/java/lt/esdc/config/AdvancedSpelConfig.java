package lt.esdc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.Getter;

@Component
@Getter
public class AdvancedSpelConfig {

    // Bonus: SpEL expression that extracts a property value, parses it, and performs math (multiplies by 10)
    @Value("#{new Double('${potion.brewing.base-multiplier:1.0}') * 10}")
    private double adjustedMultiplier;

    // Bonus: SpEL expression that reads a string property, converts it to uppercase, 
    // and evaluates a boolean condition directly inside the annotation
    @Value("#{'${potion.brewing.active-profile:default}'.toUpperCase() == 'DEVELOPMENT'}")
    private boolean isDevEnvironment;

}