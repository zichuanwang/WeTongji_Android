package com.wetongji_android.ui.main;


import com.wetongji_android.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuFragment extends Fragment {
	
	public static final String KEY_MAIN_MENU_ICON = "icon";
	public static final String KEY_MAIN_MENU_TEXT = "text";
	private static final int MAIN_MENU_ICON_RES[] = {
		R.drawable.ic_main_today,
		R.drawable.ic_main_news,
		R.drawable.ic_main_events,
		R.drawable.ic_main_now,
		R.drawable.ic_main_search,
		R.drawable.ic_main_bboard,
		R.drawable.ic_main_profile,
	};
	
	
	private static final int MAIN_MENU_ICON_SELECTED_RES[] = {
		R.drawable.ic_main_today_pressed,
		R.drawable.ic_main_news_pressed,
		R.drawable.ic_main_events_pressed,
		R.drawable.ic_main_now_pressed,
		R.drawable.ic_main_search_pressed,
		R.drawable.ic_main_bboard_pressed,
		R.drawable.ic_main_profile_pressed,
	};
	
	private static final int MAIN_MENU_TEXT_RES[] = {
		R.string.title_mainmenu_today,
		R.string.title_mainmenu_news,
		R.string.title_mainmenu_events,
		R.string.title_mainmenu_now,
		R.string.title_mainmenu_search,
		R.string.title_mainmenu_billboard,
		R.string.title_mainmenu_profile,
	};
	
	private int mCurrentItemNu = 0;
	private MainMenuListAdapter mMenuListAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.main_menu, null);
		 
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ListView lstViewMenu = (ListView) getActivity().findViewById(R.id.main_memu_list);
		
		mMenuListAdapter = new MainMenuListAdapter(getActivity());
		
		lstViewMenu.setAdapter(mMenuListAdapter);
		lstViewMenu.setOnItemClickListener(new MainMenuListItemClickListener());
	}
	
	
	public class MainMenuListAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		
		public MainMenuListAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }

		@Override
		public int getCount() {
			return MAIN_MENU_ICON_RES.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			convertView = mInflater.inflate(R.layout.row_main_menu, null);
			ImageView image = (ImageView) convertView.findViewById(R.id.img_main_menu);
			TextView text = (TextView) convertView.findViewById(R.id.tv_main_menu);
			image.setImageResource(MAIN_MENU_ICON_RES[position]);
			text.setText(MAIN_MENU_TEXT_RES[position]);
			
			// 选中项深色
			if(position == mCurrentItemNu) {
				convertView.setBackgroundColor(getResources().getColor(R.color.main_menu_selected));
				image.setImageResource(MAIN_MENU_ICON_SELECTED_RES[position]);
			}
			
			return convertView;
		}
		
		
	}
	
	public class MainMenuListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int positioin,
				long arg3) {
			
			//Change item background
			mCurrentItemNu = positioin;
			mMenuListAdapter.notifyDataSetChanged();
			
			//TODO Change the fragment
			Toast.makeText(getActivity(), "" + positioin, Toast.LENGTH_SHORT).show();
		}
		
	}
	

}
