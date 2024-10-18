package fsa.training.ims_team01.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {
    private String newPassword;
}
