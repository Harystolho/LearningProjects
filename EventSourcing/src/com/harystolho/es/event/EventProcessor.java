package com.harystolho.es.event;

import java.util.List;

import com.harystolho.es.event.eventstore.EventStore;
import com.harystolho.es.event.eventstore.TextEventStore;

public class EventProcessor {

	private EventStore eventStore;

	public EventProcessor() {
		eventStore = new TextEventStore();
	}

	public void process(Event event) {
		event.process();
		eventStore.write(event);
	}

	public void restore() {
		List<Event> events = eventStore.read();

		events.forEach(event -> event.process());
	}

}
