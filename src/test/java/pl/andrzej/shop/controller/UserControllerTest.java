package pl.andrzej.shop.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import pl.andrzej.shop.model.dao.User;
import pl.andrzej.shop.model.dto.UserDto;
import pl.andrzej.shop.repository.UserRepository;
import spock.util.mop.Use;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest //używana po to aby odpalić testową aplikację springboot-ową
@AutoConfigureMockMvc //tworzy beana który pozwala wysyłać requesty na endpointy controllera
@ActiveProfiles("test") //test dajemy bazując na nazwie naszego yaml-a to co jest po myślniku application-test
@TestPropertySource(locations = "classpath:application-test.yml") // classpath oznacza folder resources. Chodzi o podanie pliku konfiguracyjnego w adnotacji
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc; //połączone z @AutoConfigureMockMvc czyli jest to ten bean

    @Autowired
    private ObjectMapper objectMapper; //Pozwala mapować obiekty javowe na jsona i odwrotnie

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() throws Exception {
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Maria")
                                .lastName("Nowak")
                                .login("mnowak")
                                .email("maria.nowak@wp.pl")
                                .phoneNumber(666777888)
                                .password("a1s2d3f")
                                .confirmPassword("a1s2d3f")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Maria"))
                .andExpect(jsonPath("$.lastName").value("Nowak"))
                .andExpect(jsonPath("$.email").value("maria.nowak@wp.pl"))
                .andExpect(jsonPath("$.login").value("mnowak"))
                .andExpect(jsonPath("$.phoneNumber").value(666777888))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void shouldReturnConflictWhenDuplicateEmail() throws Exception {
        userRepository.save(User.builder()
                .firstName("Katarzyna")
                .lastName("Kowalska")
                .login("kkowalska")
                .email("katarzyna.kowalska@wp.pl")
                .phoneNumber(888777666)
                .password("asdfqwer")
                .build());
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Maria")
                                .lastName("Nowak")
                                .login("mnowak")
                                .email("katarzyna.kowalska@wp.pl")
                                .phoneNumber(666777888)
                                .password("a1s2d3f")
                                .confirmPassword("a1s2d3f")
                                .build())))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exist"));
    }

    @Test
    void shouldReturnMismatchPasswordAndConfirmPassword() throws Exception {
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Maria")
                                .lastName("Nowak")
                                .login("mnowak")
                                .email("katarzyna.kowalska@wp.pl")
                                .phoneNumber(666777888)
                                .password("a1s2d3f")
                                .confirmPassword("b1s2d3f")
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName(" ")
                                .lastName(" ")
                                .login(" ")
                                .email(" ")
                                .phoneNumber(0)
                                .password(" ")
                                .confirmPassword(" ")
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].field", containsInAnyOrder("firstName", "lastName")))
                .andExpect(jsonPath("$.[*].message", containsInAnyOrder("must not be blank", "must not be blank")));
    }

    @Test
    void shouldReturnBadRequestRestData() throws Exception {
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Andrzej")
                                .lastName("Pigulak")
                                .login(" ")
                                .email(" ")
                                .phoneNumber(0)
                                .password(" ")
                                .confirmPassword(" ")
                                .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[*].field", containsInAnyOrder("saveUser.user.login", "saveUser.user.email", "saveUser.user.email", "saveUser.user.password", "saveUser.user.password")))
                .andExpect(jsonPath("$.[*].message", containsInAnyOrder("must not be blank", "length must be between 5 and 2147483647", "must be a well-formed email address", "must not be blank", "must not be blank")));
    }

    @Test
    @WithMockUser
    void shouldReturnAccessDeniedWhenLogedIn() throws Exception {
        mockMvc.perform(post("/api/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Maria")
                                .lastName("Nowak")
                                .login("mnowak")
                                .email("maria.nowak@wp.pl")
                                .phoneNumber(666777888)
                                .password("a1s2d3f")
                                .confirmPassword("a1s2d3f")
                                .build())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserByIdWhenLogedInAndRoleAdmin() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("Maria")
                .lastName("Nowak")
                .login("mnowak")
                .email("maria.nowak@wp.pl")
                .phoneNumber(666777888)
                .password("a1s2d3f")
                .build());
        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundWhenSearchUserByIdWithNotExistingId() throws Exception {
        mockMvc.perform(get("/api/users/" + 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser
    void shouldReturnForbiddenWhenSearchUserById() throws Exception {
        mockMvc.perform(get("/api/users/" + 1))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldGetPageUser() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("Maria")
                .lastName("Nowak")
                .login("mnowak")
                .email("maria.nowak@wp.pl")
                .phoneNumber(666777888)
                .password("a1s2d3f")
                .build());
        mockMvc.perform(get("/api/users/")
                        .param("page", "0")
                        .param("size", "25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1L))
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(user.getId().intValue())))
                .andExpect(jsonPath("$.content[*].firstName", containsInAnyOrder(user.getFirstName())))
                .andExpect(jsonPath("$.content[*].lastName", containsInAnyOrder(user.getLastName())))
                .andExpect(jsonPath("$.content[*].login", containsInAnyOrder(user.getLogin())))
                .andExpect(jsonPath("$.content[*].email", containsInAnyOrder(user.getEmail())))
                .andExpect(jsonPath("$.content[*].phoneNumber", containsInAnyOrder(user.getPhoneNumber())))
                .andExpect(jsonPath("$.content[*].password").doesNotExist())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.size").value(25))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @WithMockUser(username = "maria.nowak@wp.pl")
    void shoudGetCurrentUser() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("Maria")
                .lastName("Nowak")
                .login("mnowak")
                .email("maria.nowak@wp.pl")
                .phoneNumber(666777888)
                .password("a1s2d3f")
                .build());
        mockMvc.perform(get("/api/users/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void shouldNotGetCurrentUser() throws Exception {
        mockMvc.perform(get("/api/users/current"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateUser() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("Maria")
                .lastName("Nowak")
                .login("mnowak")
                .email("maria.nowak@wp.pl")
                .phoneNumber(666777888)
                .password("a1s2d3f")
                .build());
        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(UserDto.builder()
                                .firstName("Anna")
                                .lastName("Skrzypczak")
                                .phoneNumber(654456456)
                                .email("andrzejp@wp.pl")
                                .login("login")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Skrzypczak"))
                .andExpect(jsonPath("$.phoneNumber").value(654456456))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(username = "login")
    void shouldNotUpdateUserWhenLoggedWithRoleUser() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Anna")
                                .lastName("Skrzypczak")
                                .phoneNumber(654456456)
                                .email("andrzejp@wp.pl")
                                .login("login")
                                .build())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotUpdateWhenUserDoesNotExists() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Anna")
                                .lastName("Skrzypczak")
                                .phoneNumber(654456456)
                                .email("andrzejp@wp.pl")
                                .login("login")
                                .build())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "andrzejp@wp.pl")
    void shouldUpdateOwnData() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("Anna")
                .lastName("Skrzypczak")
                .login("mnowak")
                .email("andrzejp@wp.pl")
                .phoneNumber(654456456)
                .password("a1s2d3f")
                .build());
        mockMvc.perform(put("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UserDto.builder()
                                .firstName("Andrzej")
                                .lastName("Pigulak")
                                .phoneNumber(633445566)
                                .email("andrzejp@wp.pl")
                                .login("login")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Andrzej"))
                .andExpect(jsonPath("$.lastName").value("Pigulak"))
                .andExpect(jsonPath("$.phoneNumber").value(633445566))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteUserById() throws Exception {
        User user = userRepository.save(User.builder()
                .firstName("Maria")
                .lastName("Nowak")
                .login("mnowak")
                .email("maria.nowak@wp.pl")
                .phoneNumber(666777888)
                .password("a1s2d3f")
                .build());
        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser
    void shouldReturnForbiddenWhenDeleteUserByIdWhenLoggedInAsUser() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldNotDeleteUserByIdWhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
