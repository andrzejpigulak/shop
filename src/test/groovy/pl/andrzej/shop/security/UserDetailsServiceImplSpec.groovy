package pl.andrzej.shop.security

import pl.andrzej.shop.model.dao.Role
import pl.andrzej.shop.model.dao.User
import pl.andrzej.shop.repository.UserRepository
import spock.lang.Specification

class UserDetailsServiceImplSpec extends Specification {

    def userRepository = Mock(UserRepository)
    def userDetailServiceImpl = new UserDetailsServiceImpl(userRepository)

    def 'Load User By Username'() {
        when:
        userDetailServiceImpl.loadUserByUsername("apigulak")

        then:
        1 * userRepository.findByEmailOrLogin("apigulak", "apigulak") >> Optional.of(new User(email: "andrzej.pigulak@o2.pl", password: "apigulak", roles: [new Role(name: "ROLE_ADMIN")]))
        0 * _
    }

    def 'Load User By UserName Checking Value'() {
        given:
        def user = new User(email: "andrzej.pigulak@o2.pl", password: "apigulak", roles: [new Role(name: "ROLE_ADMIN")])
        userRepository.findByEmailOrLogin("apigulak", "apigulak") >> Optional.of(user)

        when:
        def result = userDetailServiceImpl.loadUserByUsername("apigulak")

        then:
        result.username == user.email
        result.password == user.password
        result.authorities.size() == user.roles.size()
        result.authorities[0].authority == user.roles.get(0).name
    }
}
