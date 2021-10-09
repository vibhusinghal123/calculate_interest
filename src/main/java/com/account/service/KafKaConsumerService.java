package com.account.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.account.model.AccountDetail;
import com.account.model.AccountProducer;
import com.account.model.Balance;
import com.account.model.BalanceDetail;

/**
 * @author vsinghal
 *
 */
@Service
public class KafKaConsumerService 
{
	@Autowired
	private KafKaProducerService kafKaProducerService;
	private final Logger logger 
		= LoggerFactory.getLogger(KafKaConsumerService.class);
	
	static Map<Long, AccountDetail> map =new HashMap<>();
	static double totalInterest;
	public static final int RATE_OF_INTEREST =5;
	
	
	/**
	 * this topic would consume account opening event.
	 * @param account
	 */
	@KafkaListener(topics = "${account.topic.name}", groupId = "${account.topic.group.id}",
			containerFactory = "accountKafkaListenerContainerFactory")
		
	public void consumeAccountDetail(AccountDetail account) {
		logger.info(String.format("consume event is: -> %s", account));		
		map.put(account.getIdentification(), account);
	}
	

	/**
	 * this topic would consume all balance related events opening events.
	 * @param balanceDetail
	 */
	@KafkaListener(topics = "${balance.topic.name}", groupId = "${balance.topic.group.id}",
			containerFactory = "balanceKafkaListenerContainerFactory")
	public void consumeBalanceDetail(BalanceDetail balanceDetail) {
		logger.info(String.format("consume event is: -> %s", balanceDetail));
		
		for(   Balance balance : balanceDetail.getBalance()) {
			AccountDetail account =  map.get(balance.getIdentification());
			
			// checking for valid account
			if(balance.getBsb().equals(account.getBsb()) && (balance.getIdentification() == account.getIdentification())) {
				double dailyInterest = calculateDailyInterest(balance.getBalance(), RATE_OF_INTEREST);	
				//totalInterest = totalInterest + dailyInterest;
				
				double monthlyInterest = calculateMonthlyInterest(dailyInterest,  account.getOpeningDate(), balanceDetail.getBalanceDate());
				
				sendReportForMonthlyInterest(monthlyInterest, account);
			}	 
				
			}
		}

	/**
	 *  this method generated daily interest,
	 * @param balance
	 * @param interestRate
	 * @return
	 */
	private double calculateDailyInterest(double balance, int interestRate) {
			int interestRatePerDay = interestRate / (365*100);
			double dailyInterest = balance * interestRatePerDay;
			return dailyInterest;
	}
	
	/**
	 * this method generated monthly interest.
	 * @param dailyInterest
	 * @param accountOpeningDate
	 * @param balanceDate
	 * @return
	 */
	private double calculateMonthlyInterest(double dailyInterest, String accountOpeningDate, String balanceDate) {
	 	List<Integer> EVEN_DAYS_MONTH = Arrays.asList(4, 6, 9, 11);
	 	List<Integer> ODD_DAYS_MONTH = Arrays.asList(1, 3, 5, 7,8,10,12);
		int no_of_days;
	 	double monthlyInterest = 0;
	 	
	 	int accountOpenigMonth = Integer.parseInt(accountOpeningDate.split("-")[1]);
		int accountOpenigday = Integer.parseInt(accountOpeningDate.split("-")[2]);
		
		if (EVEN_DAYS_MONTH.contains(accountOpenigMonth)) {
			 no_of_days = 30 - accountOpenigday;
			 monthlyInterest = no_of_days * dailyInterest;
		} else if (ODD_DAYS_MONTH.contains(accountOpenigMonth)) {
			 no_of_days = 31 - accountOpenigday;
			 monthlyInterest = no_of_days * dailyInterest;
		} else {
			 no_of_days = 28 - accountOpenigday;
			 monthlyInterest = no_of_days * dailyInterest;
		}
		
		return monthlyInterest;
		
	}
	
	/**
	 * this method send monthly interest for specific account to another kafka topic.
	 * @param monthlyInterest
	 * @param account
	 */
	private void sendReportForMonthlyInterest(double monthlyInterest, AccountDetail account) {
		
		AccountProducer producer =new AccountProducer ();
		producer.setIdentification(account.getIdentification());
		producer.setMonth(Integer.parseInt(account.getOpeningDate().split("-")[1]));
		producer.setBsb(account.getBsb());
		producer.setMonthlyInterest(monthlyInterest);
		kafKaProducerService.sendMonthlyInterestReport(producer);
		
	}
}
