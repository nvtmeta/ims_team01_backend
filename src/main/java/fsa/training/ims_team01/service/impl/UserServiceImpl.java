package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.enums.userEnum.UserStatusEnum;
import fsa.training.ims_team01.exception.DuplicateException;
import fsa.training.ims_team01.exception.NotFoundException;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.dto.userDto.UserAuthDto;
import fsa.training.ims_team01.model.dto.userDto.UserDetailDto;
import fsa.training.ims_team01.model.dto.userDto.UserFormDto;
import fsa.training.ims_team01.model.dto.userDto.UserListDto;
import fsa.training.ims_team01.model.entity.Department;
import fsa.training.ims_team01.model.entity.Role;
import fsa.training.ims_team01.model.entity.User;
import fsa.training.ims_team01.repository.RoleRepository;
import fsa.training.ims_team01.repository.UserRepository;
import fsa.training.ims_team01.request.ChangePasswordRequest;
import fsa.training.ims_team01.request.ResetPassword;
import fsa.training.ims_team01.service.EmailService;
import fsa.training.ims_team01.service.UserService;
import fsa.training.ims_team01.util.EmailValidationUtils;
import fsa.training.ims_team01.util.GeneratePasswordUtil;
import fsa.training.ims_team01.util.GenerateUsernameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static fsa.training.ims_team01.service.impl.UserAuthService.getUserAuthDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    //    increment number
    private static final AtomicLong counter = new AtomicLong(1L);


    @Override
    public Page<UserListDto> getUserList(Pageable pageable, String q, String role) {
        Page<UserListDto> userListDtoPage = userRepository.findAllByDeletedFalse(pageable, q, role);
//        getAllRolesByUserId
        userListDtoPage.stream()
                .forEach(userListDto -> {
                    List<String> rolesNameList = userRepository.getAllRolesByUserId(userListDto.getId());
                    userListDto.setRoles(rolesNameList);
                });
        return userListDtoPage;

    }

    private void setDepartment(User user, DropdownDto departmentFormDto) {
        Department department = Optional.ofNullable(departmentFormDto)
                .map(dep -> new Department(dep.getValue()))
                .orElse(null);
        user.setDepartment(department);
    }

    private void setRoles(User user, List<DropdownDto> roleDtoList) {
        Set<Role> roles = roleDtoList.stream()
                .map(role -> roleRepository.findById(role.getValue()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        user.setRoles(roles);
    }

    @Override
    public Long createUser(UserFormDto userFormDto) {
        User user = new User();
        BeanUtils.copyProperties(userFormDto, user);
        if (EmailValidationUtils.isEmailExisted(user.getEmail(), userRepository)) {
            throw new DuplicateException("Email already exists");
        }

        String generatePassword = GeneratePasswordUtil.generatePassword(6);

        user.setPassword(passwordEncoder.encode(generatePassword));
        user = userRepository.save(user);

        String generatedUsername = GenerateUsernameUtil.generateUsername(userFormDto.getFullName(), user.getId());
        user.setUsername(generatedUsername);


        setDepartment(user, userFormDto.getDepartment());

        setRoles(user, userFormDto.getRoles());

        user = userRepository.save(user);

        emailService.sendEmailCreateUser(user, generatePassword);

        return user.getId();
    }


    @Override
    public Optional<UserDetailDto> getUserById(Long id) {
        Optional<UserDetailDto> userDetailDto = userRepository.getUserDetailById(id);
//        get role name
        userDetailDto.ifPresent(userDetailDto1 -> {
            List<DropdownDto> rolesNameList = userRepository.getDropdownRolesByUserId(id);
            userDetailDto1.setRoles(rolesNameList);

            Optional<DropdownDto> department = userRepository.getDepartmentNameByUserId(id);
            department.ifPresent(userDetailDto1::setDepartment);
        });


        return userDetailDto;
    }

    @Override
    public String updateUser(Long id, UserFormDto userFormDto) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            BeanUtils.copyProperties(userFormDto, user.get());

//            validate username or email exists
            if (userRepository.existsByEmailAndDeletedFalse(user.get().getEmail())) {
                // except my old email
                if (!user.get().getEmail().equals(userFormDto.getEmail())) {
                    throw new DuplicateException("Email already exists");
                }
            }

            setDepartment(user.get(), userFormDto.getDepartment());

            setRoles(user.get(), userFormDto.getRoles());

            userRepository.save(user.get());
            return "User updated successfully";
        }
        throw new NotFoundException("User not found");
    }

    @Override
    public String toggleActiveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserStatusEnum newStatus = user.getStatus() == UserStatusEnum.ACTIVE
                ? UserStatusEnum.INACTIVE
                : UserStatusEnum.ACTIVE;

        user.setStatus(newStatus);
        userRepository.save(user);

        return "User updated successfully";
    }

    @Override
    public boolean isUserExistedByEmailOrUsername(String email, String username) {
        return userRepository.existsByEmailOrUsernameAndDeletedFalse(email, username);
    }

    @Override
    public Page<DropdownDto> getRecruiterList(Pageable pageable) {
        System.out.println("getRecruiterList");
        return userRepository.getRecruiterList(pageable);
    }

    @Override
    public Page<DropdownDto> getUserListByRole(Pageable pageable, String role,String q) {
        return userRepository.getUserListByRole(pageable, role, q);
    }

    @Override
    public Optional<UserAuthDto> findByUsername(String username) {
        return getUserAuthDto(username, true, userRepository);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (UserAuthDto) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        var userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            userOptional.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(userOptional.get());
        }
    }



    @Override
    public Optional<EmailSendDto> getEmailDtoUser(Long userId) {
        return userRepository.getEmailDtoUser(userId);
    }

    @Override
    public boolean isEmailExisted(String email) {
        return userRepository.existsByEmailAndDeletedFalse(email);
    }


}
