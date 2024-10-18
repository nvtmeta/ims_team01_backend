package fsa.training.ims_team01.repository;

import fsa.training.ims_team01.model.dto.DropdownDto;
import fsa.training.ims_team01.model.dto.email.EmailSendDto;
import fsa.training.ims_team01.model.dto.userDto.UserAuthDto;
import fsa.training.ims_team01.model.dto.userDto.UserDetailDto;
import fsa.training.ims_team01.model.dto.userDto.UserListDto;
import fsa.training.ims_team01.model.entity.Department;
import fsa.training.ims_team01.model.entity.Role;
import fsa.training.ims_team01.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends BaseRepository<User, Long>, JpaSpecificationExecutor<User> {


    @Query(
            """
                                         select new fsa.training.ims_team01.model.dto.userDto.UserListDto
                                         (u.id, u.username, u.email, u.phone, u.status)
                                         from User u
                                         where u.deleted = false
                                        AND (:role IS NULL OR :role IN (SELECT r.id FROM u.roles r))
                                         AND (:keyword IS NULL OR (
                                        lower(u.fullName) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(u.email) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(u.note) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(u.status) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(u.gender) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(u.address) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(CAST(u.phone AS string)) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(u.username) LIKE lower(concat('%', :keyword, '%'))
                                        OR lower(u.department.name) LIKE lower(concat('%', :keyword, '%'))
                                           ))\s
                                         ORDER BY u.createdDate DESC
                                          \s""")
    Page<UserListDto> findAllByDeletedFalse(Pageable pageable, @Param("keyword") String q, @Param("role") String role);


    //    get all roles of user
    @Query(
            """
                        SELECT  r.role.name FROM RoleUser r WHERE r.user.id = :userId
                    """
    )
    List<String> getAllRolesByUserId(@Param("userId") Long userId);

    @Query(
            """
                        SELECT  new fsa.training.ims_team01.model.dto.DropdownDto(r.id, r.role.name) FROM RoleUser r WHERE r.user.id = :userId
                    """
    )
    List<DropdownDto> getDropdownRolesByUserId(@Param("userId") Long userId);



    Optional<User> findByEmailAndDeletedFalse(String email);


    //    getUserById
    @Query(
            """
                       select new fsa.training.ims_team01.model.dto.userDto.UserDetailDto
                    (u.fullName , u.email, u.dob, u.address, u.phone, u.status, u.gender,\s
                     u.note)
                     from User u \s
                     where u.id = :id and u.deleted = false
                          \s""")
    Optional<UserDetailDto> getUserDetailById(@Param("id") Long id);


    //    existsByEmailOrUsername
    @Query("select count(u) > 0 from User u where ((u.email = :email or u.username = :username) and u.deleted = false)")
    boolean existsByEmailOrUsernameAndDeletedFalse(@Param("email") String email, @Param("username") String username);

    boolean existsByEmailAndDeletedFalse(@Param("email") String email);

//    getRecruiterList
    @Query(
            """
                       select new fsa.training.ims_team01.model.dto.DropdownDto
                    (u.id, CONCAT(u.fullName, ' (', u.username, ')'))
                     from User u
                     join RoleUser ru on u.id = ru.user.id
                     join Role r on ru.role.id = r.id
                     where r.name = 'RECRUITER' and u.deleted = false
                          \s""")
    Page<DropdownDto> getRecruiterList(Pageable pageable);

    @Query(
            """
                       select new fsa.training.ims_team01.model.dto.DropdownDto
                    (u.id, u.fullName)
                     from User u
                     join RoleUser ru on u.id = ru.user.id
                     join Role r on ru.role.id = r.id
                     where r.name = :role and u.deleted = false
                      and (:q is null or lower(u.fullName) like lower(concat('%', :q, '%')))
                          \s""")
    Page<DropdownDto> getUserListByRole(Pageable pageable, @Param("role") String role, @Param("q") String q);

    @Query("""
                select new fsa.training.ims_team01.model.dto.userDto.UserAuthDto(u.id, u.username, u.password, u.email, u.fullName)
                from User u
                where u.username = :username
            """)
    Optional<UserAuthDto> findByUsername(@Param("username") String username);

    @Query("""
                select new fsa.training.ims_team01.model.dto.userDto.UserAuthDto(u.id, u.username, u.password, u.email, u.fullName)
                from User u
                where u.username = :username and u.status = 'ACTIVE'
            """)
    Optional<UserAuthDto> findByUsernameAndStatusInActive(@Param("username") String username);

    @Query("select new fsa.training.ims_team01.model.dto.userDto.UserAuthDto(u.id, u.username, u.password, u.email, u.fullName) " +
            "from User u " +
            "where u.email = :email and u.status = 'ACTIVE'")
    Optional<UserAuthDto> findByEmailAndStatusInActive(@Param("email") String email);


    @Query(
            """
                    SELECT new fsa.training.ims_team01.model.entity.Role(r.id, r.name)\s
                      FROM Role r
                      JOIN RoleUser ru ON r.id = ru.role.id
                      JOIN User u ON ru.user.id = u.id
                      WHERE u.username = :username
                     \s"""
    )
    Optional<Set<Role>> getAllRolesByUsername(@Param("username") String username);

//    getDepartmentByUsername
    @Query(
            """
                    SELECT new fsa.training.ims_team01.model.entity.Department(d.id, d.name)\s
                      FROM Department d
                      JOIN User u ON d.id = u.department.id
                      WHERE u.username = :username
                     \s"""
    )
    Optional<Department> getDepartmentByUsername(@Param("username") String username);

//    getDepartmentNameByUserId
    @Query(
            """
                    SELECT new fsa.training.ims_team01.model.dto.DropdownDto(d.id, d.name)\s
                      FROM Department d
                      JOIN User u ON d.id = u.department.id
                      WHERE u.id = :userId
                     \s"""
    )
    Optional<DropdownDto> getDepartmentNameByUserId(@Param("userId") Long userId);


//    findEmailByUserId
    @Query("select new fsa.training.ims_team01.model.dto.email.EmailSendDto(u.email, u.fullName) from User u where u.id = :userId and u.deleted = false")
    Optional<EmailSendDto> getEmailDtoUser(@Param("userId") Long userId);

}
