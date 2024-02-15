package com.encore.ordering.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginReqDto {
    @Email(message = "invalid email")
    private String email;

    @NotEmpty(message = "password is essential")
    @Size(min = 4, message = "minimum password length is 4")
    private String password;
}
