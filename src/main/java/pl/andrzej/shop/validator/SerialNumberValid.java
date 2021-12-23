package pl.andrzej.shop.validator;

import pl.andrzej.shop.validator.impl.SerialNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SerialNumberValidator.class)
public @interface SerialNumberValid {

    String message() default "Serial number should have first two characters from model"; //komunikat który sie wyświetli w błędzie

    Class<?>[] groups() default {}; //pozwala włączać adnotację tylko do pojedynczych metod; nie działą z default-u tylko trzeba to włączyć

    Class<? extends Payload>[] payload() default {}; // dodatkowy obiekt do błędu
}
