package pl.andrzej.shop.flyweight.standard.strategy.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.andrzej.shop.flyweight.model.FileType;
import pl.andrzej.shop.flyweight.standard.strategy.GeneratorStrategy;

@Component
@Slf4j
public class XlsGeneratorImpl implements GeneratorStrategy {

    @Override
    public FileType getType() {
        return FileType.XLS;
    }

    @Override
    public byte[] generateFile() {
        log.info("Generate XLS file");
        return new byte[0];
    }
}
