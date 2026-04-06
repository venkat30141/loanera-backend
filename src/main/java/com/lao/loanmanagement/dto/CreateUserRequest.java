package com.lao.loanmanagement.dto;

import com.lao.loanmanagement.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    private String name;
    private String email;
    private String password;

    // Allowed values:
    // BORROWER
    // LENDER
    // ANALYST
    private Role role;
}
