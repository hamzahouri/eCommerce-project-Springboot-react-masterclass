package com.ecommerce.project.cart.controller;

import com.ecommerce.project.cart.model.Cart;
import com.ecommerce.project.cart.payload.CartDto;
import com.ecommerce.project.cart.repositories.CartRepository;
import com.ecommerce.project.cart.service.CartService;
import com.ecommerce.project.utils.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    private final AuthUtil authUtil;

    private final CartRepository cartRepository;

    @PostMapping("/carts/products/{productsId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addProductToCart (@PathVariable Long productsId,
                                                     @PathVariable Integer quantity) {
        CartDto cartDto = cartService.addProductToCart(productsId,quantity);

        return new ResponseEntity<CartDto>(cartDto, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDto>> getCarts () {
     List<CartDto> cartDtos = cartService.getAllCarts();

     return new ResponseEntity<>(cartDtos, HttpStatus.FOUND);
    }

    public ResponseEntity<CartDto> getCartById () {

        String email = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Long cartId = cart.getId();
        CartDto cartDto = cartService.getCart(email, cartId);
        return new ResponseEntity<>(cartDto,HttpStatus.OK);
    }

    @PutMapping("/cart/product/{productId}/operation/{operation}")
    public ResponseEntity<CartDto> updateCartProduct (@PathVariable Long productId,
                                                      @PathVariable String operation) {
              CartDto cartDto = cartService.updateProductQuantity(productId,
                      operation.equalsIgnoreCase("delete") ? -1 : 1);

              return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @DeleteMapping("/cart/{cartId}/product/{productId}")
    public ResponseEntity<String> deletefromCart (@PathVariable Long cartId,
                                                  @PathVariable Long productId) {

        String status = cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }
}
