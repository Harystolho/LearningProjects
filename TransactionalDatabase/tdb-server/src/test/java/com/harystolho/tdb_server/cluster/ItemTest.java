package com.harystolho.tdb_server.cluster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class ItemTest {

	@Test
	public void mergeItem_WithEmptyItem_ShouldStayTheSame() {
		Item i1 = Item.fromMap(Map.of("city", "PR"));
		Item i2 = new Item();

		assertEquals(i1, i1.merge(i2));
	}

	@Test
	public void mergeItem_WithOtherItem_ShouldReplacesFields() {
		Item i1 = Item.fromMap(Map.of("city", "PR"));
		Item i2 = Item.fromMap(Map.of("city", "CWZ"));

		assertEquals("CWZ", i1.merge(i2).get("city"));
	}

	@Test
	public void mergeItem_WithOtherItem_ShouldAddFields() {
		Item i1 = Item.fromMap(Map.of("city", "PR"));
		Item i2 = Item.fromMap(Map.of("country", "EN"));

		Item i3 = i1.merge(i2);

		assertEquals("PR", i3.get("city"));
		assertEquals("EN", i3.get("country"));
	}
}
