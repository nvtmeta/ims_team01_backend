package fsa.training.ims_team01.model.dto.userDto;

import fsa.training.ims_team01.enums.userEnum.UserStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserListDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private UserStatusEnum status;
    private List<String> roles; // <>

    public UserListDto(Long id, String username, String email, String phone, UserStatusEnum status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }
}
