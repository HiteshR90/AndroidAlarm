package com.hr.alarm;


import java.text.AttributedCharacterIterator.Attribute;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.R.integer;

public class HandlingXml extends DefaultHandler{

	private XMLDataCollect info=new XMLDataCollect();
	
	public String getTempC() {
		// TODO Auto-generated method stub
		return info.dataTempC();
	}
	
	public String getTempF()
	{
		return info.dataTempF();
	}
	
	public void startElement(String uri,String locname,String qname, Attributes attribe) throws SAXException
	{
		if(locname.equals("city"))
		{
			String city=attribe.getValue("data");
			info.SetCity(city);
		}
		if(locname.equals("temp_c"))
		{
			String t=attribe.getValue("data");
			int temp=Integer.parseInt(t);
			info.setTempC(temp);
		}
		if(locname.equals("temp_f"))
		{
			String temp=attribe.getValue("data");
			int temp1=Integer.parseInt(temp);
			info.setTempF(temp1);
		}
	}
	
}
