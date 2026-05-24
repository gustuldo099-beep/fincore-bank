package com.fincore.notification.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic pixCompletedTopic() {
        return TopicBuilder.name("pix-completed").partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic pixFailedTopic() {
        return TopicBuilder.name("pix-failed").partitions(1).replicas(1).build();
    }
}
