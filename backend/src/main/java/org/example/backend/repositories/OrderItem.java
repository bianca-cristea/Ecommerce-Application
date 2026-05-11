package org.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItem extends JpaRepository<OrderItem,Long> {
}
