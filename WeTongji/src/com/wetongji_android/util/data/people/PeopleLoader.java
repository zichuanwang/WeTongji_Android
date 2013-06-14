package com.wetongji_android.util.data.people;

import android.content.Context;

import com.wetongji_android.data.Person;
import com.wetongji_android.util.data.DbListLoader;

public class PeopleLoader extends DbListLoader<Person, Integer>{

	public PeopleLoader(Context context) {
		super(context, Person.class);
	}

}
