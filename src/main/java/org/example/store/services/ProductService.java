package org.example.store.services;

import lombok.RequiredArgsConstructor;
import org.example.store.entities.Product;
import org.example.store.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product){

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());

        return productRepository.save(existingProduct);
    }

    public List<Product> getProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found with id " + id));

        productRepository.delete(product);
    }
}