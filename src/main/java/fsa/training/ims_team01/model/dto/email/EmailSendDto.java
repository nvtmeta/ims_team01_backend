package fsa.training.ims_team01.model.dto.email;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmailSendDto {

    private String email;
    private String fullName;

    public EmailSendDto(String email, String fullName) {
        this.email = email;
        this.fullName = fullName;
    }
}
