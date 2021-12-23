package pl.andrzej.shop.service.impl


import pl.andrzej.shop.model.dao.Basket
import pl.andrzej.shop.model.dao.Product
import pl.andrzej.shop.model.dao.User
import pl.andrzej.shop.repository.BasketRepository
import pl.andrzej.shop.service.ProductService
import spock.lang.Specification

class BasketServiceImplSpec extends Specification {

    def basketRepository = Mock(BasketRepository)
    def userServiceImpl = Mock(UserServiceImpl)
    def productService = Mock(ProductService)
//    def basketServiceImpl = new BasketServiceImpl(basketRepository, userServiceImpl, productService)
    def basketServiceImpl = Spy(BasketServiceImpl, constructorArgs: [basketRepository, userServiceImpl, productService])

    def 'Remove Product From Basket'() {

        when:
        basketServiceImpl.removeProductFromBasket(2)

        then:
        1 * basketServiceImpl.removeProductFromBasket(2)
        1 * userServiceImpl.getCurrentUser() >> new User(id: 2)
        1 * basketRepository.deleteByUserIdAndProductId(2, 2)
        0 * _
    }

    def 'Get'() {
        given:
        def basket = new Basket(id: id, quantity: quantity, user: user, product: product)
        userServiceImpl.getCurrentUser() >> new User(id: 2)
        basketRepository.findByUserId(2) >> [new Basket(product: new Product(stockLevel: 3), quantity: 5)]

        when:
        def result = basketServiceImpl.get()

        then:
        result.size() == 1
        result.get(0).stockLevel == 5


        where:
        id | quantity | user       | product
        1  | 2        | new User() | new Product()

//        when:
//        basketServiceImpl.get()
//
//        then:
//        1 * userServiceImpl.getCurrentUser() >> new User(id: 2)
//        1 * basketRepository.findByUserId(2) >> [new Basket(product: new Product(), quantity: 5)]
//        0 * _
    }

    def 'Update Basket with quantity is #quantity where stockLevel is #stockLevel '() {
        when:
        def result = basketServiceImpl.update(2, quantity)

        then:
        result.id == 1
        result.quantity == expected
        result.user == null
        result.product == null

        1 * basketServiceImpl.update(2, quantity)
        1 * userServiceImpl.getCurrentUser() >> new User(id: 2)
        1 * productService.searchProductById(2) >> new Product(stockLevel: stockLevel)
        1 * basketRepository.findByProductIdAndUserId(2, 2) >> Optional.of(new Basket(id: 1))
        0 * _

        where:
        stockLevel | quantity || expected
        5          | 3        || 3
        3          | 5        || 3

    }

    def 'Update Basket where Product not in Basket'() {
        when:
        basketServiceImpl.update(2, 3)

        then:
        1 * basketServiceImpl.update(2, 3)
        1 * basketServiceImpl.addProduct(2, 3)
        2 * userServiceImpl.getCurrentUser() >> new User(id: 2)
        2 * productService.searchProductById(2) >> new Product(stockLevel: 5)
        1 * basketRepository.findByProductIdAndUserId(2, 2) >> Optional.empty()
        1 * basketRepository.save(_ as Basket)
        0 * _

    }

    def 'Save stockLevel equals quantity'() {
        when:
        basketServiceImpl.addProduct(2, 5)

        then:
        1 * basketServiceImpl.addProduct(2, 5)
        1 * userServiceImpl.getCurrentUser() >> new User(id: 2)
        1 * productService.searchProductById(2) >> new Product(stockLevel: 5)
        1 * basketRepository.save(_ as Basket)
        0 * _

    }

    def 'Save stockLevel less then quantity'() {
        when:
        basketServiceImpl.addProduct(2, 5)

        then:
        1 * basketServiceImpl.addProduct(2, 5)
        1 * userServiceImpl.getCurrentUser() >> new User(id: 2)
        1 * productService.searchProductById(2) >> new Product(stockLevel: 2)
        1 * basketRepository.save(_ as Basket)
        0 * _

    }

}
