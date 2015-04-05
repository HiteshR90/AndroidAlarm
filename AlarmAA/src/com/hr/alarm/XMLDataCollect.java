package com.hr.alarm;

public class XMLDataCollect {

	private int tempf=0;
	private int tempc=0;
	private String city=null;
	
	public void SetCity(String c) {
	
		city=c;
	}
	
	public void setTempF(int t)
	{
		tempf=t;
	}
	
	public void setTempC(int t)
	{
		tempc=t;
	}
	
	public String dataTempF()
	{
		return ""+tempf;		
	}
	public String dataTempC()
	{
		return ""+tempc;		
	}
}
