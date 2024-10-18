package fsa.training.ims_team01.auth;


import fsa.training.ims_team01.enums.TokenType;
import fsa.training.ims_team01.model.dto.userDto.UserAuthDto;
import fsa.training.ims_team01.model.entity.Department;
import fsa.training.ims_team01.model.entity.Role;
import fsa.training.ims_team01.model.entity.Token;
import fsa.training.ims_team01.model.entity.User;
import fsa.training.ims_team01.repository.RoleRepository;
import fsa.training.ims_team01.repository.TokenRepository;
import fsa.training.ims_team01.repository.UserRepository;
import fsa.training.ims_team01.request.ResetPassword;
import fsa.training.ims_team01.security.config.JwtService;
import fsa.training.ims_team01.service.EmailService;
import fsa.training.ims_team01.service.UserService;
import fsa.training.ims_team01.service.impl.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final UserService userService;
    private final UserAuthService userAuthService;
    private final EmailService emailService;

    public AuthenticationResponse register(RegisterRequest request) {
        Set<Role> roles = request.getRoles().stream()
                .map(role -> roleRepository.findById(role.getValue()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        var user = User.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);

        UserAuthDto userAuthDto = new UserAuthDto();
        BeanUtils.copyProperties(new User(), userAuthDto);

        var jwtToken = jwtService.generateToken(userAuthDto, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserAuthDto userAuthDto = userRepository.findByUsernameAndStatusInActive(request.getUsername())
                .orElseThrow();
        System.out.println("userAuthDto: " + userAuthDto);
        Optional<Set<Role>> roles = userRepository.getAllRolesByUsername(request.getUsername());

//        get department
        Optional<Department> department = userRepository.getDepartmentByUsername(request.getUsername());
        roles.ifPresent(userAuthDto::setRoles);
        var jwtToken = jwtService.generateToken(userAuthDto, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7));

        revokeAllUserTokens(userAuthDto.getId());
        saveUserToken(jwtToken, userAuthDto, TokenType.LOGIN);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .username(userAuthDto.getUsername())
                .fullName(userAuthDto.getFullName())
                .id(userAuthDto.getId())
                .email(userAuthDto.getEmail())
                .roles(userAuthDto.getRoles().stream()
                        .map(Role::getName)
                        .map(String::toUpperCase)
                        .collect(Collectors.toSet()))
                .departmentName(department.map(Department::getName).orElse(null))
                .build();
    }

    private void saveUserToken(String jwtToken, UserAuthDto userAuthDto, TokenType tokenType) {
        var token = Token.builder()
                .token(jwtToken)
                .user(new User(userAuthDto.getId()))
                .tokenType(tokenType)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Long userId) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(userId);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public String forgotPassword(String email) {
        // check email exists
        boolean isEmailExisted = userService.isEmailExisted(email);
        if (!isEmailExisted) {
            throw new RuntimeException("Email not found");
        }
        Optional<UserAuthDto> userAuthDto = UserAuthService.getUserAuthDto(email, false, userRepository); // Search by email
        System.out.println("userAuthDto: " + userAuthDto);
        // Generate JWT token
        if (userAuthDto.isPresent()) {
            var jwtToken = jwtService.generateToken(userAuthDto.get(), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7));
            System.out.println("jwtToken: " + jwtToken);
            revokeAllUserTokens(userAuthDto.get().getId());
            saveUserToken(jwtToken, userAuthDto.get(), TokenType.RESET);

            // Send email with reset password link
            emailService.sendResetPasswordEmail(email, jwtToken);
            return jwtToken;
        }
        return null;
    }


    //    isTokenValid
    public boolean isTokenValid(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> {
                    if (t.getTokenType() == TokenType.RESET) {
                        return !t.isRevoked() && !t.isExpired() && hoursSinceCreation(t) < 24;
                    } else if (t.getTokenType() == TokenType.LOGIN) {
                        return !t.isRevoked() && !t.isExpired();
                    } else {
                        return false;
                    }
                })
                .isPresent();
    }

    private long hoursSinceCreation(Token token) {
        return ChronoUnit.HOURS.between(token.getCreatedDate(), LocalDateTime.now());
    }

    public void resetPassword(ResetPassword request, Principal connectedUser) {
        var user = (UserAuthDto) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        System.out.println("userGetId" + user.getId());
        System.out.println("request" + request);
        var userOptional = userRepository.findById(user.getId());
        if (userOptional.isPresent()) {
            userOptional.get().setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(userOptional.get());

            // revoke token
            revokeAllUserTokens(userOptional.get().getId());
        }
    }
}
