package fsa.training.ims_team01.auth;


import fsa.training.ims_team01.model.entity.Department;
import fsa.training.ims_team01.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private String username;
    private String fullName;
    private String departmentName;
    private String email;
    private Long id;
    private Set<String> roles;
}
