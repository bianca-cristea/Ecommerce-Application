package org.example.backend.controllers;

import jakarta.validation.Valid;
import org.example.backend.config.AppConstants;
import org.example.backend.models.Category;
import org.example.backend.models.Product;
import org.example.backend.payload.ProductDTO;
import org.example.backend.payload.ProductResponse;
import org.example.backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Port;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId,@Valid @RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.addProduct(categoryId, productDTO), HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "category", required = false) Category category,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder,keyword,category);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductsByCategory(
        @PathVariable Long categoryId,
        @RequestParam(name = "category", required = false) Category category,
        @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
        @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
        @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
        @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder),HttpStatus.OK);

    }


    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name="pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        ProductResponse productResponse = productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(productResponse,HttpStatus.FOUND);
    }


    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long productId){
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
    }

    @DeleteMapping("/admin/prduct/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        return new ResponseEntity<>(productService.deleteProduct(productId),HttpStatus.OK);
    }

}
