package org.epnoi.multiagents;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by cbadenes on 13/02/16.
 */
@Configuration
@ComponentScan({"org.epnoi.storage","org.epnoi.eventbus","org.epnoi.multiagents"})
@PropertySource({"classpath:storage.properties","classpath:eventbus.properties"})
public class Config {

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
