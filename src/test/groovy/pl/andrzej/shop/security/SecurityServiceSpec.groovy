package pl.andrzej.shop.security

import pl.andrzej.shop.model.dao.User
import pl.andrzej.shop.service.UserService
import spock.lang.Specification

class SecurityServiceSpec extends Specification {

    def userService = Mock(UserService)
    def securityService = new SecurityService(userService)

    def 'Has Access To User'() {
        given:
        userService.getCurrentUser() >> new User(id: userId)

        when:
        def result = securityService.hasAccessToUser(id)

        then:
        result == expected

        where:
        userId | id || expected
        2      | 2  || true
        2      | 3  || false
    }
}
