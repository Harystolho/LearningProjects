package com.harystolho.es.event.eventstore;

import com.harystolho.es.event.Event;

public class TextEventStore implements EventStore {

	private StringBuilder file;

	public TextEventStore() {
		this.file = new StringBuilder();
	}

	@Override
	public void write(Event event) {
		System.out.println(String.format("Writing event [Type: %s]", event.getClass().getSimpleName()));

		file.append(String.format("type=%s,%s\n", event.getClass().getSimpleName(), event.normalize()));

		System.out.println(file.toString());
	}

}
