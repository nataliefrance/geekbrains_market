package ru.shipova.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.shipova.market.entities.Product;
import ru.shipova.market.repositories.ProductsRepository;

import java.util.List;

//не функциональный класс, создан для понимания работы RestController и тестирования его с Postman
@RestController //каждому методу подшивается @ResponceBody
@RequestMapping("/api/v1/products")
public class ProductRestController {
    private ProductsRepository productsRepository;

    @Autowired
    public void setProductsRepository(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    //http://localhost:8189/market/api/v1/products/
    @GetMapping("/")
    public List<Product> findAll() {
        return (List<Product>) productsRepository.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED) //меняем статус на 201
    public Product addNewProduct(@RequestBody Product product) {
        product.setId(null);
        return productsRepository.save(product);
    }

    @GetMapping("/{id}")
    public Product findOne(@PathVariable Long id) {
        return productsRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Product saveOrUpdate(@PathVariable Long id, @RequestBody Product newProduct) {
         productsRepository.findById(id)
                .ifPresent(product -> {
                    product.setTitle(newProduct.getTitle());
                    product.setPrice(newProduct.getPrice());
                    productsRepository.save(product);
                });

        return productsRepository.findById(id)
                .map(product -> {
                    product.setTitle(newProduct.getTitle());
                    product.setPrice(newProduct.getPrice());
                    return productsRepository.save(product);
                })
                .orElseThrow();
    }

    @PutMapping("/")
    public Product updateProduct(@RequestBody Product product) {
        return productsRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productsRepository.deleteById(id);
    }
}
