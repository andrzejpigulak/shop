package pl.andrzej.shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.andrzej.shop.model.dao.User;

public interface UserService {

    User save(User user);

    User update(User user, Long id);

    User searchUserById(Long id);

    void deleteUserById(Long id);

    Page<User> getPage(Pageable pageable); //stronnicowanie //Pageable - obiekt na podstawie którego jest tworzone stronnicowanie bazodanowe

    User getCurrentUser();
}
