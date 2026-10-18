package com.ecommerce.project.cart.service.impl;

import com.ecommerce.project.cart.model.Cart;
import com.ecommerce.project.cart.model.CartItem;
import com.ecommerce.project.cart.payload.CartDto;
import com.ecommerce.project.cart.repositories.CartItemRepository;
import com.ecommerce.project.cart.repositories.CartRepository;
import com.ecommerce.project.cart.service.CartService;
import com.ecommerce.project.category.model.Product;
import com.ecommerce.project.category.payload.ProductDto;
import com.ecommerce.project.category.repository.ProductRepository;
import com.ecommerce.project.exceptions.ApiException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public CartDto addProductToCart(Long productsId, Integer quantity) {

        // find the cart or creat one
       Cart cart = fetchOrBuildUserCart();
        // retrieve product details
        Product product = productRepository.findById(productsId).orElseThrow( ()->
                new ResourceNotFoundException("Product","productId", productsId));

        // valid product if existe in stock and other validaton
        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cart.getId(),productsId);
        if(cartItem !=null) {
            throw new ApiException("Product "+product.getProductName()+"already exist");
        }

        if(product.getQuantity() == 0) {
            throw new ApiException(product.getProductName()+"is not available");
        }

        if(product.getQuantity() < quantity) {
            throw new ApiException("We dont have the requestd quantity please try to request less");
        }

        // create cartItem and associate the product
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setProductPrice(product.getPrice());
        newCartItem.setDiscount(product.getDiscount());
        // save cartItem
        cartItemRepository.save(newCartItem);

        //post cartItem update
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity) );
        cartRepository.save(cart);
        // return updated cart
         CartDto cartDto = modelMapper.map(cart, CartDto.class);

        List<CartItem> cartItems = cart.getItems();
        Stream<ProductDto> productDtoStream = cartItems.stream().map(p-> {
            ProductDto productDto = modelMapper.map(p, ProductDto.class);
            productDto.setQuantity(p.getQuantity());
            return productDto;
        });
        cartDto.setProductDtos(productDtoStream.toList());
        return cartDto;
    }

    @Override
    public List<CartDto> getAllCarts() {

        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new ApiException("No cart exist");
        }

        List<CartDto> cartDtos = carts.stream().map(cart -> {
            CartDto cartDto = modelMapper.map(cart, CartDto.class);
            List<ProductDto> productDtos = cart.getItems().stream().map(product ->
                    modelMapper.map(product.getProduct(), ProductDto.class)).collect(Collectors.toList());
            cartDto.setProductDtos(productDtos);
            return cartDto;
        }).collect(Collectors.toList());

        return cartDtos;
    }

    @Override
    public CartDto getCart(String email, Long cartId) {

        Cart cart = cartRepository.findCartByEmailAndCartId(email, cartId);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart","cartId", cartId);
        }
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        List<ProductDto> productDtos = cart.getItems().stream().map(p->
                modelMapper.map(p.getProduct(), ProductDto.class)).toList();
        cartDto.setProductDtos(productDtos);
        return cartDto;
    }

    @Override
    @Transactional
    public CartDto updateProductQuantity(Long productId, Integer quantity) {
        String email = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getId();
        Cart cart = cartRepository.findById(cartId).orElseThrow( ()->
                new ResourceNotFoundException("Cart", "cart",cartId));

        Product product = productRepository.findById(productId).orElseThrow( ()->
                new ResourceNotFoundException("Product","productId", productId));

        if(product.getQuantity() == 0) {
            throw new ApiException(product.getProductName()+"is not available");
        }

        if(product.getQuantity() < quantity) {
            throw new ApiException("We dont have the requestd quantity please try to request less");
        }

        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cartId,productId);
        if(cartItem == null) {
            throw new ApiException("Product "+product.getProductName()+" Not available in the cart");
        }
        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setDiscount(product.getDiscount());
        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice()));
        cartRepository.save(cart);
        CartItem updatedItem = cartItemRepository.save(cartItem);
        if(updatedItem.getQuantity() == 0){
            cartItemRepository.deleteById(updatedItem.getCartItemId());
        }

        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        List<CartItem> cartItems = cart.getItems();
        List<ProductDto> productDtos = cartItems.stream().map(p->
             modelMapper.map(p, ProductDto.class)
                ).toList();
        cartDto.setProductDtos(productDtos);

        return cartDto;
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow( ()->
                new ResourceNotFoundException("Cart","cart", cartId));
        CartItem cartItem = cartItemRepository.findCartItemByCartIdAndProductId(cartId,productId);
        if(cartItem ==null) {
            throw new ResourceNotFoundException("Product","product", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice()
                - cartItem.getProductPrice() * cartItem.getQuantity());
        cartItemRepository.deleteCartItemBycartIdAndProductId(cartId,productId);
        return "Product "+cartItem.getProduct().getProductName()+" removed from Cart";
    }


    public Cart fetchOrBuildUserCart () {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart !=null){
            return userCart;
        }
        Cart newCart = new Cart();
        newCart.setTotalPrice(0.0);
        newCart.setUser(authUtil.loggedInUser());
        Cart savedCart = cartRepository.save(newCart);
        return savedCart;


    }
}
