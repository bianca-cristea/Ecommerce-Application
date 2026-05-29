package org.example.backend.controllers;

import org.example.backend.payload.CartDTO;
import org.example.backend.repositories.CartRepository;
import org.example.backend.services.CartService;
import org.example.backend.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;


    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToTheCart( @PathVariable Long productId, @PathVariable Integer quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }


    //this would be needed for an admin pannel
    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts(){
        return new ResponseEntity<>(cartService.getAllCarts(),HttpStatus.OK);
    }

    @GetMapping("/carts/users/cart")  //ptc un user are un singur cart
    public ResponseEntity<CartDTO> getCartById(){
        String email = authUtil.loggedInEmail();
        Long cartId = cartRepository.findCartByEmail(email).getCartId();

        // foloseste si cartId, pentru a verifica inca o data ca e cosul care treebuie, insa ar fi putut si doar cu email
        return new ResponseEntity<>(cartService.getCart(email,cartId),HttpStatus.OK);
    }


    // | - 1 + |   se gaseste ff des in ecommerce apps
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartQuantity(@PathVariable Long productId, @PathVariable  String operation){

        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);


        return new ResponseEntity<>(cartDTO,HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,
                                                        @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);

        return new ResponseEntity<String>(status, HttpStatus.OK);
    }




}
