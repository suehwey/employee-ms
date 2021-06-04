package com.garage.upskills.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("spring.kafka")
public class KafkaConfigurationProperties {

    String bootstrapServers;
    String groupId;
    String autoOffsetReset;
    String keyDeserializer;
    String valueDeserializer;
    String topic;
    String trustedPackages;
    Boolean enableAutoCommit;


}
