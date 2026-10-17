package com.ecommerce.project.category.service;

import com.ecommerce.project.category.payload.ProductDto;
import com.ecommerce.project.category.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto addProduct(Long categoryId, ProductDto product);

    ProductResponse getAllProduct();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchByKeyword(String productName);

    ProductResponse updateProduct(Long productId, ProductDto product);

    ProductDto deleteProduct(Long productId);

    ProductDto updateImage(Long productId, MultipartFile image) throws IOException;
}
