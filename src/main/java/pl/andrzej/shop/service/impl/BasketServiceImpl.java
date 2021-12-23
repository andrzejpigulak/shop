package pl.andrzej.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.andrzej.shop.model.dao.Basket;
import pl.andrzej.shop.model.dao.Product;
import pl.andrzej.shop.model.dao.User;
import pl.andrzej.shop.repository.BasketRepository;
import pl.andrzej.shop.service.BasketService;
import pl.andrzej.shop.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final UserServiceImpl userService;
    private final ProductService productService;

    @Override
    public List<Product> addProduct(Long productId, Integer quantity) {
        User user = userService.getCurrentUser();
        Product product = productService.searchProductById(productId);

        Basket basketDb = Basket.builder()
                .product(product)
                .user(user)
                .quantity(quantity)
                .build();

        if (product.getStockLevel() < quantity) {
            basketDb.setQuantity(product.getStockLevel());
        }
        basketRepository.save(basketDb);

        return get();
    }

    @Override
    @Transactional
    public List<Product> update(Long productId, Integer quantity) {
        User user = userService.getCurrentUser();
        Product product = productService.searchProductById(productId);

        Optional<Basket> optionalBasket = basketRepository.findByProductIdAndUserId(productId, user.getId());

        if (optionalBasket.isEmpty())
            return addProduct(productId, quantity);
        Basket basket = optionalBasket.get();
        if (product.getStockLevel() < quantity) {
            basket.setQuantity(product.getStockLevel());
        } else {
            basket.setQuantity(quantity);
        }
        return get();
    }

    @Override
    public List<Product> get() {
        return basketRepository.findByUserId(userService.getCurrentUser().getId()).stream()
                .map(basket -> {
                    Product product = basket.getProduct();
                    product.setStockLevel(basket.getQuantity());
                    return product;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void removeProductFromBasket(Long productId) {
        Long userId = userService.getCurrentUser().getId();
        basketRepository.deleteByUserIdAndProductId(userId, productId);
    }

}
