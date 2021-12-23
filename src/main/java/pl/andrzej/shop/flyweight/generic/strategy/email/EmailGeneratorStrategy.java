package pl.andrzej.shop.flyweight.generic.strategy.email;

import pl.andrzej.shop.flyweight.generic.strategy.GenericStrategy;
import pl.andrzej.shop.flyweight.model.EmailType;

public interface EmailGeneratorStrategy extends GenericStrategy<EmailType> {

    byte[] generateEmail();
}
