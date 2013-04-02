package com.wetongji_android.util.net;

public class HttpRequestResult 
{
	//Response status code
	private int iResponseCode;
	//Response content
	private String strResponseCon;
	
	public HttpRequestResult()
	{
	
	}
	
	public HttpRequestResult(int iResponseCode, String strResponseCon)
	{
		this.setResponseCode(iResponseCode);
		this.setStrResponseCon(strResponseCon);
	}
	
	public String getStrResponseCon() 
	{
		return strResponseCon;
	}

	public void setStrResponseCon(String strResponseCon) 
	{
		this.strResponseCon = strResponseCon;
	}

	public int getResponseCode() 
	{
		return iResponseCode;
	}

	public void setResponseCode(int iResponseCode) 
	{
		this.iResponseCode = iResponseCode;
	}

}
