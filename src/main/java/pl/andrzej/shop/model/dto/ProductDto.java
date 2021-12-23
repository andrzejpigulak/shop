package pl.andrzej.shop.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.andrzej.shop.validator.SerialNumberValid;
import pl.andrzej.shop.validator.group.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@SerialNumberValid(groups = Create.class)
public class ProductDto {

    private Long id;
    private String name;
    private String model;
    @Positive
    private Double price;
    private Integer stockLevel;
    @NotBlank(groups = Create.class)
    private String serialNumber;
    private Integer revisionNumber;
}
