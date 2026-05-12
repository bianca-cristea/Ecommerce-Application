package org.example.backend.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long productId;
    private String description;
    private Double discount;
    //private String  image;
    private Double price;
    private String productName;
    private Integer quantity;
    //private Double specialPrice;

}
