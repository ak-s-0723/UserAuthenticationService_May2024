package org.example.userauthenticationservice_may.services;

import org.example.userauthenticationservice_may.models.User;

public interface IAuthService {
    User signup(String email, String password);

    User login(String email, String password);
}
