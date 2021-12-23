package pl.andrzej.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.andrzej.shop.validator.PasswordValid;
import pl.andrzej.shop.validator.group.Create;
import pl.andrzej.shop.validator.group.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
// jeżeli jakieś pole będzie miało wartość null to nie zostanie dodane do odpowiedzi klientowi (użytkownikowi)
@Builder
@PasswordValid(groups = Create.class)
//w tym momencie wyłączyłem działanie adnotacji PasswordValid i włączy się dopiero przy użycu interfejsu Create
public class UserDto {

    private Long id;

    @NotBlank//sprawdza czy nie jest pusty i czy nie ma samych spacji w sobie i czy jest różny od null
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank(groups = Create.class)
    @Email(groups = Create.class)
    private String email;
    @NotBlank(groups = Create.class)
    private String login;
    @NotBlank(groups = Create.class)
    @Length(min = 5, groups = Create.class)
    @Null(groups = Update.class)
    private String password;
    @Null(groups = Update.class)
    private String confirmPassword;
    @NotNull
    private Integer phoneNumber;

    private Integer revisionNumber;
}
