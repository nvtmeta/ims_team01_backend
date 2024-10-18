package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.enums.commonEnum.OfferStatusEnum;
import fsa.training.ims_team01.model.dto.email.EmailSendOffer;
import fsa.training.ims_team01.model.dto.offerDto.OfferListDto;
import fsa.training.ims_team01.model.entity.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    @Query("select new fsa.training.ims_team01.model.dto.offerDto.OfferListDto(" +
            "o.id, o.candidate.fullName, o.candidate.email, o.approver.fullName, " +
            "o.department.name, o.note, o.status) " +
            "from Offer o " +
            "where o.deleted = false " +
            "AND (:status IS NULL OR :status = '' OR o.status = :status)" +
            "AND (:departmentId IS NULL OR :departmentId = 0L OR :departmentId = o.department.id)" +
            "AND (:keyword IS NULL OR :keyword = ''" +
            "OR lower(o.candidate.fullName) LIKE lower(concat('%', lower(:keyword), '%')) " +
            "OR lower(o.candidate.email) LIKE lower(concat('%', lower(:keyword), '%')) " +
            "OR lower(o.approver.fullName) LIKE lower(concat('%', lower(:keyword), '%')) " +
            "OR lower(o.department.name) LIKE lower(concat('%', lower(:keyword), '%')) " +
            "OR lower(o.note) LIKE lower(concat('%', lower(:keyword), '%'))" +
            "OR lower(CAST(o.status AS STRING)) LIKE lower(concat('%', lower(:keyword), '%'))) " +
            "AND (:contractFromDate IS NULL OR o.contractFromDate >= :contractFromDate) " +
            "AND (:contractToDate IS NULL OR o.contractToDate <= :contractToDate) " +
            "order by  o.createdDate desc")
    Page<OfferListDto> findAllByDeletedFalse(Pageable pageable,
                                             @Param("keyword") String keyword,
                                             @Param("status") OfferStatusEnum status,
                                             @Param(("departmentId")) Long departmentId,
                                             @Param(("contractFromDate")) LocalDate contractFromDate,
                                             @Param(("contractToDate")) LocalDate contractToDate
    );

//    @Query("SELECT NEW fsa.training.ims_team01.model.dto.offerDto.OfferDetailDto(" +
//            "o.id, o.contractType, " +
//            "(SELECT STRING_AGG(u.fullName, ', ') FROM User u WHERE u MEMBER OF InterviewSchedule .interviewers), " +
//            "o.startDate, o.endDate, o.dueDate, " +
//            "o.basicSalary, o.note, o.status) FROM Offer o")
//    Optional<OfferDetailDto> findByIdAndDeletedFalse(Long id);

    Optional<Offer> findOfferByIdAndDeletedFalse(Long id);

    @Query("SELECT o.status FROM Offer o WHERE o.id = :id AND o.deleted = false")
    OfferStatusEnum findStatus(@Param("id") Long id);

    //    findCandidateIdByOfferId
    @Query("SELECT o.candidate.id FROM Offer o WHERE o.id = :id AND o.deleted = false")
    Optional<Long> findCandidateIdByOfferId(@Param("id") Long id);


    @Query("SELECT new fsa.training.ims_team01.model.dto.email.EmailSendOffer(o.id, o.candidate.fullName, o.position.name, " +
            "o.dueDate, o.recruiterOwner.fullName ,o.approver.email,o.recruiterOwner.email)" +
            " FROM Offer o " +
            "WHERE o.dueDate = :now AND o.deleted = false AND o.status = 'WAITING_FOR_APPROVAL'")
    List<EmailSendOffer> findByOfferDueDate(LocalDate now);
}
