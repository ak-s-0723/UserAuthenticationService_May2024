package org.example.userauthenticationservice_may.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String email;

    private String password;
}
