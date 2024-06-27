package org.example.userauthenticationservice_may.repositories;

import org.example.userauthenticationservice_may.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {
}
