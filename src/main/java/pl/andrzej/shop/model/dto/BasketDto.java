package pl.andrzej.shop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketDto {

    @Positive
    private Integer quantity;
    @Positive
    @NotNull
    private Long productId;

}
