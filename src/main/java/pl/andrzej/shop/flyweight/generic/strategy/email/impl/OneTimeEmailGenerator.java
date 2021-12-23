package pl.andrzej.shop.flyweight.generic.strategy.email.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;
import pl.andrzej.shop.flyweight.generic.strategy.email.EmailGeneratorStrategy;
import pl.andrzej.shop.flyweight.model.EmailType;

import java.io.IOException;

@Component
@Slf4j
public class OneTimeEmailGenerator implements EmailGeneratorStrategy {

    @Override
    public EmailType getType() {
        return EmailType.ONETIME;
    }

    @Override
    public byte[] generateEmail() {

        log.info("ONETIME Email Generator");
        return new byte[0];
    }
}
