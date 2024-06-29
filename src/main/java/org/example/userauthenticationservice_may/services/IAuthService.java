package org.example.userauthenticationservice_may.services;

import org.example.userauthenticationservice_may.models.User;
import org.springframework.util.MultiValueMap;
import org.antlr.v4.runtime.misc.Pair;

public interface IAuthService {
    User signup(String email, String password);

    Pair<User, MultiValueMap<String,String>> login(String email, String password);

    Boolean validateToken(Long userId, String token);
}
