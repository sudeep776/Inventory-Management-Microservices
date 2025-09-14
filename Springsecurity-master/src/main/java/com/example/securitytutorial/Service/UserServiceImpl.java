package com.example.securitytutorial.Service;


import com.example.securitytutorial.Models.User;
import com.example.securitytutorial.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByName(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"+username));
        System.out.println("Loaded user: " + user.getName());
        System.out.println("Loaded pwd: " + user.getPassword());
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role->new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getName(),user.getPassword(),authorities);
    }
}
