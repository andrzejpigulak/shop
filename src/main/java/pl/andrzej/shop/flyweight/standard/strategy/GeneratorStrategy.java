package pl.andrzej.shop.flyweight.standard.strategy;

import pl.andrzej.shop.flyweight.model.FileType;

public interface GeneratorStrategy {

    FileType getType();

    byte[] generateFile();

}
