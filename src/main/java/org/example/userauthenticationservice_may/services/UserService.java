package org.example.userauthenticationservice_may.services;

import com.netflix.discovery.provider.Serializer;
import org.example.userauthenticationservice_may.models.User;
import org.example.userauthenticationservice_may.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).get();
    }
}
