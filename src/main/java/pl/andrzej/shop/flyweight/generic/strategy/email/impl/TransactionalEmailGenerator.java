package pl.andrzej.shop.flyweight.generic.strategy.email.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import pl.andrzej.shop.flyweight.generic.strategy.email.EmailGeneratorStrategy;
import pl.andrzej.shop.flyweight.model.EmailType;

import java.io.IOException;

@Component
@Slf4j
public class TransactionalEmailGenerator implements EmailGeneratorStrategy {

    @Override
    public EmailType getType() {
        return EmailType.TRANSACTIONAL;
    }

    @Override
    public byte[] generateEmail() {
        try (Workbook workbook = WorkbookFactory.create(false)) {
            workbook.createSheet("Report");
        } catch (IOException e) {
            log.error("Email not generated");
        }
        log.info("TRANSACTIONAL Email Generator");
        return new byte[0];
    }
}
