package org.example.backend.services;

import org.example.backend.exceptions.APIException;
import org.example.backend.exceptions.ResourceNotFoundException;
import org.example.backend.models.Cart;
import org.example.backend.models.Category;
import org.example.backend.models.Product;
import org.example.backend.payload.CategoryDTO;
import org.example.backend.payload.ProductDTO;
import org.example.backend.payload.ProductResponse;
import org.example.backend.repositories.CartRepository;
import org.example.backend.repositories.CategoryRepository;
import org.example.backend.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private  FileService fileService;



    @Value("${project.image}")
    private String path;

    @Value("${image.base.url}")
    private String imageBaseUrl;


    @Override
    public ProductResponse getAllProducts(String keyword, String category, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Specification spec = (root,query,cb) -> cb.conjunction();

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), "%" + keyword.toLowerCase() + "%"));
        }

        if(category != null && !category.isEmpty()){
            spec = spec.and((root,query,cb) -> cb.like(cb.lower(root.get("categoryName")),category));
        }

        Page<Product> pageProducts = productRepository.findAll(spec,pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
                    productDTO.setImage(constructImageUrl(product.getImage()));
                    return productDTO;
                })
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productResponse.getPageNumber());
        productResponse.setPageSize(productResponse.getPageSize());
        productResponse.setTotalPages(productResponse.getTotalPages());
        productResponse.setTotalElements(productResponse.getTotalElements());
        productResponse.setLastPage(productResponse.isLastPage());

        return productResponse;
    }

    private String constructImageUrl(String imageName){
        return imageBaseUrl + (imageBaseUrl.endsWith("/") ? "" : "/") + imageName;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                                Sort.by(sortBy).ascending():
                                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepository.findProductsByProductNameLikeIgnoreCase("%"+keyword+"%",pageDetails);

        List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products.stream().map(prod -> modelMapper.map(prod,ProductDTO.class)).toList();

        if(products.isEmpty()) throw new APIException("Products not found.");

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());


        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Product> pageProducts = productRepository.findByCategoryCategoryId(categoryId,pageDetails);

        List<Product> products = pageProducts.getContent();
        if(products.isEmpty()) throw new APIException("Category does not have any products.");

        List<ProductDTO> productDTOS = products.stream().map(
                prod -> modelMapper.map(prod,ProductDTO.class)
        ).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setLastPage(pageProducts.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();

        for(Product product : products){
            if(product.getProductName().equals(productDTO.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent) {

            Product newProd = new Product();
            newProd.setImage("default.png");
            newProd.setCategory(category);
            //newProd.setUser();
            newProd.setProductName(productDTO.getProductName());
            newProd.setPrice(productDTO.getPrice());
            newProd.setDescription(productDTO.getDescription());
            newProd.setQuantity(productDTO.getQuantity());
            newProd.setDiscount(productDTO.getDiscount());
            double specialPrice = productDTO.getPrice() - (productDTO.getDiscount() * 0.01) * productDTO.getPrice();
            newProd.setSpecialPrice(specialPrice);

            Product savedProd = productRepository.save(newProd);
            return modelMapper.map(savedProd,ProductDTO.class);

        }
        else {
            throw new APIException("Product already exists in the category.");
        }

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product prodFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId", productId));

        prodFromDB.setProductName(productDTO.getProductName());
        prodFromDB.setPrice(productDTO.getPrice());
        prodFromDB.setDescription(productDTO.getDescription());
        prodFromDB.setQuantity(productDTO.getQuantity());
        prodFromDB.setDiscount(productDTO.getDiscount());
        double specialPrice = productDTO.getPrice() - (productDTO.getDiscount() * 0.01) * productDTO.getPrice();
        prodFromDB.setSpecialPrice(specialPrice);

        Product savedProd = productRepository.save(prodFromDB);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart ->
                cart.getCartItems().forEach(item -> {
                    if(item.getProduct().getProductId().equals(productId)){
                        item.setProduct(savedProd);
                    }
                }));

        return modelMapper.map(savedProd,ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(),productId));

        productRepository.delete(product);
        return modelMapper.map(product,ProductDTO.class);

    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);
        productFromDb.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }





    @Override
    public ProductResponse getAllProductsForSeller(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }

    @Override
    public ProductResponse getAllProductsForAdmin(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        return null;
    }


}
