package pl.andrzej.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.andrzej.shop.model.dao.Basket;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    void deleteByUserIdAndProductId(Long userId, Long productId);

    Optional<Basket> findByProductIdAndUserId(Long productId, Long userId);

    List<Basket> findByUserId(Long userId);


}
