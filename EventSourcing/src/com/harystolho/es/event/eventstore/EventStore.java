package com.harystolho.es.event.eventstore;

import com.harystolho.es.event.Event;

public interface EventStore {

	void write(Event event);

}
