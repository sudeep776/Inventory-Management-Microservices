package com.example.securitytutorial.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String  hello(){
        return "Hello";
    }

    @GetMapping("/helloAuth")
    public String helloAuth(){
        return "Hello you are authenticated";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/helloUser")
    public String getUser(){
        return "Hi User you are authenticated";
    }

    @GetMapping("/helloAdmin")
    public String getAdmin(){
        return "Hi Admin you are authenticated";
    }
}
