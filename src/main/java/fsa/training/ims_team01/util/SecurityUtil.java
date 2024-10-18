package fsa.training.ims_team01.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class SecurityUtil {
    private SecurityUtil() {
    }

    public static List<String> getRoleCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        return authentication
                .getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replaceAll("ROLE_", ""))
                .toList();
    }
    //    //        List of role: Admin, Recruiter, Manager, Interviewer
    public static boolean isAdmin(String role) {
        return role.equals("ADMIN");
    }

    public static boolean isRecruiter() {
        return getRoleCurrentUserLogin().contains("RECRUITER");
    }

    public static boolean isManager(String role) {
        return role.equals("MANAGER");
    }

    public static boolean isInterviewer(String role) {
        return role.equals("INTERVIEWER");
    }
}

