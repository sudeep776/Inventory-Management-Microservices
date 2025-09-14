package clients;


import dto.LoginForm;
import jakarta.servlet.http.HttpServletRequest;
import models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authservice")
public interface AuthClient {

    //testing auth client
    @GetMapping("/hello")
    public String  hello();

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody LoginForm loginForm);

    @PostMapping("/verify")
    public User verifyToken(HttpServletRequest request);
}

