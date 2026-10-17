package com.ecommerce.project.category.controller;

import com.ecommerce.project.category.model.Product;
import com.ecommerce.project.category.payload.ProductDto;
import com.ecommerce.project.category.payload.ProductResponse;
import com.ecommerce.project.category.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto product,
                                                 @PathVariable Long categoryId) {

        ProductDto dto = productService.addProduct(categoryId,product);

        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse productResponse = productService.getAllProduct();

        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }


    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId) {

        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String productName) {

        ProductResponse productResponse = productService.searchByKeyword(productName);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }


    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponse> updateProduct (@RequestBody ProductDto product,
                                                          @PathVariable Long productId) {

        ProductResponse upadtedProduct = productService.updateProduct(productId,product);
      return new ResponseEntity<>(upadtedProduct, HttpStatus.OK);

    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Long productId) {

        ProductDto dto = productService.deleteProduct(productId);
      return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/admin/products/{ProductId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long ProductId,
                                                         @RequestParam("image") MultipartFile image) throws IOException {

          ProductDto updatedProduct =  productService.updateImage(ProductId, image);

          return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }
}
