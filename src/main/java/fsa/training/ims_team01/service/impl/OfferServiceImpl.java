package fsa.training.ims_team01.service.impl;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.email.EmailSendOffer;
import fsa.training.ims_team01.model.dto.offerDto.OfferDetailDto;
import fsa.training.ims_team01.model.dto.offerDto.OfferFormDto;
import fsa.training.ims_team01.model.dto.offerDto.OfferListDto;
import fsa.training.ims_team01.model.entity.*;
import fsa.training.ims_team01.repository.InterviewScheduleRepository;
import fsa.training.ims_team01.repository.OfferRepository;
import fsa.training.ims_team01.service.CandidateService;
import fsa.training.ims_team01.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final InterviewScheduleRepository isRepository;
    private final CandidateService candidateService;

    @Override
    public Page<OfferListDto> getOfferList(Pageable pageable, String q, OfferStatusEnum status, Long departmentId,LocalDate contractFromDate, LocalDate contractToDate) {
        return offerRepository.findAllByDeletedFalse(pageable, q, status, departmentId, contractFromDate, contractToDate);
    }

    @Override
    public Optional<OfferDetailDto> getOfferDetail(Long id) {
        Optional<Offer> offerOptional = offerRepository.findOfferByIdAndDeletedFalse(id);
        if (offerOptional.isPresent()) {
            OfferDetailDto offerDetailDto = new OfferDetailDto();
            BeanUtils.copyProperties(offerOptional.get(), offerDetailDto);

            offerDetailDto.setCandidate(Optional.ofNullable(offerOptional.get().getCandidate())
                    .map(object -> new DropdownDto(object.getId(), object.getFullName()))
                    .orElse(null));

            offerDetailDto.setPosition(Optional.ofNullable(offerOptional.get().getPosition())
                    .map(object -> new DropdownDto(object.getId(), object.getName()))
                    .orElse(null));

            offerDetailDto.setLevel(Optional.ofNullable(offerOptional.get().getLevel())
                    .map(object -> new DropdownDto(object.getId(), object.getName()))
                    .orElse(null));

            offerDetailDto.setApprover(Optional.ofNullable(offerOptional.get().getApprover())
                    .map(object -> new DropdownDto(object.getId(), object.getFullName()))
                    .orElse(null));

            offerDetailDto.setDepartment(Optional.ofNullable(offerOptional.get().getDepartment())
                    .map(object -> new DropdownDto(object.getId(), object.getName()))
                    .orElse(null));

            offerDetailDto.setInterviewInfo(Optional.ofNullable(offerOptional.get().getInterviewSchedule())
                    .map(object -> new DropdownDto(object.getId(), object.getTitle()))
                    .orElse(null));

            offerDetailDto.setRecruiterOwner(Optional.ofNullable(offerOptional.get().getRecruiterOwner())
                    .map(object -> new DropdownDto(object.getId(), object.getFullName()))
                    .orElse(null));

//            offerDetailDto.setInterviewers(isRepository.getInterviewers(offerDetailDto.getInterviewInfo().getValue()).toString());
//            offerDetailDto.setInterviewNotes(isRepository.findNote(offerDetailDto.getInterviewInfo().getValue()));
            offerDetailDto.setInterviewNotes(offerOptional
                    .map(Offer::getInterviewSchedule)
                    .map(InterviewSchedule::getNote)
                    .orElse(""));
            List<DropdownDto> interviewers = offerOptional.get().getInterviewSchedule().getInterviewers().stream()
                    .map(item -> new DropdownDto(item.getId(), item.getFullName()))
                    .distinct()
                    .collect(Collectors.toList());
            offerDetailDto.setInterviewers(interviewers);


            return Optional.of(offerDetailDto);
        }
        return Optional.empty();
    }

    @Override
    public Offer create(OfferFormDto offerFormDto) {
        Offer offer = new Offer();
        updateProperties(offerFormDto, offer);

        candidateService.updateCandidateStatus(offerFormDto.getCandidate().getValue(), CandidateStatusEnum.WAITING_FOR_APPROVAL);
        return offerRepository.save(offer);
    }

    @Override
    public Offer update(Long id, OfferFormDto offerFormDto) {
        Optional<Offer> offerOptional = offerRepository.findOfferByIdAndDeletedFalse(id);
        if (offerOptional.isEmpty()) {
            throw new RuntimeException("Offer not found");
        }
        Offer offer = offerOptional.get();
        updateProperties(offerFormDto, offer);
        return offerRepository.save(offer);
    }

    public String updateOfferStatus(Long id, OfferStatusEnum status) {
        Optional<Offer> offerOptional = offerRepository.findOfferByIdAndDeletedFalse(id);
        if (offerOptional.isEmpty()) {
            throw new RuntimeException("Offer not found");
        }
        Offer offer = offerOptional.get();
        offer.setStatus(status);
        return offerRepository.save(offer).getId().toString();
    }

    @Override
    public String approveOffer(Long id) {
        Long candidateId = findCandidateIdByOfferId(id).orElseThrow(null);
        candidateService.updateCandidateStatus(candidateId, CandidateStatusEnum.APPROVED_OFFER);
        return updateOfferStatus(id, OfferStatusEnum.APPROVED_OFFER);
    }

    @Override
    public String rejectOffer(Long id) {
        Long candidateId = findCandidateIdByOfferId(id).orElseThrow(null);
        candidateService.updateCandidateStatus(candidateId, CandidateStatusEnum.REJECTED_OFFER);
        return updateOfferStatus(id, OfferStatusEnum.REJECTED_OFFER);
    }

    @Override
    public String markOfferCandidateSend(Long id) {
        Long candidateId = findCandidateIdByOfferId(id).orElseThrow(null);
        candidateService.updateCandidateStatus(candidateId, CandidateStatusEnum.WAITING_FOR_RESPONSE);
        return updateOfferStatus(id, OfferStatusEnum.WAITING_FOR_RESPONSE);
    }

    @Override
    public String acceptOffer(Long id) {
        Long candidateId = findCandidateIdByOfferId(id).orElseThrow(null);
        candidateService.updateCandidateStatus(candidateId, CandidateStatusEnum.ACCEPTED_OFFER);
         return updateOfferStatus(id, OfferStatusEnum.ACCEPTED_OFFER);
    }

    @Override
    public String declineOffer(Long id) {
        Long candidateId = findCandidateIdByOfferId(id).orElseThrow(null);
        candidateService.updateCandidateStatus(candidateId, CandidateStatusEnum.DECLINED_OFFER);
        return updateOfferStatus(id, OfferStatusEnum.DECLINED_OFFER);
    }

    @Override
    public String cancelOffer(Long id) {
        Long candidateId = findCandidateIdByOfferId(id).orElseThrow(null);
        candidateService.updateCandidateStatus(candidateId, CandidateStatusEnum.CANCELLED_OFFER);
        return updateOfferStatus(id, OfferStatusEnum.CANCELLED);
    }

    @Override
    public Optional<Long> findCandidateIdByOfferId(Long id) {
        return offerRepository.findCandidateIdByOfferId(id);
    }

    @Override
    public List<EmailSendOffer> findByOfferDueDate(LocalDate now) {
        System.out.println("now: " + now);
        return offerRepository.findByOfferDueDate(now);
    }

    private void updateProperties(OfferFormDto formDto, Offer offer) {
        BeanUtils.copyProperties(formDto, offer);
        Optional.ofNullable(formDto.getCandidate())
                .map(dto -> new Candidate(dto.getValue()))
                .ifPresent(offer::setCandidate);

        Optional.ofNullable(formDto.getPosition())
                .map(dto -> new Position(dto.getValue()))
                .ifPresent(offer::setPosition);

        Optional.ofNullable(formDto.getLevel())
                .map(dto -> new Level(dto.getValue()))
                .ifPresent(offer::setLevel);

        Optional.ofNullable(formDto.getApprover())
                .map(dto -> new User(dto.getValue()))
                .ifPresent(offer::setApprover);

        Optional.ofNullable(formDto.getDepartment())
                .map(dto -> new Department(dto.getValue()))
                .ifPresent(offer::setDepartment);

        Optional.ofNullable(formDto.getInterviewInfo())
                .map(dto -> new InterviewSchedule(dto.getValue()))
                .ifPresent(offer::setInterviewSchedule);

        Optional.ofNullable(formDto.getRecruiterOwner())
                .map(dto -> new User(dto.getValue()))
                .ifPresent(offer::setRecruiterOwner);

//        offer.setInterviewNote(isRepository.findNote(formDto.getInterviewInfo() != null ? formDto.getInterviewInfo().getValue() : null));
    }
}
