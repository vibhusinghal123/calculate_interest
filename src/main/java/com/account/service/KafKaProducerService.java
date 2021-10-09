package com.account.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.account.model.AccountProducer;

@Service
public class KafKaProducerService 
{
	private static final Logger logger = 
			LoggerFactory.getLogger(KafKaProducerService.class);
    
    @Value(value = "${monthlyInterest.topic.name}")
    private String monthlyInterestTopicName;
    
    @Autowired
    private KafkaTemplate<String, AccountProducer> accountKafkaTemplate;
	
	
	public void sendMonthlyInterestReport(AccountProducer accountProducer) 
	{
		ListenableFuture<SendResult<String, AccountProducer>> future 
			= this.accountKafkaTemplate.send(monthlyInterestTopicName, accountProducer);
		
		future.addCallback(new ListenableFutureCallback<SendResult<String, AccountProducer>>() {
            @Override
            public void onSuccess(SendResult<String, AccountProducer> result) {
            	logger.info("Monthly Interest Published: " 
            			+ accountProducer + " with offset: " + result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
            	logger.error("Message : " + accountProducer, ex);
            }
       });
	}
}
