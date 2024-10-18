package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

//    findByName
    Optional<Role> findByName(String name);

//    findAllRole
    @Query("select new fsa.training.ims_team01.model.dto.DropdownDto(r.id, r.name) from Role r")
    List<DropdownDto> findAllRoles();
}
