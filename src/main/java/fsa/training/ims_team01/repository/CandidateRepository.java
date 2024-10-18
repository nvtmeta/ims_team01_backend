package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.enums.candidateEnum.CandidateStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.candidateDto.CandidateListDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CandidateRepository extends BaseRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {
//    search by q and status

    @Query("""
            SELECT new fsa.training.ims_team01.model.dto.candidateDto.CandidateListDto(
                c.id, c.fullName, c.email, c.phone, c.status, p.name , u.fullName
            )
            FROM Candidate c
            LEFT JOIN User u ON c.recruiter.id = u.id
            LEFT JOIN Position p ON c.position.id = p.id
            WHERE c.deleted = false
            AND (:status IS NULL OR c.status = :status)
            AND (:keyword IS NULL OR (
                    lower(c.fullName) LIKE lower(concat('%', :keyword, '%'))
                    OR lower(c.email) LIKE lower(concat('%', :keyword, '%'))
                    OR lower(c.note) LIKE lower(concat('%', :keyword, '%'))
                    OR lower(p.name) LIKE lower(concat('%', :keyword, '%'))
                    OR lower(u.fullName) LIKE lower(concat('%', :keyword, '%'))
                    OR lower(c.address) LIKE lower(concat('%', :keyword, '%'))
                    OR lower(u.fullName) LIKE lower(concat('%', :keyword, '%'))
                ))
                ORDER BY
                CASE c.status
                    WHEN 'Waiting for interview' THEN 1
                    WHEN 'Waiting for approval' THEN 2
                    WHEN 'Waiting for response' THEN 3
                    WHEN 'OPEN' THEN 4
                    WHEN 'Passed Interview' THEN 5
                    WHEN 'Approved Offer' THEN 6
                    WHEN 'Rejected Offer' THEN 7
                    WHEN 'Accepted offer' THEN 8
                    WHEN 'Declined offer' THEN 9
                    WHEN 'Cancelled offer' THEN 10
                    WHEN 'Failed interview' THEN 11
                    WHEN 'Cancelled interview' THEN 12
                    WHEN 'BANNED' THEN 13
                    ELSE 999
                END,
                c.createdDate DESC
            """
    )
    Page<CandidateListDto> findAllByDeletedFalseSorted(Pageable pageable,
                                                       @Param("keyword") String q,
                                                       @Param("status") CandidateStatusEnum status
                                                       );


    Optional<Candidate> findByEmailAndDeletedFalse(String email);

    Optional<Candidate> findByIdAndDeletedFalse(Long candidateId);


    @Query(
            """
                       select new fsa.training.ims_team01.model.dto.DropdownDto
                    (c.id, c.fullName)
                     from Candidate c
                     where c.status = :status and c.deleted = false
                          \s""")
    Page<DropdownDto> getCandidateListByStatus(@Param("status") CandidateStatusEnum status, Pageable pageable);

//    getCandidateListNoBan
    @Query(
            """
                    select new fsa.training.ims_team01.model.dto.DropdownDto
                    (c.id, c.fullName)
                     from Candidate c
                     where c.deleted = false and c.status <> 'BANNED'
                     and (:keyword is null or lower(c.fullName) like lower(concat('%', :keyword, '%')))
                          \s""")
    Page<DropdownDto> getCandidateListNoBan(Pageable pageable, @Param(("keyword")) String q);


//    getEmailCandidate
    @Query("select new fsa.training.ims_team01.model.dto.email.EmailSendDto(c.email,c.fullName) from Candidate c where c.id = :candidateId and c.deleted = false")
    Optional<EmailSendDto> findEmailByCandidateId(Long candidateId);

}
