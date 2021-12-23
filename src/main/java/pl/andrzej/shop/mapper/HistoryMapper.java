package pl.andrzej.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.history.Revision;
import pl.andrzej.shop.model.dao.Product;
import pl.andrzej.shop.model.dao.User;
import pl.andrzej.shop.model.dto.ProductDto;
import pl.andrzej.shop.model.dto.UserDto;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    @Mapping(source = "requiredRevisionNumber", target = "revisionNumber")
    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.firstName", target = "firstName")
    @Mapping(source = "entity.lastName", target = "lastName")
    @Mapping(source = "entity.email", target = "email")
    @Mapping(source = "entity.login", target = "login")
    @Mapping(source = "entity.phoneNumber", target = "phoneNumber")
    UserDto revisionToUserDto(Revision<Integer, User> revision);

    @Mapping(source = "requiredRevisionNumber", target = "revisionNumber")
    @Mapping(source = "entity.id", target = "id")
    @Mapping(source = "entity.name", target = "name")
    @Mapping(source = "entity.model", target = "model")
    @Mapping(source = "entity.price", target = "price")
    @Mapping(source = "entity.serialNumber", target = "serialNumber")
    ProductDto revisionToProductDto(Revision<Integer, Product> revision);
}
