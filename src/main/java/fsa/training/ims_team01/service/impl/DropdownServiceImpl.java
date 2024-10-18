package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.repository.*;
import fsa.training.ims_team01.service.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DropdownServiceImpl implements DropdownService {
    private final DepartmentRepository departmentRepository;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;
    private final BenefitRepository benefitRepository;
    private final LevelRepository levelRepository;
    private final RoleRepository roleRepository;

//todo: schedule, to auto update status, and add job into interview

    @Override
    public List<DropdownDto> findAllDepartments() {
        return departmentRepository.findAllDepartments();
    }

    @Override
    public List<DropdownDto> findAllSkills() {
        return skillRepository.findAllSkills();
    }

    @Override
    public List<DropdownDto> findAllBenefits() {
        return benefitRepository.findAllBenefits();
    }

    @Override
    public List<DropdownDto> findAllPositions() {
        return positionRepository.findAllPosition();
    }

    @Override
    public List<DropdownDto> findAllRoles() {
         return roleRepository.findAllRoles();
    }

    @Override
    public List<DropdownDto> findAllLevels() {
        return levelRepository.findAllLevels();
    }
}
