package org.example.backend.services;

public interface CartService {
    void deleteProductFromCart(Long cartId, Long productId);
}
