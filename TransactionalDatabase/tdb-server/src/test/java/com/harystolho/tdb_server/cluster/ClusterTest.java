package com.harystolho.tdb_server.cluster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tdb_server.cluster.command.DeleteItemCommand;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.cluster.command.ReadItemCommand;
import com.harystolho.tdb_server.cluster.command.UpdateItemCommand;
import com.harystolho.tdb_server.cluster.query.ItemFieldQuery;
import com.harystolho.tdb_server.transaction.CommandLogger;
import com.harystolho.tdb_server.transaction.LogBlock;
import com.harystolho.tdb_shared.QueryResult;

@ExtendWith(MockitoExtension.class)
public class ClusterTest {

	private Cluster cluster;

	private List<Item> items;

	@Mock
	private CommandLogger logger;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void beforeEach() {
		items = Mockito.mock(List.class);
		cluster = new Cluster("TEST_CLUSTER", items, logger);
	}

	@Test
	public void insertItemCommand_ShouldInsertItem() {
		Mockito.when(items.add(Mockito.any(Item.class))).then((inv) -> {
			Item item = (Item) inv.getArgument(0);

			assertEquals("john", item.get("name"));
			assertEquals("7", item.get("age"));

			return true;
		});

		InsertItemCommand iic = new InsertItemCommand(4, "TEST_CLUSTER", Map.of("name", "john", "age", "7"));

		cluster.handle(iic);
	}

	@Test
	public void readItemCommand_SingleQuery_ShouldReturnItemsThatMatch() {
		Cluster cluster = new Cluster("CLOTHES", new ArrayList<>(), logger);

		cluster.handle(new InsertItemCommand(7, "CLOTHES", Map.of("color", "brown", "size", "M")));
		cluster.handle(new InsertItemCommand(7, "CLOTHES", Map.of("color", "yellow", "size", "L")));
		cluster.handle(new InsertItemCommand(7, "CLOTHES", Map.of("color", "red", "size", "S")));
		cluster.handle(new InsertItemCommand(7, "CLOTHES", Map.of("color", "black", "size", "S")));

		ReadItemCommand ric = new ReadItemCommand("CLOTHES", ItemFieldQuery.equal("size", "M"));

		QueryResult result = cluster.handle(ric);

		List<Map> list = result.getList("items", Map.class);

		assertEquals("brown", list.get(0).get("color"));
	}

	@Test
	public void readItemCommand_CompositeQuery_ShouldReturnItemsThatMatch() {
		Cluster cluster = new Cluster("VEHICLES", new ArrayList<>(), logger);

		cluster.handle(new InsertItemCommand(14, "VEHICLES", Map.of("year", "2011", "color", "green", "brand", "JD")));
		cluster.handle(
				new InsertItemCommand(14, "VEHICLES", Map.of("year", "2017", "color", "yellow", "brand", "CAT")));
		cluster.handle(new InsertItemCommand(14, "VEHICLES", Map.of("year", "2016", "color", "red", "brand", "MF")));
		cluster.handle(
				new InsertItemCommand(14, "VEHICLES", Map.of("year", "2019", "color", "yellow", "brand", "CAT")));

		ReadItemCommand ric = new ReadItemCommand("VEHICLES",
				ItemFieldQuery.equal("year", "2017").and(ItemFieldQuery.equal("brand", "CAT")));

		QueryResult result = cluster.handle(ric);

		List<Map> list = result.getList("items", Map.class);

		assertEquals("yellow", list.get(0).get("color"));
	}

	@Test
	public void readItemCommand_ShouldReturnEmptyIfNoItemsMatch() {
		Cluster cluster = new Cluster("VEHICLES", new ArrayList<>(), logger);

		cluster.handle(new InsertItemCommand(14, "VEHICLES", Map.of("year", "2011", "color", "green", "brand", "JD")));
		cluster.handle(
				new InsertItemCommand(14, "VEHICLES", Map.of("year", "2017", "color", "yellow", "brand", "CAT")));
		cluster.handle(new InsertItemCommand(14, "VEHICLES", Map.of("year", "2016", "color", "red", "brand", "MF")));
		cluster.handle(
				new InsertItemCommand(14, "VEHICLES", Map.of("year", "2019", "color", "yellow", "brand", "CAT")));

		ReadItemCommand ric = new ReadItemCommand("VEHICLES",
				ItemFieldQuery.equal("year", "2019").and(ItemFieldQuery.equal("brand", "Jd")));

		QueryResult result = cluster.handle(ric);

		List<Map> list = result.getList("items", Map.class);

		assertTrue(list.isEmpty());
	}

	@Test
	public void t() {
		throw new RuntimeException(); // test item query on _id
	}

	@Test
	public void deleteItemCommand_ShouldDeleteItems() {
		List<Item> items = new ArrayList<Item>();
		items.add(Item.fromMap(Map.of("color", "red")));
		items.add(Item.fromMap(Map.of("color", "blue")));
		items.add(Item.fromMap(Map.of("color", "green")));
		items.add(Item.fromMap(Map.of("color", "blue")));
		items.add(Item.fromMap(Map.of("age", "7")));

		Cluster cluster = new Cluster("PAINTINGS", items, logger);

		DeleteItemCommand dic = new DeleteItemCommand(99, "PAINTINGS", ItemFieldQuery.equal("color", "blue"));

		cluster.handle(dic);

		assertEquals(3, items.size());
	}

	@Test
	public void updateItemCommand_ShouldReplacesFieldsInOldItem() {
		List<Item> items = new ArrayList<Item>();
		items.add(Item.fromMap(Map.of("age", "1", "name", "Auth")));
		items.add(Item.fromMap(Map.of("age", "4", "name", "Poo")));
		items.add(Item.fromMap(Map.of("age", "1", "name", "Zoo")));

		Cluster cluster = new Cluster("PEOPLE", items, logger);

		UpdateItemCommand uic = new UpdateItemCommand(4757, "PEOPLE", ItemFieldQuery.equal("age", "1"),
				Map.of("name", "Auth Zoo"));

		cluster.handle(uic);

		assertEquals("Auth Zoo", items.get(0).get("name"));
		assertEquals("Auth Zoo", items.get(2).get("name"));
	}

	@Test
	public void updateItemCommand_ShouldAddFieldsNotPresentInOldItem() {
		List<Item> items = new ArrayList<Item>();
		items.add(Item.fromMap(Map.of("name", "james")));

		Cluster cluster = new Cluster("PEOPLE", items, logger);

		UpdateItemCommand uic = new UpdateItemCommand(1111, "PEOPLE", ItemFieldQuery.equal("name", "james"),
				Map.of("age", "37"));

		cluster.handle(uic);

		assertEquals("37", items.get(0).get("age"));
	}

	@Test
	public void updateItemCommand_AddOneNewField_ShouldCallToLogBlockWithCorrectValue() {
		List<Item> items = new ArrayList<Item>();
		items.add(Item.fromMap(Map.of("name", "guina", "age", "33")));

		Cluster cluster = new Cluster("ACTORS", items, logger);

		UpdateItemCommand uic = Mockito.mock(UpdateItemCommand.class);
		Mockito.when(uic.getQuery()).thenReturn(ItemFieldQuery.equal("age", "33"));
		Mockito.when(uic.getNewValues()).thenReturn(Map.of("city", "pry"));

		Mockito.doAnswer((inv) -> {
			List<Map<String, String>> oldItems = inv.getArgument(0);

			assertTrue(oldItems.get(0).get("_id") != null);

			assertTrue(oldItems.get(0).containsKey("city"));
			assertEquals(null, oldItems.get(0).get("city"));

			return new LogBlock(0, null);
		}).when(uic).toLogBlock(Mockito.any());

		cluster.handle(uic);
	}

	@Test
	public void updateItemCommand_ReplaceOneNewField_ShouldCallToLogBlockWithCorrectValue() {
		List<Item> items = new ArrayList<Item>();
		items.add(Item.fromMap(Map.of("name", "guina", "age", "33")));

		Cluster cluster = new Cluster("ACTORS", items, logger);

		UpdateItemCommand uic = Mockito.mock(UpdateItemCommand.class);
		Mockito.when(uic.getQuery()).thenReturn(ItemFieldQuery.equal("name", "guina"));
		Mockito.when(uic.getNewValues()).thenReturn(Map.of("age", "41"));

		Mockito.doAnswer((inv) -> {
			List<Map<String, String>> oldItems = inv.getArgument(0);

			assertTrue(oldItems.get(0).get("_id") != null);

			assertEquals("33", oldItems.get(0).get("age"));
			assertFalse(oldItems.get(0).containsKey("name"));

			return new LogBlock(0, null);
		}).when(uic).toLogBlock(Mockito.any());

		cluster.handle(uic);
	}

	@Test
	public void updateItemCommand_ReplaceOneNewField_TwoItems_ShouldCallToLogBlockWithCorrectValue() {
		List<Item> items = new ArrayList<Item>();
		items.add(Item.fromMap(Map.of("name", "guina", "age", "27", "car", "aleggro")));
		items.add(Item.fromMap(Map.of("name", "popie", "age", "27")));

		Cluster cluster = new Cluster("PEOPLE", items, logger);

		UpdateItemCommand uic = Mockito.mock(UpdateItemCommand.class);
		Mockito.when(uic.getQuery()).thenReturn(ItemFieldQuery.equal("age", "27"));
		Mockito.when(uic.getNewValues()).thenReturn(Map.of("car", "mondeo"));

		Mockito.doAnswer((inv) -> {
			List<Map<String, String>> oldItems = inv.getArgument(0);

			oldItems.sort((a, b) -> a.get("_id").compareTo(b.get("_id")));

			assertTrue(oldItems.get(0).get("_id") != null);
			assertTrue(oldItems.get(1).get("_id") != null);

			assertEquals("aleggro", oldItems.get(0).get("car"));

			assertTrue(oldItems.get(1).containsKey("car"));
			assertEquals(null, oldItems.get(1).get("car"));

			return new LogBlock(0, null);
		}).when(uic).toLogBlock(Mockito.any());

		cluster.handle(uic);
	}

}
