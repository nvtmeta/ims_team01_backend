package fsa.training.ims_team01.service;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateDetailDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateFormDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateListDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CandidateService {

    //    list candidate
    Page<CandidateListDto> getCandidateList(Pageable pageable, String q, CandidateStatusEnum status);

    //    create candidate
    String createCandidate(CandidateFormDto candidateCreateDto);

    String updateCandidate(Long candidateId, CandidateFormDto candidateCreateDto);

    Optional<CandidateDetailDto> getCandidateById(Long candidateId);

    Page<DropdownDto> getCandidateByStatus(CandidateStatusEnum status, Pageable pageable);

    Page<DropdownDto> getCandidateNoBan(Pageable pageable, String q);

    //delete candidate
    String deleteCandidateById(Long candidateId);

    //    ban candidate
    String banCandidateById(Long candidateId);


    boolean isEmailExisted(String email);


    void updateCandidateStatus(Long candidateId, CandidateStatusEnum status);

//    get email candidate
    Optional<EmailSendDto> getEmailCandidate(Long candidateId);

}
