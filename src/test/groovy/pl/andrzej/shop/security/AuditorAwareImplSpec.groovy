package pl.andrzej.shop.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class AuditorAwareImplSpec extends Specification {

    def securityContext = Mock(SecurityContext)
    def authentication = Mock(Authentication)
    def auditorAwareImpl = new AuditorAwareImpl()

    def initUserName(String userName) {
        securityContext.getAuthentication() >> authentication
        SecurityContextHolder.setContext(securityContext)
        authentication.getName() >> userName
    }


    def 'Should Get Current Auditor'() {
        given:
        def email = "andrzej.pigulak@o2.pl"
        initUserName(email)

        when:
        def result = auditorAwareImpl.getCurrentAuditor()

        then:
        result.isPresent()
        result.get() == email
    }

    def 'Should Not Get Current Auditor'() {
        given:
        initUserName(null)

        when:
        def result = auditorAwareImpl.getCurrentAuditor()

        then:
        result.isEmpty()
    }
}
