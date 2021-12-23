package pl.andrzej.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean //tylko dla metod rejestruje to co zostało zwrócone z metody jako bean w springu
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
