package org.example.backend.services;

import org.example.backend.exceptions.APIException;
import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.Cart;
import org.example.backend.models.CartItem;
import org.example.backend.models.Category;
import org.example.backend.models.Product;
import org.example.backend.payload.CartDTO;
import org.example.backend.payload.ProductDTO;
import org.example.backend.payload.ProductResponse;
import org.example.backend.repositories.CategoryRepository;
import org.example.backend.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository cartRepository;

    @Autowired
    private CartService cartService;



    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();
        for(Product product : products){
            if(product.getProductName().equalsIgnoreCase(productDTO.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent){
            Product product = modelMapper.map(productDTO,Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() - (product.getDiscount() * 0.01) * product.getPrice();
            product.setSpecialPrice(specialPrice);

            //product.setUser();
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct,ProductDTO.class);
        } else {
            throw new APIException("Product is already present.");
        }
    }




    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String keyword, Category category) {

    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        @Override
        public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

            Product productFromDB = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product", "productId", productId));


            productFromDB.setProductName(productDTO.getProductName());
            productFromDB.setDescription(productDTO.getDescription());
            productFromDB.setQuantity(productDTO.getQuantity());
            productFromDB.setPrice(productDTO.getPrice());
            productFromDB.setDiscount(productDTO.getDiscount());


            double specialPrice =
                    productDTO.getPrice()
                            - (productDTO.getDiscount() * 0.01) * productDTO.getPrice();

            productFromDB.setSpecialPrice(specialPrice);

            // Salvăm produsul actualizat
            Product savedProduct = productRepository.save(productFromDB);

            // Găsim toate carturile care conțin produsul
            List<Cart> carts = cartRepository.findCartsByProductId(productId);

            // Recalculăm carturile afectate
            carts.forEach(cart ->
                    cartService.updateProductInCarts(cart.getCartId(), productId));

            // Transformăm produsul în DTO pentru response
            return modelMapper.map(savedProduct, ProductDTO.class);
        }

    }


    @Override
    public ProductDTO deleteProduct(Long productId) {

    }
}
