package pl.andrzej.shop.mapper.impl

import pl.andrzej.shop.mapper.ProductMapperImpl
import pl.andrzej.shop.model.dao.Product
import pl.andrzej.shop.model.dto.ProductDto
import spock.lang.Specification

class ProductMapperSpec extends Specification {

    def productMapperImpl = new ProductMapperImpl()

    def 'Should Test Product Mapper DaoToDto'() {

        given:
        def product = new Product(id: id, name: name, model: model, price: price, stockLevel: stockLevel, serialNumber: serialNumber)

        when:
        def result = productMapperImpl.daoToDTo(product)

        then:
        result.id == id
        result.name == name
        result.model == model
        result.price == price
        result.stockLevel == stockLevel
        result.serialNumber == serialNumber

        where:
        id | name   | model | price   | stockLevel | serialNumber
        1  | "Bosh" | "13PKPS"    | 3999.00 | 5          | "13KMPDSSA"
    }

    def 'Should Test Product Mapper DtoToDao'() {
        given:
        def productDto = new ProductDto(id: id, name: name, model: model, price: price, stockLevel: stockLevel, serialNumber: serialNumber)

        when:
        def result = productMapperImpl.dtoToDao(productDto)

        then:
        result.id == id
        result.name == name
        result.model == model
        result.price == price
        result.stockLevel == stockLevel
        result.serialNumber == serialNumber

        where:
        id | name   | model | price   | stockLevel | serialNumber
        1  | "Bosh" | "13PKPS"    | 3999.00 | 5          | "13KMPDSSA"
    }
}
