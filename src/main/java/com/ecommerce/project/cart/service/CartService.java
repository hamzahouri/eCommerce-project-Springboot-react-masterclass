package com.ecommerce.project.cart.service;

import com.ecommerce.project.cart.payload.CartDto;

import java.util.List;

public interface CartService {
    CartDto addProductToCart(Long productsId, Integer quantity);

    List<CartDto> getAllCarts();

    CartDto getCart(String email, Long cartId);

    CartDto updateProductQuantity(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);
}
