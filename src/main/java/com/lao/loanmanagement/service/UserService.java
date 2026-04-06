package com.lao.loanmanagement.service;

import com.lao.loanmanagement.entity.Role;
import com.lao.loanmanagement.entity.User;
import com.lao.loanmanagement.entity.UserStatus;
import com.lao.loanmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /* =========================
       GET ALL USERS (ADMIN)
    ========================== */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /* =========================
       CREATE USER (ADMIN)
       ✅ INSERTS INTO USERS TABLE ONLY
    ========================== */
    public User createUser(
            String name,
            String email,
            String password,
            Role role
    ) {

        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)   // encrypt later if needed
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        // ✅ ONLY USERS TABLE IS TOUCHED
        return userRepository.save(user);
    }

    /* =========================
       UPDATE USER (ADMIN)
       - name
       - email
       (NO borrower logic here)
    ========================== */
    public User updateUser(
            Long userId,
            String name,
            String email
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        if (email != null && !email.isBlank()) {
            user.setEmail(email);
        }

        return userRepository.save(user);
    }

    /* =========================
       DELETE USER (ADMIN)
       (still only affects users table)
    ========================== */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
