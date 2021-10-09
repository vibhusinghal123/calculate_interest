package com.account.model;

import java.util.List;

public class BalanceDetail 
{
	private String balanceDate;
	private List<Balance> balance;
	public String getBalanceDate() {
		return balanceDate;
	}
	
	public void setBalanceDate(String balanceDate) {
		this.balanceDate = balanceDate;
	}

	public List<Balance> getBalance() {
		return balance;
	}

	public void setBalance(List<Balance> balance) {
		this.balance = balance;
	}
	
	
}
