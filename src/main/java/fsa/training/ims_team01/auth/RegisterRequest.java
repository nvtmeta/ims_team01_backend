package fsa.training.ims_team01.auth;

import fsa.training.ims_team01.model.dto.DropdownDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String fullName;
    private String email;
    private String password;
    private List<DropdownDto> roles;
}
