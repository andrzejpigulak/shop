package pl.andrzej.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import pl.andrzej.shop.model.dao.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, RevisionRepository<User, Long, Integer> {

    Optional<User> findByEmailOrLogin(String email, String login);

    Optional<User> findByEmail(String email);
}
