package com.wetongji_android.ui.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wetongji_android.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainMenuFragment extends Fragment {
	
	public static final String KEY_MAIN_MENU_ICON = "icon";
	public static final String KEY_MAIN_MENU_TEXT = "text";
	public static final int MAIN_MENU_ICON_RES[] = {
		R.drawable.ic_launcher,
		R.drawable.ic_launcher,
		R.drawable.ic_launcher,
		R.drawable.ic_launcher,
		R.drawable.ic_launcher,
		R.drawable.ic_launcher,
		R.drawable.ic_launcher,
	};
	
	public static final int MAIN_MENU_TEXT_RES[] = {
		R.string.title_mainmenu_today,
		R.string.title_mainmenu_news,
		R.string.title_mainmenu_events,
		R.string.title_mainmenu_now,
		R.string.title_mainmenu_search,
		R.string.title_mainmenu_billboard,
		R.string.title_mainmenu_profile,
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.main_menu, null);
		 
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ListView lstViewMenu = (ListView) getActivity().findViewById(R.id.main_memu_list);
		lstViewMenu.setAdapter(new SimpleAdapter(getActivity(), 
				getData(), R.layout.row_main_menu, new String[] {KEY_MAIN_MENU_ICON, KEY_MAIN_MENU_TEXT}, 
				new int[] {R.id.img_main_menu, R.id.tv_main_menu}));
		
		lstViewMenu.setOnItemClickListener(new MainMenuListItemClickListener());
	}
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> lst = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		
		for(int i = 0; i < MAIN_MENU_ICON_RES.length; i++) {
			map = new HashMap<String, Object>();
			map.put(KEY_MAIN_MENU_ICON, MAIN_MENU_ICON_RES[i]);
			map.put(KEY_MAIN_MENU_TEXT, getResources().getString(MAIN_MENU_TEXT_RES[i]));
			lst.add(map);
		}
		
		return lst;
	}
	
	
	public class MainMenuListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int positioin,
				long arg3) {
			
			//TODO Change the fragment
			Toast.makeText(getActivity(), "" + positioin, Toast.LENGTH_SHORT).show();
		}
		
	}
	

}
