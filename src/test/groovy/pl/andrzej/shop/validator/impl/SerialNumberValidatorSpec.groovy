package pl.andrzej.shop.validator.impl

import pl.andrzej.shop.model.dto.ProductDto
import spock.lang.Specification

class SerialNumberValidatorSpec extends Specification {

    def serialNumberValidator = new SerialNumberValidator();

    def 'Should Test Serial Number Validator'() {
        given:
        def productDto = new ProductDto(serialNumber: serialNumber, model: model)

        when:
        def result = serialNumberValidator.isValid(productDto, null)

        then:
        result == expected

        where:
        serialNumber | model || expected
        "13ASDF"     | "13.01"  || true
        "13ASDF"     | "14.01"  || false
    }

}
