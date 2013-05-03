package com.wetongji_android.util.data.information;

import java.util.List;

import android.content.Context;
import android.os.Bundle;

import com.wetongji_android.data.Information;
import com.wetongji_android.util.data.DbListLoader;

public class InformationLoader extends DbListLoader<Information, Integer> 
{
	private Bundle args;
	
	public InformationLoader(Context context, Bundle args)
	{
		super(context, Information.class);
		this.args = args;
	}

	@Override
	public List<Information> loadInBackground() 
	{
		// TODO Auto-generated method stub
		return super.loadInBackground();
	}
}