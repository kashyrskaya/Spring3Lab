package lt.esdc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "alchemists")
public class Alchemist {

    @Id
    @NotBlank(message = "Alchemist ID is required")
    private String id; // This will match the X-Alchemist-ID header

    @NotBlank(message = "Alchemist name is required")
    private String name;

    @Min(value = 1, message = "Experience level must be at least 1")
    @Column(name = "experience_level")
    private int experienceLevel;

    private String specialty; // e.g., "Healing", "Poisons", "Transmutation"

    @OneToMany(mappedBy = "alchemist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Potion> potions = new ArrayList<>();
}