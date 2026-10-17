package com.ecommerce.project.category.service;

import com.ecommerce.project.category.model.Category;
import com.ecommerce.project.category.model.Product;
import com.ecommerce.project.category.payload.ProductDto;
import com.ecommerce.project.category.payload.ProductResponse;
import com.ecommerce.project.category.repository.CategoryRepository;
import com.ecommerce.project.category.repository.ProductRepository;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Value("{project.image}")
    private  String path;

    @Override
    public ProductDto addProduct(Long categoryId, ProductDto productDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );
        Product product =modelMapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductResponse getAllProduct() {

        List<Product> products =  productRepository.findAll();
        List<ProductDto> productDtos = products.stream().map(
                p-> modelMapper.map(p,ProductDto.class)).collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category","categoryId",categoryId)
        );

        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        List<ProductDto> productDtos = products.stream().map(
                p-> modelMapper.map(p,ProductDto.class)).collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String productName) {
        List<Product> products = productRepository.findByProductName(productName);

        List<ProductDto> productDtos = products.stream().map(
                p-> modelMapper.map(p,ProductDto.class)).collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDtos);
        return productResponse;
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductDto productDto) {

        Product existingProduct = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product", "productId", productId)
        );

        Product product = modelMapper.map(productDto, Product.class);
        existingProduct.setProductName(product.getProductName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());

        Product updatedProduct =  productRepository.save(existingProduct);

        ProductDto mappedProductDto = modelMapper.map(updatedProduct,ProductDto.class);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(List.of(mappedProductDto));
        return productResponse;
    }

    @Override
    public ProductDto deleteProduct(Long productId) {

        Product existingProduct = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product", "productId", productId)
        );

        // productRepository.delete(existingProduct);
        return modelMapper.map(existingProduct,ProductDto.class);
    }

    @Override
    public ProductDto updateImage(Long productId, MultipartFile image) throws IOException {

        Product existingProduct = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product", "productId", productId)
        );

       // String path = "images/";
        String fileName = uploadImage(path, image);
        existingProduct.setImage(fileName);
        Product  product = productRepository.save(existingProduct);
        return modelMapper.map(product, ProductDto.class);
    }

    private String uploadImage(String path, MultipartFile image) throws IOException {

        String fileName = image.getName();
        String name = "image "+fileName.substring(fileName.lastIndexOf('.'));
        String filePath = path + File.separator + name;
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        Files.copy(image.getInputStream(), Paths.get(filePath));
        return name;
    }
}
