package pl.andrzej.shop.service;

import pl.andrzej.shop.model.dao.Product;

import java.util.List;

public interface BasketService {

    List<Product> addProduct(Long productId, Integer quantity);

    List<Product> update(Long productId, Integer quantity);

    List<Product> get();

    void removeProductFromBasket(Long productId);
}