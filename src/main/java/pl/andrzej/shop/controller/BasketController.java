package pl.andrzej.shop.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.andrzej.shop.mapper.ProductMapper;
import pl.andrzej.shop.model.dto.BasketDto;
import pl.andrzej.shop.model.dto.ProductDto;
import pl.andrzej.shop.service.BasketService;

import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    private final ProductMapper productMapper;

    @ApiOperation(value = "Add Product To Basket", authorizations = @Authorization(value = "JWT"))
    @PostMapping
    public List<ProductDto> addProduct(@RequestBody BasketDto basketDto) {
        return productMapper.daoListToDtoList(basketService.addProduct(basketDto.getProductId(), basketDto.getQuantity()));
    }

    @ApiOperation(value = "Update Product In Basket", authorizations = @Authorization(value = "JWT"))
    @PutMapping
    public List<ProductDto> updateBasket(@RequestBody BasketDto basketDto) {
        return productMapper.daoListToDtoList(basketService.update(basketDto.getProductId(), basketDto.getQuantity()));
    }

    @ApiOperation(value = "Remove Product From Basket", authorizations = @Authorization(value = "JWT"))
    @DeleteMapping("/{productId}")
    public void removeProductFromBasket(@PathVariable Long productId) {
        basketService.removeProductFromBasket(productId);
    }

    @ApiOperation(value = "Show Products From Basket", authorizations = @Authorization(value = "JWT"))
    @GetMapping
    public List<ProductDto> getProducts() {
        return productMapper.daoListToDtoList(basketService.get());
    }

}
