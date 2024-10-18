package fsa.training.ims_team01.config;

import fsa.training.ims_team01.model.entity.*;
import fsa.training.ims_team01.repository.*;
import fsa.training.ims_team01.security.config.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DbInit {

    private final DepartmentRepository departmentRepository;
    private final SkillRepository skillRepository;
    private final PositionRepository positionRepository;
    private final BenefitRepository benefitRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final LevelRepository levelRepository;


    @PostConstruct
    private void postConstruct() {

//        o IT
//o HR
//o Finance
//o Communication
//o Marketing
//o Accounting
        List<String> departments = new ArrayList<>(Arrays.asList("IT", "HR", "Finance", "Communication", "Marketing", "Accounting"));
        departments.forEach(department -> {
            if (!departmentRepository.existsByName(department)) {
                Department departmentEntity = new Department(department);
                departmentRepository.save(departmentEntity);
                System.out.println("Department created: " + department);
            } else {
                System.out.println("Department already exists: " + department);
            }
        });

//        Backend Developer,
//o Business Analyst,
//o Tester,
//o HR,
//o Project manager
//o Not available
        List<String> positions = new ArrayList<>(Arrays.asList("Backend Developer", "Business Analyst", "Tester", "HR", "Project manager", "Not available"));

        positions.forEach(position -> {
            if (!positionRepository.existsByName(position)) {
                Position positionEntity = new Position(position);
                positionRepository.save(positionEntity);
                System.out.println("Position created/" + position);
            } else {
                System.out.println("Position already exists: " + position);
            }
        });

//Skills o Java
//o Nodejs
//o .net
//o C++
//o Business analysis
//o Communication
        List<String> skills = new ArrayList<>(Arrays.asList("Nodejs", "Java", ".net", "C++", "Business analysis", "Communication"));

        skills.forEach(skill -> {
            if (!skillRepository.existsByName(skill)) {
                Skill skillEntity = new Skill(skill);
                skillRepository.save(skillEntity);
                System.out.println("Skill created: " + skill);
            } else {
                System.out.println("Skill already exists: " + skill);
            }
        });

//        Lunch
//o 25-day leave
//o Healthcare insurance
//o Hybrid working
//o Travel
        List<String> benefits = new ArrayList<>(Arrays.asList("Lunch", "25-day leave", "Healthcare insurance", "Hybrid working", "Travel"));
        benefits.forEach(benefit -> {
            if (!benefitRepository.existsByName(benefit)) {
                Benefit benefitEntity = new Benefit(benefit);
                benefitRepository.save(benefitEntity);
                System.out.println("Benefit created: " + benefit);
            } else {
                System.out.println("Benefit already exists: " + benefit);
            }
        });

//        List of role: Admin, Recruiter, Manager, Interviewer
        List<String> roles = new ArrayList<>(Arrays.asList("Admin", "Recruiter", "Manager", "Interviewer"));
        roles.forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                Role roleEntity = new Role(role);
                roleRepository.save(roleEntity);
                System.out.println("Role created: " + role);
            } else {
                System.out.println("Role already exists: " + role);
            }
        });

        //Fresher
        //Junior
        //Senior
        //Leader
        //Manager
        //Vice Head
        List<String> levels = new ArrayList<>(Arrays.asList("Fresher", "Junior", "Senior", "Leader", "Manager", "Vice Head"));
        levels.forEach(level -> {
            if (!levelRepository.existsByName(level)) {
                Level levelEntity = new Level(level);
                levelRepository.save(levelEntity);
                System.out.println("Level created: " + level);
            } else {
                System.out.println("Level already exists: " + level);
            }
        });
        initUserAccount();

    }

    private void initUserAccount() {
        if (userRepository.findByUsername("admin").isPresent()) {
            System.out.println("User already exists, skip init admin account");
        } else {
            Optional<Role> adminRole = roleRepository.findByName("Admin");
            if (adminRole.isPresent()) {
                Role role = adminRole.get();

                User savedUser = new User();
                savedUser.setUsername("admin");
                savedUser.setFullName("admin");
                savedUser.setEmail("admin@gmail.com");
                savedUser.setPassword(passwordEncoder.encode("123"));
                User user = userRepository.save(savedUser);

                user.setRoles(Set.of(role));
                userRepository.save(user);
                System.out.println("Admin account created");
            }
        }

    }

}