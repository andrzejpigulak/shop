package pl.andrzej.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.andrzej.shop.model.dao.User;
import pl.andrzej.shop.repository.RoleRepository;
import pl.andrzej.shop.repository.UserRepository;
import pl.andrzej.shop.security.SecurityUtils;
import pl.andrzej.shop.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;

@RequiredArgsConstructor //generuje wieloargumantowy konstruktor dla finalnych zmiennych
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        roleRepository.findByName("ROLE_USER").ifPresent(role -> user.setRoles(Collections.singletonList(role)));
        return userRepository.save(user); //save - sprawdza czy w user pole id jest ustawione Tak - select czy w bazie istnieje obiekt o takim id jak istnieje to robi update a jak nie to insert
    }

    @Transactional
    @Override
    public User update(User user, Long id) {

        User userDb = searchUserById(id);

        userDb.setFirstName(user.getFirstName());
        userDb.setLastName(user.getLastName());
        userDb.setPhoneNumber(user.getPhoneNumber());

        return userDb;
    }

    @Override
    public User searchUserById(Long id) {
        return userRepository.getById(id); //jeżeli obiektu nie znajdzie po id to zwróci EntityNotFoundException
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> getPage(Pageable pageable) {
        return userRepository.findAll(pageable); // wyswietla wszystkich user na danej stronie
    }

    @Override
    public User getCurrentUser() {
        return userRepository.findByEmail(SecurityUtils.getCurrentEmailUser())
                .orElseThrow(() -> new EntityNotFoundException("User not logged"));
    }
}
