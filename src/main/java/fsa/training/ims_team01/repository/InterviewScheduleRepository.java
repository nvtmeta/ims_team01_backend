package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.enums.interviewEnum.InterviewScheduleEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleDetailDto;
import fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleListDto;
import fsa.training.ims_team01.model.entity.InterviewSchedule;
import fsa.training.ims_team01.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {
    //search
    @Query(
            "SELECT new fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleListDto" +
                    "(i.id, i.title, i.candidate.fullName, " +
                    "(SELECT STRING_AGG(u.fullName, ', ') FROM User u WHERE u MEMBER OF i.interviewers), " +
                    "(SELECT STRING_AGG(u.id, ', ') FROM User u WHERE u MEMBER OF i.interviewers), " +
                    "CONCAT(i.date, ' ', CONCAT(i.startTime, ' - '), i.endTime) , " +
                    "i.result, i.status, j.title )" +
                    " FROM InterviewSchedule i" +
                    " LEFT JOIN User u ON i.recruiter.id = u.id" +
                    " LEFT JOIN Job j ON j.id = i.job.id" +
                    " WHERE i.deleted = false" +
                    " AND (:status IS NULL OR :status = '' OR i.status = :status)" +
                    " AND (:keyword IS NULL OR :keyword = '' OR lower(i.title) LIKE lower(concat('%', lower(:keyword), '%')) " +
                    "OR lower(i.candidate.fullName) LIKE lower(concat('%', lower(:keyword), '%')) " +
                    "OR lower(j.title) LIKE lower(concat('%', lower(:keyword), '%')) " +
                    "OR lower(u.fullName) LIKE lower(concat('%', lower(:keyword), '%')) " +
                    "OR lower(i.note) LIKE lower(concat('%', lower(:keyword), '%'))" +
                    " OR lower(CAST(i.result AS STRING)) LIKE lower(concat('%', lower(:keyword), '%'))) "+
                    " AND (:interviewerId IS NULL OR :interviewerId = 0L OR :interviewerId in (SELECT u.id FROM User u WHERE u MEMBER OF i.interviewers))" +
                    " ORDER BY i.createdDate DESC"
    )
    Page<InterviewScheduleListDto> findAllByDeletedFalse(Pageable pageable,
                                                         @Param("keyword") String q,
                                                         @Param("status") InterviewScheduleEnum status,
                                                         @Param("interviewerId") Long interviewerId);


    @Query("SELECT new fsa.training.ims_team01.model.dto.interviewScheduleDto.InterviewScheduleDetailDto(i.date, i.title, i.startTime, i.endTime," +
            " i.meetingId,i.location, i.result, i.note, i.status, i.createdDate,i.updatedDate )" +
            " FROM InterviewSchedule i WHERE i.deleted = false AND i.id = ?1")
    Optional<InterviewScheduleDetailDto> getByIdAndDeletedFalse(Long id);

    Optional<InterviewSchedule> findByIdAndDeletedFalse(Long id);


    @Query(
            "SELECT new fsa.training.ims_team01.model.dto.DropdownDto(u.id,u.fullName) " +
                    " FROM User u" +
                    " JOIN InterviewSchedule i ON i.recruiter.id = u.id" +
                    "  WHERE i.id = ?1"
    )
    Optional<DropdownDto> getRecruiterByInterviewId(Long id);

    @Query(
            "SELECT new fsa.training.ims_team01.model.dto.DropdownDto(j.id, j.title) " +
                    " FROM Job j" +
                    " JOIN InterviewSchedule i ON i.job.id = j.id" +
                    "  WHERE i.id = ?1"
    )
    Optional<DropdownDto> getJobByInterviewId(Long id);

    @Query(
            "SELECT new fsa.training.ims_team01.model.dto.DropdownDto(c.id, c.fullName) " +
                    " FROM Candidate c" +
                    " JOIN InterviewSchedule i ON i.candidate.id = c.id" +
                    "  WHERE i.id = ?1"
    )
    Optional<DropdownDto> getCandidateByInterviewId(Long id);

    @Query(
            "SELECT new fsa.training.ims_team01.model.dto.DropdownDto(u.id, u.fullName) " +
                    " FROM User u" +
                    " JOIN InterviewSchedule i ON i.author.id = u.id" +
                    "  WHERE i.id = ?1"
    )
    Optional<DropdownDto> getAuthorByInterviewId(Long id);


    boolean existsByIdAndDeletedFalse(Long id);


    @Query("SELECT i.status FROM InterviewSchedule i WHERE i.deleted = false AND i.id = ?1")
    Optional<InterviewScheduleEnum> findStatusByIdAndDeletedFalse(Long id);


    @Query("SELECT i.note FROM InterviewSchedule i WHERE i.deleted = false AND i.id = ?1")
    Optional<String> getNoteByIdAndDeletedFalse(Long id);


    //    findByScheduleDate
    @Query("SELECT new fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule(i.id, i.title, i.date, i.candidate.fullName," +
            "i.candidate.position.name, i.candidate.email, i.startTime, i.endTime, i.recruiter.email, i.meetingId )"
            + " FROM InterviewSchedule i WHERE i.deleted = false AND i.date = :scheduleDate")
    List<EmailSendInterviewSchedule> findByScheduleDate(@Param("scheduleDate") LocalDate scheduleDate);

    @Query("SELECT new fsa.training.ims_team01.model.dto.email.EmailSendInterviewSchedule(i.id, i.title, i.date, i.candidate.fullName," +
            "i.candidate.position.name, i.candidate.email, i.startTime, i.endTime, i.recruiter.email, i.meetingId )"
            + " FROM InterviewSchedule i WHERE i.deleted = false AND i.id = :id")
    Optional<EmailSendInterviewSchedule> findByScheduleDateById(@Param("id") Long id);

    @Query("SELECT i.interviewers FROM InterviewSchedule i WHERE i.id = :id")
    List<User> getInterviewers(@Param("id") Long id);

    @Query("SELECT i.note FROM InterviewSchedule i WHERE i.id = :id")
    String findNote(@Param("id") Long id);

}
