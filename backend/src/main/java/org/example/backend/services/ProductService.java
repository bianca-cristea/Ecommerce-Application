package org.example.backend.services;

import jakarta.validation.Valid;
import org.example.backend.models.Category;
import org.example.backend.payload.ProductDTO;
import org.example.backend.payload.ProductResponse;

public interface ProductService {


    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, Category category);

    ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductDTO updateProduct(Long productId, @Valid ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);
}
