package com.wetongji_android.util.data.activity;

import android.os.Bundle;

public class QueryHelper {
	
	public static final String ARG_ORDER_BY="order";
	public static final String ARG_ORDER_BY_LIKE="Like";
	public static final String ARG_ORDER_BY_TIME="Begin";
	public static final String ARG_ORDER_BY_PUBLISH_TIME="Id";
	
	public static final String ARG_ASCENDING="ascending";
	public static final String ARG_HAS_EXPIRED="hasExpired";
	public static final String ARG_HAS_CHANNEL_1="hasChannel1";
	public static final String ARG_HAS_CHANNEL_2="hasChannel2";
	public static final String ARG_HAS_CHANNEL_3="hasChannel3";
	public static final String ARG_HAS_CHANNEL_4="hasChannel4";
	
	public Bundle getPreparedQueryArgs(String orderBy, boolean ascending, boolean hasExpired, boolean hasChannel1, boolean hasChannel2, boolean hasChannel3, boolean hasChannel4){
		Bundle bundle=new Bundle();
		bundle.putString(ARG_ORDER_BY, orderBy);
		bundle.putBoolean(ARG_HAS_CHANNEL_1, hasChannel1);
		bundle.putBoolean(ARG_HAS_CHANNEL_2, hasChannel2);
		bundle.putBoolean(ARG_HAS_CHANNEL_3, hasChannel3);
		bundle.putBoolean(ARG_HAS_CHANNEL_4, hasChannel4);
		bundle.putBoolean(ARG_ASCENDING, ascending);
		bundle.putBoolean(ARG_HAS_EXPIRED, hasExpired);
		return bundle;
	}
	
}
