package com.wetongji_android.ui.course;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wetongji_android.R;
import com.wetongji_android.data.Course;
import com.wetongji_android.ui.EndlessListAdapter;

public class CourseListAdapter extends EndlessListAdapter<Course> {

	private LayoutInflater mInflater;
	private Context mContext;
	
	static class ViewHolder {
		TextView tvCourseTitle;
		TextView tvTeacherName;
		LinearLayout layoutCourseBg;
	}
	
	public CourseListAdapter(Activity activity, AbsListView listView) {
		super(activity, listView, R.layout.amazing_lst_view_loading_view);
		mContext = activity;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	protected View doGetView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Course course = getItem(position);
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.row_my_courses, parent, false);
			holder.tvCourseTitle = (TextView) convertView
					.findViewById(R.id.text_mycourses_name);
			holder.tvTeacherName = (TextView) convertView
					.findViewById(R.id.text_mycourses_teacher);
			holder.layoutCourseBg = (LinearLayout) convertView
					.findViewById(R.id.layout_mycourse_bg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Set background color
		if (position % 2 != 0) {
			holder.layoutCourseBg.setBackgroundResource(R.drawable.listview_selector_2);
		} else {
			holder.layoutCourseBg.setBackgroundResource(R.drawable.listview_selector_1);
		}
		holder.tvCourseTitle.setText(course.getTitle());
		holder.tvTeacherName.setText(course.getTeacher());
		
		return convertView;
	}
}
