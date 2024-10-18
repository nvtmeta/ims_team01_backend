package fsa.training.ims_team01.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DropdownDto {
    private Long value;

    public DropdownDto(Long value, String label) {
        this.value = value;
        this.label = label;
    }

    private String label;

    public DropdownDto(Long value) {
        this.value = value;
    }
}
