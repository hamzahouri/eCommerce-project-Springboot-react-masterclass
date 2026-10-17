package com.ecommerce.project.category.repository;

import com.ecommerce.project.category.model.Category;
import com.ecommerce.project.category.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategoryOrderByPriceAsc(Category category );

    List<Product> findByProductName(String productName);


}
