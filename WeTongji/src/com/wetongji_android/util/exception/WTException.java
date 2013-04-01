/**
 * 
 */
package com.wetongji_android.util.exception;

import android.text.TextUtils;

/**
 * @author nankonami
 *	Implement custom Exception class
 */
public class WTException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Specific http method
	private String strHttpMethod;
	//Specific api that leads exception
	private String strApi;
	//Web Server specific response error
	private String strError;
	//Response status code include the original http status
	private int iErrorCode;
	
	public WTException()
	{
	}
	
	public WTException(String strError)
	{
		this.strError = strError;
	}
	
	public WTException(String strError, Throwable th)
	{
		this.strError = strError;
	}

	public WTException(String strHttpMethod, String strApi, String strError, Throwable th)
	{
		this.setStrHttpMethod(strHttpMethod);
		this.setStrApi(strApi);
		this.setStrError(strError);
	}
	
	public WTException(String strHttpMethod, String strApi, String strError, int iErrorCode, Throwable th)
	{
		this.setStrHttpMethod(strHttpMethod);
		this.setStrApi(strApi);
		this.setStrError(strError);
		this.setErrorCode(iErrorCode);
	}
	
	@Override
	public String getMessage() 
	{
		// TODO Auto-generated method stub
		return getError();
	}
	
	private String getError()
	{
		if(!TextUtils.isEmpty(strError) && !TextUtils.isEmpty(strApi))
			return strApi + strError;
		
		return strError;
	}

	public String getStrHttpMethod() 
	{
		return strHttpMethod;
	}

	public void setStrHttpMethod(String strHttpMethod) 
	{
		this.strHttpMethod = strHttpMethod;
	}

	public String getStrApi() 
	{
		return strApi;
	}

	public void setStrApi(String strApi) 
	{
		this.strApi = strApi;
	}

	public int getErrorCode() 
	{
		return iErrorCode;
	}

	public void setErrorCode(int iErrorCode) 
	{
		this.iErrorCode = iErrorCode;
	}
	
	public String getStrError() 
	{
		return strError;
	}

	public void setStrError(String strError) 
	{
		this.strError = strError;
	}
}
