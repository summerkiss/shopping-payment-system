package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Map;

import com.example.demo.exception.NotEnoughProductsInStockException;
import com.example.demo.model.Product;

public interface ShoppingCartService {
	void addProduct(Product product);

    void removeProduct(Product product);

    Map<Product, Integer> getProductsInCart();

    void checkout() throws NotEnoughProductsInStockException;

    BigDecimal getTotal();
}
