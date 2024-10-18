package fsa.training.ims_team01.util;

import fsa.training.ims_team01.repository.BaseRepository;
import fsa.training.ims_team01.repository.CandidateRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public class EmailValidationUtils {

    public static <T> boolean isEmailExisted(String email, BaseRepository<T, Long> repository) {
        return repository.existsByDeletedFalseAndEmail(email);
    }

}
