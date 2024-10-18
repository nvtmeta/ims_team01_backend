package fsa.training.ims_team01.model.dto.userDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fsa.training.ims_team01.enums.userEnum.GenderUserEnum;
import fsa.training.ims_team01.enums.userEnum.UserStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailDto {
    private String fullName;
    private String email;

    private LocalDate dob;

    private String address;
    private String phone;
    private UserStatusEnum status;
    private GenderUserEnum gender;
    private DropdownDto department;
    private List<DropdownDto> roles; // <>

    public UserDetailDto(String fullName, String email, LocalDate dob, String address,
                         String phone, UserStatusEnum status, GenderUserEnum gender,
                         String note) {
        this.fullName = fullName;
        this.email = email;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.status = status;
        this.gender = gender;
        this.note = note;
    }

    private String note;
}
