package org.example.userauthenticationservice_may.repositories;

import org.example.userauthenticationservice_may.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
     Optional<User> findByEmail(String email);

     User save(User user);

}
