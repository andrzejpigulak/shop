package pl.andrzej.shop.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.andrzej.shop.config.properties.FilePropertiesConfig;
import pl.andrzej.shop.helper.FileHelper;
import pl.andrzej.shop.model.dao.Product;
import pl.andrzej.shop.repository.ProductRepository;
import pl.andrzej.shop.service.ProductService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final FilePropertiesConfig filePropertiesConfig;
    private final FileHelper fileHelper;

    @Override
    @Transactional
    public Product save(Product product, MultipartFile file) {

        Product productDB = productRepository.save(product);

        if (file != null) {
            Path path = Paths.get(filePropertiesConfig.getProduct(), productDB.getId() + ".png");
            try {
                fileHelper.fileCopy().accept(file.getInputStream(), path);
                productDB.setImageUrl(path.toString());
                log.info("Picture saved ok");
            } catch (IOException e) {
                log.error("Picture not saved", e);
            }
        }
        return productDB;
    }

    @Transactional
    @Override
    public Product update(Product product, Long id, MultipartFile file) {

        Product productDb = searchProductById(id);

        productDb.setName(product.getName());
        productDb.setModel(product.getModel());
        productDb.setPrice(product.getPrice());
        productDb.setStockLevel(product.getStockLevel());
        productDb.setSerialNumber(product.getSerialNumber());
        if (file != null) {
            Path path = Paths.get(filePropertiesConfig.getProduct(), id + ".png");
            try {
                fileHelper.fileCopy().accept(file.getInputStream(),path);
                productDb.setImageUrl(path.toString());
                log.info("Picture saved ok");
            } catch (IOException e) {
                log.error("Picture not saved", e);
            }
        }
        return productDb;
    }

    @Override
    public Product searchProductById(Long id) {
        log.info("Product {} not in cache", id);
        return productRepository.getById(id);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> getPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
