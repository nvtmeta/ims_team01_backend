package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.exception.DuplicateException;
import fsa.training.ims_team01.exception.NotFoundException;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateDetailDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateFormDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateListDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.entity.Candidate;
import fsa.training.ims_team01.model.entity.Position;
import fsa.training.ims_team01.model.entity.Skill;
import fsa.training.ims_team01.model.entity.User;
import fsa.training.ims_team01.repository.CandidateRepository;
import fsa.training.ims_team01.repository.SkillRepository;
import fsa.training.ims_team01.service.CandidateService;
import fsa.training.ims_team01.util.EmailValidationUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CandidateServiceImpl implements CandidateService {
    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository, SkillRepository skillRepository) {
        this.candidateRepository = candidateRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public Page<CandidateListDto> getCandidateList(Pageable pageable, String q, CandidateStatusEnum status) {
        return candidateRepository.findAllByDeletedFalseSorted(pageable, q, status);
    }

    private void updateCandidateProperties(Candidate candidate, CandidateFormDto candidateFormDto) {
        BeanUtils.copyProperties(candidateFormDto, candidate);
        if (EmailValidationUtils.isEmailExisted(candidate.getEmail(), candidateRepository)) {
//            check if it is candidate old email
            Optional<Candidate> candidateOptional = candidateRepository.findByEmailAndDeletedFalse(candidate.getEmail());
            if (candidateOptional.isPresent() && !candidateOptional.get().getId().equals(candidate.getId())) {
                throw new DuplicateException("Email already exists");
            }
        }

        List<Skill> skills = candidateFormDto.getSkillCandidates().stream()
                .map(skill -> skillRepository.findById(skill.getValue()).orElse(null))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Optional.ofNullable(candidateFormDto.getPosition())
                .map(dto -> new Position(dto.getValue()))
                .ifPresent(candidate::setPosition);

        Optional.ofNullable(candidateFormDto.getRecruiter())
                .map(dto -> new User(dto.getValue()))
                .ifPresent(candidate::setRecruiter);

        Optional.ofNullable(candidateFormDto.getAuthor())
                .map(dto -> new User(dto.getValue()))
                .ifPresent(candidate::setAuthor);

        candidate.setSkills(skills);
    }


    @Override
    public String createCandidate(CandidateFormDto candidateCreateDto) {
        //todo: upload file
        Candidate candidate = new Candidate();
        updateCandidateProperties(candidate, candidateCreateDto);
        candidateRepository.save(candidate);
        return candidate.getId().toString();
    }

    @Override
    public String updateCandidate(Long candidateId, CandidateFormDto candidateFormDto) {
        Optional<Candidate> optionalCandidate = candidateRepository.findByIdAndDeletedFalse(candidateId);
        if (optionalCandidate.isPresent()) {
            Candidate candidate = optionalCandidate.get();
            updateCandidateProperties(candidate, candidateFormDto);
            candidateRepository.save(candidate);
            return candidate.getId().toString();
        }
        throw new NotFoundException("Candidate not found");
    }

    @Override
    public Optional<CandidateDetailDto> getCandidateById(Long candidateId) {
        Optional<Candidate> candidate = candidateRepository.findByIdAndDeletedFalse(candidateId);
        if (candidate.isPresent()) {
            CandidateDetailDto candidateDetailDto = new CandidateDetailDto();
            BeanUtils.copyProperties(candidate.get(), candidateDetailDto, "skills");

            candidateDetailDto.setPosition(Optional.ofNullable(candidate.get().getPosition())
                    .map(position -> new DropdownDto(position.getId(), position.getName()))
                    .orElse(null));
            candidateDetailDto.setRecruiter(Optional.ofNullable(candidate.get().getRecruiter())
                    .map(user -> new DropdownDto(user.getId(), user.getFullName()))
                    .orElse(null));

            candidateDetailDto.setAuthor(Optional.ofNullable(candidate.get().getAuthor())
                    .map(user -> new DropdownDto(user.getId(), user.getFullName()))
                    .orElse(null));


            List<DropdownDto> skillsDto = candidate.get().getSkills().stream()
                    .filter(Objects::nonNull) // Filter out potential null skills
                    .map(skill -> new DropdownDto(skill.getId(), skill.getName()))
                    .collect(Collectors.toList());

            candidateDetailDto.setSkills(skillsDto);

            return Optional.of(candidateDetailDto);
        }
        throw new NotFoundException("Candidate not found");
    }

    @Override
    public Page<DropdownDto> getCandidateByStatus(CandidateStatusEnum status, Pageable pageable) {
        return candidateRepository.getCandidateListByStatus(status, pageable);
    }

    @Override
    public Page<DropdownDto> getCandidateNoBan(Pageable pageable , String q) {
        return candidateRepository.getCandidateListNoBan(pageable , q);
    }


    @Override
    public String deleteCandidateById(Long candidateId) {
//        Candidate only can be deleted when in status “Open”. If it is in another status, do not
//show “Delete” button in the list of candidate
        Optional<Candidate> candidate = candidateRepository.findByIdAndDeletedFalse(candidateId);
        if (candidate.isPresent() && candidate.get().getStatus() == CandidateStatusEnum.OPEN) {
            candidate.get().setDeleted(true);
            candidateRepository.save(candidate.get());
            return "Candidate successfully deleted.";
        }
        throw new NotFoundException("Candidate not found or already deleted.");
    }

    @Override
    public String banCandidateById(Long candidateId) {
        Optional<Candidate> candidate = candidateRepository.findByIdAndDeletedFalse(candidateId);
        if (candidate.isPresent()) {
            candidate.get().setStatus(CandidateStatusEnum.BANNED);
            candidateRepository.save(candidate.get());
            return "Candidate successfully banned."; // Success message
        }
        throw new NotFoundException("Candidate not found");
    }

    @Override
    public boolean isEmailExisted(String email) {
        return EmailValidationUtils.isEmailExisted(email, candidateRepository);
    }

    @Override
    public void updateCandidateStatus(Long candidateId, CandidateStatusEnum status) {
        Optional<Candidate> candidate = candidateRepository.findByIdAndDeletedFalse(candidateId);
        if (candidate.isPresent()) {
            candidate.get().setStatus(status);
            candidateRepository.save(candidate.get());
        }
    }

    @Override
    public Optional<EmailSendDto> getEmailCandidate(Long candidateId) {
        return candidateRepository.findEmailByCandidateId(candidateId);
    }
}
