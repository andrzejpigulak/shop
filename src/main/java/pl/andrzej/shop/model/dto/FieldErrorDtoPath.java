package pl.andrzej.shop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.Path;

@Data
@AllArgsConstructor
public class FieldErrorDtoPath {

    private Path path;
    private String message;
}
