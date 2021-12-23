package pl.andrzej.shop.flyweight.generic.strategy.file;

import pl.andrzej.shop.flyweight.generic.strategy.GenericStrategy;
import pl.andrzej.shop.flyweight.model.FileType;

public interface FileGeneratorStrategy extends GenericStrategy<FileType> {

    byte[] generateFile();

}
