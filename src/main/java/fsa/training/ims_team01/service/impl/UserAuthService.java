package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.exception.NotFoundException;
import fsa.training.ims_team01.model.dto.userDto.UserAuthDto;
import fsa.training.ims_team01.model.entity.Role;
import fsa.training.ims_team01.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserAuthService {
    private final UserRepository userRepository;

    public Optional<UserAuthDto> findByUsername(String username) {
        return getUserAuthDto(username, true, userRepository);
    }

    public static Optional<UserAuthDto> getUserAuthDto(String identifier, boolean isUsername, UserRepository userRepository) {
        Optional<UserAuthDto> userAuthDto;
        if (isUsername) {
            userAuthDto = userRepository.findByUsernameAndStatusInActive(identifier);
        } else {
            userAuthDto = userRepository.findByEmailAndStatusInActive(identifier);
        }
        if (userAuthDto.isPresent()) {
            // get list role
            Optional<Set<Role>> roles = userRepository.getAllRolesByUsername(identifier);
            if (roles.isPresent()) {
                userAuthDto.get().setRoles(roles.get());
                return userAuthDto;
            }
        }
        throw new NotFoundException("User not found");
    }

}
