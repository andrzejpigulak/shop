package pl.andrzej.shop.flyweight.generic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.andrzej.shop.flyweight.generic.strategy.GenericStrategy;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class GenericFactory<K, V extends GenericStrategy<K>> {

    private final List<V> strategies;
    private Map<K, V> strategyMap;

    @PostConstruct
    void init() {
        strategyMap = strategies.stream()
                .collect(Collectors.toMap(GenericStrategy::getType, Function.identity()));
    }

    public V getByType(K k) {
        return strategyMap.get(k);
    }
}
