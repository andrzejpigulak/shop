package pl.andrzej.shop.flyweight.generic.strategy.file.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import pl.andrzej.shop.flyweight.generic.strategy.file.FileGeneratorStrategy;
import pl.andrzej.shop.flyweight.model.FileType;

import java.io.IOException;

@Component
@Slf4j
public class CsvFileGenerator implements FileGeneratorStrategy {

    @Override
    public FileType getType() {
        return FileType.CSV;
    }

    @Override
    public byte[] generateFile() {
        log.info("CSV File Generator");
        return new byte[0];
    }
}
