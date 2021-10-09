package com.account.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.account.model.AccountDetail;
import com.account.model.BalanceDetail;

@Configuration
public class KafkaConsumerConfig 
{
	@Value(value = "${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "${balance.topic.group.id}")
	private String balanceGroupId;

	@Value(value = "${account.topic.group.id}")
	private String accountGroupId;

	// 1. Consume balance data from Kafka

	@Bean
	public ConsumerFactory<String, BalanceDetail> balanceConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, balanceGroupId);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), 
				new JsonDeserializer<>(BalanceDetail.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, BalanceDetail> 
									balanceKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, BalanceDetail> factory 
			= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(balanceConsumerFactory());
		return factory;
	}

	// 2. Consume account open objects from Kafka

	public ConsumerFactory<String, AccountDetail> accountConsumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, accountGroupId);
		props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		return new DefaultKafkaConsumerFactory<>(props, 
				new StringDeserializer(), 
				new JsonDeserializer<>(AccountDetail.class));
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, AccountDetail> 
									accountKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, AccountDetail> factory 
			= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(accountConsumerFactory());
		return factory;
	}
}
