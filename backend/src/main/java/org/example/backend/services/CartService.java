package org.example.backend.services;

import jakarta.transaction.Transactional;
import org.example.backend.payload.CartDTO;
import org.example.backend.payload.CartItemDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> getAllCarts();

    CartDTO getCart(String email, Long cartId);

    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    @Transactional
    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);

    @Transactional
    String createOrUpdateCartWithItems(List<CartItemDTO> cartItems);
}
