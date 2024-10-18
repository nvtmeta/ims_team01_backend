package fsa.training.ims_team01.model.dto.userDto;

import fsa.training.ims_team01.enums.userEnum.GenderUserEnum;
import fsa.training.ims_team01.enums.userEnum.UserStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserFormDto {

    @NotNull(message = "{ME002.message}")
    private String fullName;

    private LocalDate dob;

    @Email(message = "{ME009.message}")
    @NotNull(message = "{ME002.message}")
    private String email;

    private String address;

    private String phone;

    @NotNull(message = "{ME002.message}")
    private List<DropdownDto> roles;

    private String note;

    @NotNull(message = "{ME002.message}")
    private GenderUserEnum gender;

    @NotNull(message = "{ME002.message}")
    private UserStatusEnum status = UserStatusEnum.ACTIVE;

    @NotNull(message = "{ME002.message}")
    private DropdownDto department;

    private Boolean deleted = false;
}
