package pl.andrzej.shop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.andrzej.shop.model.dao.User;
import pl.andrzej.shop.model.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true) // ignoruje password
    UserDto daoToDto(User user);

    User dtoToDao(UserDto userDto);
}
