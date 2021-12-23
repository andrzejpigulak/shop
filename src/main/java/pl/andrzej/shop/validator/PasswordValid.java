package pl.andrzej.shop.validator;

import pl.andrzej.shop.validator.impl.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE) //Target mówi w którym miejscu adnotacja może być używana a Type że w tym miejscu żeby ta adnotacja mogła być używana na klasach
@Retention(RetentionPolicy.RUNTIME) // Retention - w który momencie działania kodu będzie ta adnotacja uruchamiana, Runtime - w trakcie działania kodu
@Constraint(validatedBy = PasswordValidator.class) // podpinamy implementacje do adnotacji
public @interface PasswordValid {

    String message() default "Confirm password should be the same"; //komunikat który sie wyświetli w błędzie

    Class<?>[] groups() default {}; //pozwala włączać adnotację tylko do pojedynczych metod; nie działą z default-u tylko trzeba to włączyć

    Class<? extends Payload>[] payload() default {}; // dodatkowy obiekt do błędu

}
