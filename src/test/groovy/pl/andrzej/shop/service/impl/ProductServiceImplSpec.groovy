package pl.andrzej.shop.service.impl


import org.springframework.data.domain.PageRequest
import org.springframework.web.multipart.MultipartFile
import pl.andrzej.shop.config.properties.FilePropertiesConfig
import pl.andrzej.shop.function.BiConsumerThrowable
import pl.andrzej.shop.helper.FileHelper
import pl.andrzej.shop.model.dao.Product
import pl.andrzej.shop.repository.ProductRepository
import spock.lang.Specification

import java.nio.file.Path

class ProductServiceImplSpec extends Specification {

    def productRepository = Mock(ProductRepository)
    def filePropertiesConfig = Mock(FilePropertiesConfig)
    def fileHelper = Mock(FileHelper)
    def productServiceImpl = new ProductServiceImpl(productRepository, filePropertiesConfig, fileHelper)

    def 'Search Product By Id'() {
        when:
        productServiceImpl.searchProductById(2)

        then:
        1 * productRepository.getById(2)
        0 * _
    }

    def 'Delete Product By Id'() {
        when:
        productServiceImpl.deleteProductById(2)

        then:
        1 * productRepository.deleteById(2)
        0 * _
    }

    def 'Get Page'() {
        given:
        def pageable = PageRequest.of(2, 25)

        when:
        productServiceImpl.getPage(pageable)

        then:
        1 * productRepository.findAll(pageable)
        0 * _
    }

    def 'Update Product'() {
        given:
        def product = new Product(name: "Bosh", model: "12F", price: 2499, stockLevel: 3, serialNumber: 123456787654321)
        def file = Mock(MultipartFile)
        def biConsumerThrowable = Mock(BiConsumerThrowable)
        def inputStream = Mock(InputStream)

        when:
        def result = productServiceImpl.update(product, 2, file)

        then:
        result.id == 2
        result.name == product.name
        result.model == product.model
        result.price == product.price
        result.stockLevel == product.stockLevel
        result.serialNumber == product.serialNumber
        1 * productRepository.getById(2) >> new Product(id: 2, name: "Bosh", model: "12F", price: 2299, stockLevel: 3, serialNumber: 123456787654321)
        1 * filePropertiesConfig.getProduct() >> "Product"
        1 * fileHelper.fileCopy() >> biConsumerThrowable
        1 * biConsumerThrowable.accept(inputStream, _ as Path)
        1 * file.getInputStream() >> inputStream
        0 * _
    }

    def 'Save Product'() {
        given:
        def product = new Product()
        def file = Mock(MultipartFile)
        def biConsumerThrowable = Mock(BiConsumerThrowable)
        def inputStream = Mock(InputStream)

        when:
        productServiceImpl.save(product, file)

        then:
        1 * productRepository.save(_ as Product) >> new Product(id: 2)
        1 * filePropertiesConfig.getProduct() >> "Product"
        1 * fileHelper.fileCopy() >> biConsumerThrowable
        1 * biConsumerThrowable.accept(inputStream, _ as Path)
        1 * file.getInputStream() >> inputStream
        0 * _
    }

}
