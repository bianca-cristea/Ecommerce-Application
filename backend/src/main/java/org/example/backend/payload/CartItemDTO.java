package org.example.backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartItemId;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
    private ProductDTO product;
    private CartDTO cart;

}