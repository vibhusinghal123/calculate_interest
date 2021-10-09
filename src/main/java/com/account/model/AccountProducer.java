package com.account.model;

public class AccountProducer 
{
	private double monthlyInterest;
    private int month;
    private long identification;
    private String bsb;
	public double getMonthlyInterest() {
		return monthlyInterest;
	}
	public int getMonth() {
		return month;
	}
	public long getIdentification() {
		return identification;
	}
	public void setMonthlyInterest(double monthlyInterest) {
		this.monthlyInterest = monthlyInterest;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public void setIdentification(long identification) {
		this.identification = identification;
	}
	public String getBsb() {
		return bsb;
	}
	public void setBsb(String bsb) {
		this.bsb = bsb;
	}
	
}
