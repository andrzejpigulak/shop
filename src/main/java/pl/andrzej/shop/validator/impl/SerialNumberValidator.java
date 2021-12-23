package pl.andrzej.shop.validator.impl;

import pl.andrzej.shop.model.dto.ProductDto;
import pl.andrzej.shop.validator.SerialNumberValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SerialNumberValidator implements ConstraintValidator<SerialNumberValid, ProductDto> {

    @Override
    public boolean isValid(ProductDto productDto, ConstraintValidatorContext constraintValidatorContext) {

        String firstTwoModelCharacters = productDto.getModel().substring(0, 2);

        return productDto.getSerialNumber().startsWith(firstTwoModelCharacters);
    }
}
