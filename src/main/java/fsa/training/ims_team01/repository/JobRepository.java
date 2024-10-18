package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.enums.commonEnum.JobStatusEnum;
import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.jobDto.JobDetailDto;
import fsa.training.ims_team01.model.dto.jobDto.JobListDto;
import fsa.training.ims_team01.model.dto.jobDto.JobScheduleDto;
import fsa.training.ims_team01.model.entity.Job;
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
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT new fsa.training.ims_team01.model.dto.jobDto.JobListDto(" +
            "j.id, j.title, " +
            "(SELECT STRING_AGG(s.name, ', ') FROM Skill s WHERE s MEMBER OF j.skills), " +
            "(SELECT STRING_AGG(l.name, ', ') FROM Level l WHERE l MEMBER OF j.levels)," +
            "j.startDate, j.endDate, j.status)" +
            "FROM Job j " +
            "WHERE j.deleted = false " +
            "AND (:status IS NULL OR j.status = :status) " +
            "AND (:keyword IS NULL OR (" +
            "lower(j.title) LIKE lower(concat('%', :keyword, '%')) " +
            "OR lower(j.description) LIKE lower(concat('%', :keyword, '%'))" +
            "OR lower(j.workingAddress) LIKE lower(concat('%', :keyword, '%'))" +
            "OR EXISTS (SELECT s FROM Skill s WHERE s MEMBER OF j.skills AND lower(s.name) LIKE lower(concat('%', :keyword, '%')))" +
            "OR EXISTS (SELECT l FROM Level l WHERE l MEMBER OF j.levels AND lower(l.name) LIKE lower(concat('%', :keyword, '%')))" +
            "OR EXISTS (SELECT b FROM Benefit b WHERE b MEMBER OF j.benefits AND lower(b.name) LIKE lower(concat('%', :keyword, '%'))) " +
            "))"
            +
            "ORDER BY " +
            "CASE j.status " +
            "WHEN 'OPEN' THEN 1 " +
            "WHEN 'OPEN' THEN 2 " +
            "WHEN 'CLOSED' THEN 3 " +
            "ELSE 999 " +
            "END, " +
            "j.createdDate DESC"
    )
    Page<JobListDto> findAllByDeletedFalse(Pageable pageable, @Param("keyword") String q,
                                           @Param("status") JobStatusEnum status);

    //todo:BRL-11-07
    //    search by id and deleted
    @Query("SELECT new fsa.training.ims_team01.model.dto.jobDto.JobDetailDto" +
            "(j.id, j.title, j.salaryFrom, j.salaryTo, j.startDate, j.endDate, j.workingAddress, " +
            "j.description, j.status,  j.createdDate, j.updatedDate)" +
            "FROM Job j WHERE j.id = :id AND j.deleted = false")
    Optional<JobDetailDto> findByIdAndDeletedFalse(Long id);

    Optional<Job> findJobByIdAndDeletedFalse(Long id);


    @Query("SELECT new fsa.training.ims_team01.model.dto.DropdownDto(j.id, j.title)" +
            "FROM Job j WHERE j.deleted = false AND j.status = :status AND (:q IS NULL OR lower(j.title) LIKE lower(concat('%', :q, '%')))"
    )
    Page<DropdownDto> getJobListByStatus(Pageable pageable, @Param("status") JobStatusEnum status, @Param("q") String q);


    //    findByStartDateLessThanEqual
    @Query("SELECT new fsa.training.ims_team01.model.dto.jobDto.JobScheduleDto(j.id,j.status)" +
            "FROM Job j WHERE j.deleted = false AND j.startDate <= :date")
    List<JobScheduleDto> findByStartDateLessThanEqual(LocalDate date);

    @Query("SELECT new fsa.training.ims_team01.model.dto.jobDto.JobScheduleDto(j.id,j.status)" +
            "FROM Job j WHERE j.deleted = false AND j.endDate <= :date")
    List<JobScheduleDto> findByEndDateLessThanEqual(LocalDate date);

}
