package com.lao.loanmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    // Admin can update these fields
    private String name;
    private String email;

    // Only applicable if the user is a BORROWER
    private Integer creditScore;
}
