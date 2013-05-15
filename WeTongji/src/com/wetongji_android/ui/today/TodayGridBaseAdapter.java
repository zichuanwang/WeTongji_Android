package com.wetongji_android.ui.today;

import java.util.List;

import com.androidquery.AQuery;
import com.wetongji_android.R;
import com.wetongji_android.util.common.WTApplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TodayGridBaseAdapter<T> extends BaseAdapter 
{
	protected List<T> items;
	protected Context context;
	protected ViewHolder holder;
	private LayoutInflater inflater;
	protected AQuery gridAq;
	
	public TodayGridBaseAdapter(Context context, List<T> items)
	{
		this.context=context;
		this.items=items;
		inflater=LayoutInflater.from(context);
		gridAq=WTApplication.getInstance().getAq((Activity) context);
	}
	
	@Override
	public int getCount() 
	{
		return items.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return items.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.grid_today, parent, false);
			holder.ivGridImage=(ImageView) convertView.findViewById(R.id.iv_today_gird_image);
			holder.ivGridImageMask=(ImageView) convertView.findViewById(R.id.iv_today_gird_mask);
			holder.tvGridTitle=(TextView) convertView.findViewById(R.id.tv_today_grid_title);
			holder.ivGridTitleIndicator=(ImageView) convertView.findViewById(R.id.iv_today_grid_title_indicator);
			holder.tvGridContent=(TextView) convertView.findViewById(R.id.tv_today_grid_content);
			convertView.setTag(holder);
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		return convertView;
	}
	
	protected static class ViewHolder{
		ImageView ivGridImage;
		ImageView ivGridImageMask;
		TextView tvGridTitle;
		ImageView ivGridTitleIndicator;
		TextView tvGridContent;
	}

}
