package com.ecommerce.project.cart.payload;

import com.ecommerce.project.category.payload.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long cartItemDto;
    private CartDto cartDto;
    private ProductDto productDto;
    private Double discount;
    private Double productPrice;
    private Integer quantity;
}
