package pl.andrzej.shop.mapper.impl

import pl.andrzej.shop.mapper.BasketMapperImpl
import pl.andrzej.shop.model.dao.Basket
import pl.andrzej.shop.model.dao.Product
import pl.andrzej.shop.model.dao.User
import pl.andrzej.shop.model.dto.BasketDto
import spock.lang.Specification

class BasketMapperSpec extends Specification {

    def basketMapperImpl = new BasketMapperImpl()

    def 'Should Test Basket Mapper DaoToDto'() {

        given:
        def basket = new Basket(id: id, quantity: quantity, user: user, product: product)

        when:
        def result = basketMapperImpl.daoToDto(basket)

        then:
        result.id == id
        result.quantity == quantity
        result.user == new User()
        result.product == new Product()

        where:
        id | quantity | user       | product
        1  | 2        | new User() | new Product()
    }

    def 'Should Test Basket Mapper DtoToDao'() {
        given:
        def basketDto = new BasketDto(id: id, quantity: quantity, user: user, product: product)

        when:
        def result = basketMapperImpl.dtoToDao(basketDto);

        then:
        result.id == id
        result.quantity == quantity
        result.user == new User()
        result.product == new Product()

        where:
        id | quantity | user       | product
        1  | 2        | new User() | new Product()
    }
}
