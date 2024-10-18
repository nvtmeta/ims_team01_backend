package fsa.training.ims_team01.util;

import java.util.UUID;

public class GenerateUsernameUtil {

    public static String generateUsername(String fullName, Long id) {
        // 1. Split the full name into words (assuming Vietnamese name format)
        String[] nameParts = fullName.split("\\s+");

        // 2. Extract first name
        String firstName = nameParts[0];

        // 3. Extract initials from middle and last name
        StringBuilder initials = new StringBuilder();
        for (int i = 1; i < nameParts.length; i++) {
            initials.append(nameParts[i].charAt(0));
        }

        // 4. Combine username parts and add ID
        return firstName + initials.toString() + id ;
    }

}
