package pl.andrzej.shop.service.impl

import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import pl.andrzej.shop.model.dao.Role
import pl.andrzej.shop.model.dao.User
import pl.andrzej.shop.repository.RoleRepository
import pl.andrzej.shop.repository.UserRepository
import pl.andrzej.shop.security.SecurityUtils
import spock.lang.Specification

class UserServiceImplSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def passwordEncoder = Mock(PasswordEncoder)
    def roleRepository = Mock(RoleRepository)
    def userServiceImpl = new UserServiceImpl(userRepository, passwordEncoder, roleRepository)

    def 'Save User'() {
        given:
        def user = new User(password: "hasloTestowe")

        when:
        userServiceImpl.save(user)

        then:
        1 * passwordEncoder.encode("hasloTestowe") >> "DISHDBS&^*^#(HND#(*"
        1 * roleRepository.findByName("ROLE_USER") >> Optional.of(new Role())
        1 * userRepository.save(_ as User) // podkreślinik oznacza cokolwiek
        0 * _

    }

    def 'Search By Id'() {
        when:
        userServiceImpl.searchUserById(10)

        then:
        1 * userRepository.getById(10)
        0 * _
    }


    def 'Delete By Id'() {
        when:
        userServiceImpl.deleteUserById(2)

        then:
        1 * userRepository.deleteById(2)
        0 * _
    }

    def 'Get Page'() {
        given:
        def pageable = PageRequest.of(2, 25)

        when:
        userServiceImpl.getPage(pageable)

        then:
        1 * userRepository.findAll(pageable)
        0 * _
    }

    def 'Update User'() {
        given:
        def user = new User(firstName: "Andrzej", lastName: "Pigulak", phoneNumber: 665103102)
        userRepository.getById(2) >> new User(id: 2, firstName: "Bartek", lastName: "Kowalski", phoneNumber: 603777890)

        when:
        def result = userServiceImpl.update(user, 2)

        then:
        result.id == 2
        result.firstName == user.firstName
        result.lastName == user.lastName
        result.phoneNumber == user.phoneNumber
    }

    def 'Get Current User'() {
        given:
        def securityUtils = GroovySpy(SecurityUtils, global: true)

        when:
        userServiceImpl.getCurrentUser()

        then:
        1 * securityUtils.getCurrentEmailUser() >> "andrzej.pigulak@o2.pl"
        1 * userRepository.findByEmail("andrzej.pigulak@o2.pl") >> Optional.of(new User())
        0 * _
    }

}
