package org.example.backend.repositories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.example.backend.models.Cart;
import org.example.backend.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
        Category findByCategoryName(@NotBlank @Size(min = 5, message = "Name must contain at least 5 characters") String categoryName);

}
