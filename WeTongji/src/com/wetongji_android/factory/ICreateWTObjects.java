package com.wetongji_android.factory;

import java.util.List;

public interface ICreateWTObjects<T> {
	abstract T createObject(String jsonStr);
	abstract List<T> createObjects(String jsonStr);
}
