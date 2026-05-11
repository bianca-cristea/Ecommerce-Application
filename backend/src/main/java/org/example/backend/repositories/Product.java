package org.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface Product extends JpaRepository<Product,Long> {
}
