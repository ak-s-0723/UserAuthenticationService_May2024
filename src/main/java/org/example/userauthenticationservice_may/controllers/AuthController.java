package org.example.userauthenticationservice_may.controllers;


import org.example.userauthenticationservice_may.dtos.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @PostMapping
    public UserDto signup(@RequestBody SingupRequestDto singupRequestDto) {
      return null;
    }

    @PostMapping
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto) {
      return null;
    }

    @PostMapping
    public void logout(@RequestBody LogoutRequestDto logoutRequestDto) {

    }

    @PostMapping
    public void forgetPassword(@RequestBody ForgetPasswordRequestDto forgetPasswordRequestDto) {

    }


    @PostMapping
    public void validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {

    }
}
