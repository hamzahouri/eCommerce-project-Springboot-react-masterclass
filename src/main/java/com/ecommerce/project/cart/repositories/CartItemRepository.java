package com.ecommerce.project.cart.repositories;

import com.ecommerce.project.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.productId = ?2")
    CartItem findCartItemByCartIdAndProductId(Long id, Long productsId);

    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.productId = ?2")
    void deleteCartItemBycartIdAndProductId(Long cartId, Long productId);
}
