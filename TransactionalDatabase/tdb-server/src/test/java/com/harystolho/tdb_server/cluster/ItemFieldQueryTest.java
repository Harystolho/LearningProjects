package com.harystolho.tdb_server.cluster;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.harystolho.tdb_server.cluster.query.ItemFieldQuery;
import com.harystolho.tdb_server.cluster.query.Query;

public class ItemFieldQueryTest {

	@Test
	public void equalQuery() {
		Item item = Item.fromMap(Map.of("city", "latvia", "region", "EU"));

		Query<Item> equal = ItemFieldQuery.equal("city", "latvia");

		assertTrue(equal.isSatisfiedBy(item));
	}

	@Test
	public void lessQuery() {
		Item underage = Item.fromMap(Map.of("age", "17"));
		Item overage = Item.fromMap(Map.of("age", "24"));

		Query<Item> less = ItemFieldQuery.less("age", 18);

		assertTrue(less.isSatisfiedBy(underage));
		assertFalse(less.isSatisfiedBy(overage));
	}

	@Test
	public void greaterQuery() {
		Item car1 = Item.fromMap(Map.of("year", "2017"));
		Item car2 = Item.fromMap(Map.of("year", "2008"));

		Query<Item> greater = ItemFieldQuery.greater("year", 2010);

		assertTrue(greater.isSatisfiedBy(car1));
		assertFalse(greater.isSatisfiedBy(car2));
	}
}
