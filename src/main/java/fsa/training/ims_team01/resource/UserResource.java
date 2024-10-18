package fsa.training.ims_team01.resource;

import fsa.training.ims_team01.enums.userEnum.UserRoleEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.userDto.UserDetailDto;
import fsa.training.ims_team01.model.dto.userDto.UserFormDto;
import fsa.training.ims_team01.model.dto.userDto.UserListDto;
import fsa.training.ims_team01.request.ChangePasswordRequest;
import fsa.training.ims_team01.request.ResetPassword;
import fsa.training.ims_team01.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserListDto> getUsers(
            @PageableDefault Pageable pageable,
            @RequestParam(name = "role", required = false) String role,
            @RequestParam(name = "q", required = false) String q
    ) {
        return userService.getUserList(pageable, q, role);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Optional<UserDetailDto>> getUserById(@PathVariable Long id) {
        Optional<UserDetailDto> user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createUser(@RequestBody @Valid UserFormDto userFormDto) {
        Long id = userService.createUser(userFormDto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody @Valid UserFormDto userFormDto) {
        String message = userService.updateUser(id, userFormDto);
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> toggleActiveUser(@PathVariable Long id) {
        String message = userService.toggleActiveUser(id);
        return ResponseEntity.ok(message);
    }


    //    validate username and email unique
    @GetMapping("/isUserExisted")
    @PreAuthorize("hasRole('ADMIN')")
    public boolean validateUser(@RequestParam("username") String username, @RequestParam("email") String email) {
        return userService.isUserExistedByEmailOrUsername(email, username);
    }

    //    list of user role recruiter
    @GetMapping("/role/recruiter")
    public Page<DropdownDto> getRecruiterList(@PageableDefault Pageable pageable) {
        return userService.getRecruiterList(pageable);
    }

    @GetMapping("/role/{role}")
    public Page<DropdownDto> getUserListByRole(@PageableDefault Pageable pageable, @PathVariable String role, @RequestParam(name = "q", required = false) String q) {
        return userService.getUserListByRole(pageable,role,q);
    }

    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }


}
