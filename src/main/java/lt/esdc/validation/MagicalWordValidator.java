package lt.esdc.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class MagicalWordValidator implements ConstraintValidator<MagicalWord, String> {

    private static final List<String> MAGICAL_WORDS = Arrays.asList("elixir", "brew", "potion", "draught", "philter");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; 
        }
        
        String lowerCaseValue = value.toLowerCase();
        return MAGICAL_WORDS.stream().anyMatch(lowerCaseValue::contains);
    }
}