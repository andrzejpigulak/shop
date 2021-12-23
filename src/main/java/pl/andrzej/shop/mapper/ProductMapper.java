package pl.andrzej.shop.mapper;

import org.mapstruct.Mapper;
import pl.andrzej.shop.model.dao.Product;
import pl.andrzej.shop.model.dto.ProductDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto daoToDTo(Product product);

    Product dtoToDao(ProductDto productDto);

    List<ProductDto> daoListToDtoList(List<Product> productList);
}
