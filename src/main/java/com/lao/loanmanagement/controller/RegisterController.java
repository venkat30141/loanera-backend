package com.lao.loanmanagement.controller;

import com.lao.loanmanagement.entity.User;
import com.lao.loanmanagement.entity.UserStatus;
import com.lao.loanmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class RegisterController {

    private final UserRepository userRepository;

    
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        

        // default status (ENUM, not String)
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }
}
