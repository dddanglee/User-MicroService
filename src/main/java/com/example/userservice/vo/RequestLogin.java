package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {
    @NotNull(message ="Email cannot be null")
    @Size(min=2,message = "Email not be less then two characters")
    @Email
    private String email;

    @NotNull(message ="password cannot be null")
    @Size(min=8,message = "password must be less then eight characters")
    private String password;
}