package com.wetongji_android.util.adapter;

import com.wetongji_android.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter 
{
	private Context mContext;
	
	private static int[] titles = {
		R.string.text_today_campus,
		R.string.text_today_news,
		R.string.text_today_people,
		R.string.text_today_place
	};
	
	public GridViewAdapter(Context context)
	{
		this.mContext = context;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		if(titles != null)
			return titles.length;
		
		return 0;
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = View.inflate(this.mContext, R.layout.today_gridview_item, null);
			holder.tv_title  = (TextView)convertView.findViewById(R.id.tv_today_grid_title);
			holder.img_pic = (ImageView)convertView.findViewById(R.id.img_today_grid_pic);
			holder.tv_content = (TextView)convertView.findViewById(R.id.tv_today_grid_content);
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.tv_title.setText(this.mContext.getResources().getText(titles[position]));
		holder.tv_content.setText(this.mContext.getResources().getText(titles[position]));
		holder.img_pic.setBackgroundResource(R.drawable.img_today_grid_pic);
		
		return convertView;
	}
	
	static class ViewHolder
	{
		TextView tv_title;
		ImageView img_pic;
		TextView tv_content;
	}

}
