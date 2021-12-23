package pl.andrzej.shop.mapper;

import org.mapstruct.Mapper;
import pl.andrzej.shop.model.dao.Basket;
import pl.andrzej.shop.model.dto.BasketDto;

@Mapper(componentModel = "spring")
public interface BasketMapper {

    BasketDto daoToDto(Basket basket);

    Basket dtoToDao(BasketDto basketDto);
}
