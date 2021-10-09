package com.account.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class TopicConfig 
{
	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${balance.topic.name}")
	private String balanceTopic;

	@Value(value = "${account.topic.name}")
	private String accountTopic;

	@Value(value = "${monthlyInterest.topic.name}")
	private String monthlyInterestTopic;

	
	@Bean
	public NewTopic balanceTopic() {
		return TopicBuilder.name(balanceTopic)
			      .partitions(3)
			      .replicas(3)
			      .build();
	}

	@Bean
	public NewTopic accountOpenTopic() {
		return TopicBuilder.name(accountTopic)
			      .partitions(3)
			      .replicas(3)
			      .build();
	}
	
	@Bean
	public NewTopic monthlyInterestTopic() {
		return TopicBuilder.name(monthlyInterestTopic)
			      .partitions(3)
			      .replicas(3)
			      .build();
	}
	
	@Bean
    public KafkaAdmin kafkaAdmin() 
	{
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }
}
