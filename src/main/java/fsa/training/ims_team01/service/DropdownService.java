package fsa.training.ims_team01.service;

import fsa.training.ims_team01.model.dto.DropdownDto;

import java.util.List;

public interface DropdownService {
    List<DropdownDto> findAllDepartments();
    List<DropdownDto> findAllSkills();

    List<DropdownDto> findAllBenefits();

    List<DropdownDto> findAllPositions();

    List<DropdownDto> findAllLevels();
    List<DropdownDto> findAllRoles();

}
