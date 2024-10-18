package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.entity.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BenefitRepository extends JpaRepository<Benefit, Long> {
    boolean existsByName(String name);


    @Query("select new fsa.training.ims_team01.model.dto.DropdownDto(b.id, b.name) from Benefit  b")
    List<DropdownDto> findAllBenefits();

}
