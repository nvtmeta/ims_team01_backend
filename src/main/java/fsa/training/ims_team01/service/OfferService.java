package fsa.training.ims_team01.service;


import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import fsa.training.ims_team01.model.dto.email.EmailSendOffer;
import fsa.training.ims_team01.model.dto.offerDto.OfferDetailDto;
import fsa.training.ims_team01.model.dto.offerDto.OfferFormDto;
import fsa.training.ims_team01.model.dto.offerDto.OfferListDto;
import fsa.training.ims_team01.model.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
public interface OfferService {

    Page<OfferListDto> getOfferList(Pageable pageable, String q, OfferStatusEnum status, Long departmentId, LocalDate contractFromDate, LocalDate contractToDate);

    Optional<OfferDetailDto> getOfferDetail(Long Id);

    Offer create(OfferFormDto offerFormDto);

    Offer update(Long id, OfferFormDto offerFormDto);

    String approveOffer(Long id);
    String rejectOffer(Long id);

    String markOfferCandidateSend(Long id);

    String acceptOffer(Long id);

    String declineOffer(Long id);

    String cancelOffer(Long id);

    Optional<Long> findCandidateIdByOfferId(Long id);

    List<EmailSendOffer> findByOfferDueDate(LocalDate now);
}
