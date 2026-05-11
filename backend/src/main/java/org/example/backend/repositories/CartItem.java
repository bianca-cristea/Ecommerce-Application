package org.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItem extends JpaRepository<CartItem,Long> {
}
