package org.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface User extends JpaRepository<User,Long> {
}
