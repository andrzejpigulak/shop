package pl.andrzej.shop.flyweight.generic.strategy.file.impl;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import pl.andrzej.shop.flyweight.generic.strategy.file.FileGeneratorStrategy;
import pl.andrzej.shop.flyweight.model.FileType;
import pl.andrzej.shop.model.dao.Product;
import pl.andrzej.shop.repository.ProductRepository;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonFileGenerator implements FileGeneratorStrategy {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Override
    public FileType getType() {
        return FileType.JSON;
    }

    @Override
    public byte[] generateFile() {
        try {
            List<Product> products = productRepository.findAll();
            return objectMapper.writeValueAsBytes(products);
        } catch (IOException e) {
            log.error("File not generated", e);
        }
        log.info("JSON File Generator");
        return new byte[0];
    }
}
