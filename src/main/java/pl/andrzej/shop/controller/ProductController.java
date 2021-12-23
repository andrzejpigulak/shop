package pl.andrzej.shop.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.andrzej.shop.mapper.ProductMapper;
import pl.andrzej.shop.model.dto.ProductDto;
import pl.andrzej.shop.service.ProductService;
import pl.andrzej.shop.validator.group.Create;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping
    @Validated(Create.class)
    public ProductDto saveProduct(@RequestPart @Valid ProductDto product, @RequestPart MultipartFile file) {
        return productMapper.daoToDTo(productService.save(productMapper.dtoToDao(product), file));
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        return productMapper.daoToDTo(productService.searchProductById(id));
    }

    @ApiOperation(value = "Update Product", authorizations = @Authorization(value = "JWT"))
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDto updateProduct(@RequestPart ProductDto product, @PathVariable Long id, @RequestPart(required = false) MultipartFile file) {
        return productMapper.daoToDTo(productService.update(productMapper.dtoToDao(product), id, file));
    }

    @ApiOperation(value = "Delete Product By Id", authorizations = @Authorization(value = "JWT"))
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @GetMapping
    public Page<ProductDto> pageProduct(@RequestParam int page, @RequestParam int size) {
        return productService.getPage(PageRequest.of(page, size))
                .map(productMapper::daoToDTo);
    }
}
