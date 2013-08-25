package com.example.sodacloudsmsexampleclient;

import java.util.HashMap;
import java.util.Map;

public class ModuleImpl implements Module {
	Map<Class<?>,Object> maps = new HashMap<Class<?>,Object>();
	
	@Override
	public <T> T getComponent(Class<T> type) {

		return (T)maps.get(type);
	}

	@Override
	public <T> void setComponent(Class<T> type, T component) {
		maps.put(type, component);

	}

}
