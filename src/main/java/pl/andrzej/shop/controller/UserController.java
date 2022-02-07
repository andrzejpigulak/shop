package pl.andrzej.shop.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.andrzej.shop.mapper.UserMapper;
import pl.andrzej.shop.model.dto.UserDto;
import pl.andrzej.shop.service.UserService;
import pl.andrzej.shop.validator.group.Create;
import pl.andrzej.shop.validator.group.Update;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @Validated(Create.class)
    @PreAuthorize("isAnonymous()")
    public UserDto saveUser(@RequestBody @Valid UserDto user) {
        return userMapper.daoToDto(userService.save(userMapper.dtoToDao(user)));
    }

    @ApiOperation(value = "Get User By Id", authorizations = @Authorization(value = "JWT"))
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN')")
    public UserDto getUserById(@PathVariable Long id) {
        return userMapper.daoToDto(userService.searchUserById(id));
    }

    @ApiOperation(value = "Update User", authorizations = @Authorization(value = "JWT"))
    @PutMapping("/{id}")
    @Validated(Update.class)
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN') || @securityService.hasAccessToUser(#id)")
    public UserDto updateUser(@RequestBody @Valid UserDto user, @PathVariable Long id) {
        return userMapper.daoToDto(userService.update(userMapper.dtoToDao(user), id));
    }

    @ApiOperation(value = "Delete User By Id", authorizations = @Authorization(value = "JWT"))
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated() && hasRole('ADMIN') || @securityService.hasAccessToUser(#id)")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @ApiOperation(value = "User Page", authorizations = @Authorization(value = "JWT"))
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserDto> pageUser(@RequestParam int page, @RequestParam int size) {
        return userService.getPage(PageRequest.of(page, size))
                .map(userMapper::daoToDto);
    }

    @ApiOperation(value = "Get Current User", authorizations = @Authorization(value = "JWT"))
    @GetMapping("/current")
    @PreAuthorize("isAuthenticated()")
    public UserDto getCurrentUser() {
        return userMapper.daoToDto(userService.getCurrentUser());
    }

}
