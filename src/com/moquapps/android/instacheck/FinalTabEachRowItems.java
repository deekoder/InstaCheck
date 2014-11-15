package com.moquapps.android.instacheck;

public class FinalTabEachRowItems {
	private String peopleName="";
	private String amount=""; 
	//******************
	// Setter Methods
	//******************
	public void setPeopleName(String peopleName)
	{
		this.peopleName = peopleName;
	}
	
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	//*******************
	// Getter Methods
	//*******************
	public String getPeopleName()
	{
		return this.peopleName;
	}
	
	public String getAmount()
	{
		return ("$"+this.amount);
	}
	
}