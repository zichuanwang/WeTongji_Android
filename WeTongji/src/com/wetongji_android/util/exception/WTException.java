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

	//Original http response error
	private String oriStrError;
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

	@Override
	public String getMessage() 
	{
		// TODO Auto-generated method stub
		return getError();
	}
	
	private String getError()
	{
		String strResult;
		
		if(!TextUtils.isEmpty(this.strError))
			strResult =  this.strError;
		else
			strResult = "";
		
		return strResult;
	}

	public int getiErrorCode() 
	{
		return iErrorCode;
	}

	public void setiErrorCode(int iErrorCode) 
	{
		this.iErrorCode = iErrorCode;
	}
}
