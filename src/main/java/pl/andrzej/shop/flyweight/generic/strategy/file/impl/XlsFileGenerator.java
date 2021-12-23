package pl.andrzej.shop.flyweight.generic.strategy.file.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;
import pl.andrzej.shop.flyweight.generic.strategy.file.FileGeneratorStrategy;
import pl.andrzej.shop.flyweight.model.FileType;
import pl.andrzej.shop.model.dao.Product;
import pl.andrzej.shop.repository.ProductRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class XlsFileGenerator implements FileGeneratorStrategy {

    private final ProductRepository productRepository;

    @Override
    public FileType getType() {
        return FileType.XLS;
    }

    @Override
    public byte[] generateFile() {
        try (Workbook workbook = WorkbookFactory.create(false)) {
            Sheet sheet = workbook.createSheet("Report");
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Id");
            row.createCell(1).setCellValue("Name");
            row.createCell(2).setCellValue("Model");
            row.createCell(3).setCellValue("Price");
            row.createCell(4).setCellValue("Serial Number");
            List<Product> products = productRepository.findAll();
            int i = 1;
            for (Product product : products) {
                row = sheet.createRow(i++);
                row.createCell(0).setCellValue(product.getId());
                row.createCell(1).setCellValue(product.getName());
                row.createCell(2).setCellValue(product.getModel());
                row.createCell(3).setCellValue(product.getPrice());
                row.createCell(4).setCellValue(product.getSerialNumber());
            }
            sheet.setAutoFilter(new CellRangeAddress(0, i, 0, 4));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("File not generated", e);
        }

        log.info("XLS File Generator");
        return new byte[0];
    }


}
