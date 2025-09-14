package com.example.securitytutorial.Controller;

import com.example.securitytutorial.Models.User;
import com.example.securitytutorial.Repository.UserRepository;
import com.example.securitytutorial.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user")
    public User createUser(@RequestBody User user){

        // Set the password with the noop prefix
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user to the repository
        return userRepository.save(user);
    }
}
