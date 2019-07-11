package com.harystolho.es.event.eventstore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.harystolho.es.book.event.BookCreatedEvent;
import com.harystolho.es.book.event.BookReservedEvent;
import com.harystolho.es.book.event.BookReturnedEvent;
import com.harystolho.es.event.Event;

public class TextEventStore implements EventStore {

	private StringBuilder file;

	public TextEventStore() {
		this.file = new StringBuilder();
	}

	@Override
	public void write(Event event) {
		System.out.println(String.format("Writing event [Type: %s]", event.getClass().getSimpleName()));

		file.append(
				String.format("type=%s,%s\n", event.getName(), event.normalize().entrySet().stream().map((mapper) -> {
					return mapper.getKey() + "='" + mapper.getValue() + "\'";
				}).collect(Collectors.joining(","))));

		System.out.println(file.toString());
	}

	@Override
	public List<Event> read() {
		List<String> eventsAsString = Arrays.asList(file.toString().split("\\n"));

		List<Map<String, String>> events = eventsAsString.stream().map((mapper) -> {
			List<String> entries = Arrays.asList(mapper.split(","));
			Map<String, String> fields = new HashMap<>();

			entries.forEach((field) -> {
				String[] split = field.split("=");

				fields.put(split[0], split[1]);
			});

			return fields;
		}).collect(Collectors.toList());

		List<Event> eventObjects = new ArrayList<>();

		for (Map<String, String> event : events) {
			switch (event.get("type")) {
			case "BOOK_CREATED_EVENT":
				eventObjects.add(new BookCreatedEvent(event.get("isbn"), event.get("title"), event.get("author")));
				break;
			case "BOOK_RESERVED_EVENT":
				eventObjects.add(new BookReservedEvent(event.get("isbn")));
				break;
			case "BOOK_RETURNED_EVENT":
				eventObjects.add(new BookReturnedEvent(event.get("isbn")));
				break;
			default:
				System.out.println("Unsupported operation");
				break;
			}
		}

		return eventObjects;
	}

}
