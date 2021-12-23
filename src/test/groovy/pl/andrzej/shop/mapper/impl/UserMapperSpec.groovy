package pl.andrzej.shop.mapper.impl

import pl.andrzej.shop.mapper.UserMapperImpl
import pl.andrzej.shop.model.dao.User
import pl.andrzej.shop.model.dto.UserDto
import spock.lang.Specification

class UserMapperSpec extends Specification {

    def userMapperImpl = new UserMapperImpl()

    def 'Should Test User Mapper DaoToDto'() {
        given:
        def user = new User(id: id, firstName: firstName, lastName: lastName, email: email, login: login, password: password, phoneNumber: phoneNumber)

        when:
        def result = userMapperImpl.daoToDto(user)

        then:
        result.id == id
        result.firstName == firstName
        result.lastName == lastName
        result.email == email
        result.login == login
        result.password == null
        result.phoneNumber == phoneNumber

        where:
        id | firstName | lastName  | email                   | login      | password | phoneNumber
        1  | "Andrzej" | "Pigulak" | "andrzej.pigulak@o2.pl" | "apigulak" | "12312"  | 665103102

    }

    def 'Should Test User Mapper DtoToDao'() {
        given:
        def userDto = new UserDto(id: id, firstName: firstName, lastName: lastName, email: email, login: login, password: password, phoneNumber: phoneNumber)

        when:
        def result = userMapperImpl.dtoToDao(userDto)

        then:
        result.id == id
        result.firstName == firstName
        result.lastName == lastName
        result.email == email
        result.login == login
        result.password == password
        result.phoneNumber == phoneNumber

        where:
        id | firstName | lastName  | email                   | login      | password | phoneNumber
        1  | "Andrzej" | "Pigulak" | "andrzej.pigulak@o2.pl" | "apigulak" | "12312"  | 665103102
    }

}
