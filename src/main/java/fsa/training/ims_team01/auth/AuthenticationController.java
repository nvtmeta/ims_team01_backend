package fsa.training.ims_team01.auth;


import fsa.training.ims_team01.request.ResetPassword;
import fsa.training.ims_team01.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final UserService userService;

    //TODO:forgot password ui
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = service.authenticate(request);
        System.out.println("AuthenticationResponse: " + authenticationResponse);
        // Set the token as a cookie in the response
        //todo:fix cookies
//        Cookie cookie = new Cookie("token_p", authenticationResponse.getToken());
//        cookie.setMaxAge(86400); // Set the cookie's expiration time (in seconds), e.g. 24 hours
//        cookie.setPath("/"); // Set the cookie's path to be accessible across the entire domain
//        cookie.setSecure(false);
//        cookie.setDomain("localhost"); // Set the cookie's domain to be accessible'
//        cookie.setHttpOnly(true); // Set the cookie as HttpOnly
//        response.addCookie(cookie);
//        response.setHeader("Access-Control-Expose-Headers", "Set-Cookie");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }

    @PostMapping("/forgot-password/{email}")
    public String forgotPassword(@PathVariable("email") String email) {
        return service.forgotPassword(email);
    }

    @GetMapping("/isEmailExisted/{email}")
    public boolean isEmailExisted(@PathVariable String email) {
        return userService.isEmailExisted(email);
    }

    // isTokenValid
    @GetMapping("/isTokenValid/{token}")
    public boolean isTokenValid(@PathVariable String token) {
        return service.isTokenValid(token);
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPassword request,
            Principal connectedUser
    ) {
        service.resetPassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
