package org.example.backend.config;

import org.example.backend.models.Category;
import org.example.backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public void run(String... args) throws Exception {

        if(categoryRepository.count() == 0) {
            Category category = new Category("casual");
            Category category1 = new Category("sport");

            categoryRepository.save(category);
            categoryRepository.save(category1);
        }
    }
}
