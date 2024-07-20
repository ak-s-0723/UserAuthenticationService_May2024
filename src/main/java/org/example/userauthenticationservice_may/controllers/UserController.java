package org.example.userauthenticationservice_may.controllers;

import org.example.userauthenticationservice_may.dtos.UserDto;
import org.example.userauthenticationservice_may.models.User;
import org.example.userauthenticationservice_may.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserDetail(@PathVariable Long id) {
        User user = userService.getUser(id);
        System.out.println(user.getEmail());
        return from(user);
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        //userDto.setRoles(user.getRoles());
        return userDto;
    }


}
