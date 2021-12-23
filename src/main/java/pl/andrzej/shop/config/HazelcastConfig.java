package pl.andrzej.shop.config;

import com.hazelcast.config.*;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.andrzej.shop.model.dao.Product;

@EnableCaching
@Configuration
public class HazelcastConfig {

    @Bean
    public Config configHazelcast() {
        Config config = new Config()
                .setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig()
                        .setName("product")
                        .setEvictionConfig(new EvictionConfig()
                                .setSize(10)
                                .setEvictionPolicy(EvictionPolicy.LRU)
                                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE))
                        .setTimeToLiveSeconds(60 * 60 * 24));
        config.getSerializationConfig()
                .addDataSerializableFactory(1, id -> id == 1 ? new Product() : null);
        return config;
    }


}
