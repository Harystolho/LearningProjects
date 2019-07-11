package com.harystolho.es.event.eventstore;

import java.util.List;

import com.harystolho.es.event.Event;

public interface EventStore {

	void write(Event event);

	List<Event> read();

}
