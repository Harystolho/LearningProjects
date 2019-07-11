package com.harystolho.es.event;

import java.util.Map;

public abstract class Event {

	protected final long createdTime;
	protected String name;

	protected Event(String name) {
		this.name = name;

		this.createdTime = System.currentTimeMillis();
	}

	public long createdTime() {
		return createdTime;
	}

	public String getName() {
		return name;
	}
	
	public abstract void process();

	public abstract Map<String, String> normalize();

}
