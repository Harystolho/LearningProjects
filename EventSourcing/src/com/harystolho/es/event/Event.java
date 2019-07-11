package com.harystolho.es.event;

public abstract class Event {

	protected final long createdTime;

	protected Event() {
		this.createdTime = System.currentTimeMillis();
	}

	public long createdTime() {
		return createdTime;
	}

	public abstract void process();

	public abstract String normalize();
	
}
