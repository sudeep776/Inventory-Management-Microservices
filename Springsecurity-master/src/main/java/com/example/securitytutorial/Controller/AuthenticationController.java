package com.example.securitytutorial.Controller;


import com.example.securitytutorial.Config.UsernamePwdAuthProvider;
import com.example.securitytutorial.Models.User;
import com.example.securitytutorial.Repository.UserRepository;
import com.example.securitytutorial.Service.UserService;
import com.example.securitytutorial.WebToken.JwtService;
import com.example.securitytutorial.WebToken.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class AuthenticationController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UsernamePwdAuthProvider usernamePwdAuthProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm) {
        Authentication authentication = usernamePwdAuthProvider.authenticate(new UsernamePasswordAuthenticationToken(
                loginForm.username(), loginForm.password()
        ));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(userService.loadUserByUsername(loginForm.username()));
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

//    @PostMapping("/verify")
//    public User verifyToken(HttpServletRequest request){
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer")) {
//            return null;
//        }
//        if(!jwtService.isTokenValid(authHeader)) return null;
//        String username = jwtService.extractUsername(authHeader);
//        Optional<User> user = userRepository.findByName(username);
//        return user.map(value -> User.builder().name(username).roles(value.getRoles()).build()).orElse(null);
//    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }
        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        String username = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByName(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Return only minimal info (username + roles)
        return ResponseEntity.ok(User.builder()
                .name(username)
                .roles(user.get().getRoles())
                .build());
    }


}

