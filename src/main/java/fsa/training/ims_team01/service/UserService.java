package fsa.training.ims_team01.service;

import fsa.training.ims_team01.enums.userEnum.UserRoleEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.dto.userDto.UserAuthDto;
import fsa.training.ims_team01.model.dto.userDto.UserDetailDto;
import fsa.training.ims_team01.model.dto.userDto.UserFormDto;
import fsa.training.ims_team01.model.dto.userDto.UserListDto;
import fsa.training.ims_team01.request.ChangePasswordRequest;
import fsa.training.ims_team01.request.ResetPassword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.Optional;

public interface UserService {

    // list user
    Page<UserListDto> getUserList(Pageable pageable, String q, String role);

    //create user
    Long createUser(UserFormDto userCreateDto);

    //    getUserById
    Optional<UserDetailDto> getUserById(Long id);

    //    updateUser
    String updateUser(Long id, UserFormDto userFormDto);

    String toggleActiveUser(Long id);

    //    isUserExistedByEmailOrUsername
    boolean isUserExistedByEmailOrUsername(String email, String username);

    //    getRecruiterList
    Page<DropdownDto> getRecruiterList(Pageable pageable);

    Page<DropdownDto> getUserListByRole(Pageable pageable, String role, String q);

    Optional<UserAuthDto> findByUsername(String username);

    public void changePassword(ChangePasswordRequest request, Principal connectedUser);

//    get email user
    Optional<EmailSendDto> getEmailDtoUser(Long userId);

    // email user existed
    boolean isEmailExisted(String email);

}
