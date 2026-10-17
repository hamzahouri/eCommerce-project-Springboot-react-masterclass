package com.ecommerce.project.cart.payload;

import com.ecommerce.project.category.payload.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long cartId;
    private Double totalPrice = 0.0;

    // should we do this in cart ??
    private List<ProductDto> productDtos = new ArrayList<>();
}
