package com.account.model;

public class AccountDetail 
{
	private String bsb;
    private String openingDate;
    private long identification;
	public String getBsb() {
		return bsb;
	}
	
	public String getOpeningDate() {
		return openingDate;
	}
	public void setBsb(String bsb) {
		this.bsb = bsb;
	}
	
	public void setOpeningDate(String openingDate) {
		this.openingDate = openingDate;
	}

	public long getIdentification() {
		return identification;
	}

	public void setIdentification(long identification) {
		this.identification = identification;
	}
    

}
