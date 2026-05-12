package org.example.backend.services;

import org.example.backend.models.Category;
import org.example.backend.payload.ProductDTO;
import org.example.backend.payload.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        return null;
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, Category category) {
        return null;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        return null;
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        return null;
    }
}
