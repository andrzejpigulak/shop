package pl.andrzej.shop.validator.impl

import pl.andrzej.shop.model.dto.UserDto
import spock.lang.Specification

class PasswordValidatorSpec extends Specification {

    def passwordValidator = new PasswordValidator()

    def 'Should Test Password Validator'() {
        given:
        def userDto = new UserDto(password: password, confirmPassword: confirmPassword)

        when:
        def result = passwordValidator.isValid(userDto, null)

        then:
        result == expected

        where:
        password | confirmPassword || expected
        "asdf"   | "asdf"          || true
        "asdf"   | "fdsa"          || false
    }

}
