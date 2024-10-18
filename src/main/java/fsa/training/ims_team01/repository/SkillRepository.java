package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    boolean existsByName(String name);

    //    findAll
    @Query("select new fsa.training.ims_team01.model.dto.DropdownDto(s.id, s.name) from Skill s ")
    List<DropdownDto> findAllSkills();


}
