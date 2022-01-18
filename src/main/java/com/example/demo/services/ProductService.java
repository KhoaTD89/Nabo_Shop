package com.example.demo.services;

import com.example.demo.model.Product;

import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);

    List<Product> getAll();

    Product getById(String id);

    void delete(String id);
}
