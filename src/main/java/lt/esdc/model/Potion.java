package lt.esdc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lt.esdc.validation.MagicalWord;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "potions")
public class Potion {

    @Id
    @NotBlank(message = "Potion code is required")
    @Size(min = 3, max = 3, message = "Code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Code must consist of 3 uppercase letters")
    private String code;

    @NotBlank(message = "Potion name is required")
    private String name;

    @Min(value = 1, message = "Power level must be greater than 0")
    @Column(name = "power_level")
    private int powerLevel;

    @MagicalWord
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alchemist_id")
    @JsonIgnore
    private Alchemist alchemist;

    @JsonProperty(value = "alchemistId", access = JsonProperty.Access.READ_ONLY)
    public String getAlchemistId() {
        return alchemist != null ? alchemist.getId() : null;
    }
}