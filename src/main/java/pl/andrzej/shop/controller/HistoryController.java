package pl.andrzej.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.andrzej.shop.mapper.HistoryMapper;
import pl.andrzej.shop.model.dto.ProductDto;
import pl.andrzej.shop.model.dto.UserDto;
import pl.andrzej.shop.repository.ProductRepository;
import pl.andrzej.shop.repository.UserRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/history")
public class HistoryController {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final HistoryMapper historyMapper;

    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN') || @securityService.hasAccessToUser(#id)")
    Page<UserDto> getUserHistory(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        return userRepository.findRevisions(id, PageRequest.of(page, size))
                .map(historyMapper::revisionToUserDto);
    }

    @GetMapping("/product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    Page<ProductDto> getProductHistory(@PathVariable Long id, @RequestParam int page, @RequestParam int size) {
        return productRepository.findRevisions(id, PageRequest.of(page, size))
                .map(historyMapper::revisionToProductDto);
    }

}
