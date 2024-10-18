package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.model.entity.Role;
import fsa.training.ims_team01.model.entity.RoleUser;
import fsa.training.ims_team01.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUser, Long> {
    boolean existsByUserAndRole(User user, Role role);

    Optional<RoleUser> findByUserAndRole(User user, Role role);

    Optional<List<RoleUser>> findByUser(User user);

}
