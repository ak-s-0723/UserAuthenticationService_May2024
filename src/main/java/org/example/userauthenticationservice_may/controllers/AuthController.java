package org.example.userauthenticationservice_may.controllers;


import org.example.userauthenticationservice_may.dtos.*;
import org.example.userauthenticationservice_may.models.User;
import org.example.userauthenticationservice_may.repositories.UserRepository;
import org.example.userauthenticationservice_may.services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private IAuthService authService;
    private final UserRepository userRepository;

    public AuthController(IAuthService authService,
                          UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
        User user = authService.signup(signupRequestDto.getEmail(),signupRequestDto.getPassword());
        return new ResponseEntity<UserDto>(from(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            if (user == null) {
                throw new IllegalArgumentException("Invalid Credentials");
            }

            return new ResponseEntity<UserDto>(from(user),HttpStatus.OK);
        }catch(IllegalArgumentException ex) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequestDto logoutRequestDto) {

    }

    @PostMapping("/forgetPassword")
    public void forgetPassword(@RequestBody ForgetPasswordRequestDto forgetPasswordRequestDto) {

    }


    @PostMapping("/validateToken")
    public void validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {

    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
