package com.harystolho.it;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.harystolho.tdb_server.cluster.ClusterCatalog;
import com.harystolho.tdb_server.cluster.command.DeleteItemCommand;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.cluster.command.ReadItemCommand;
import com.harystolho.tdb_server.cluster.query.ItemFieldQuery;
import com.harystolho.tdb_server.command.CommandDispatcher;
import com.harystolho.tdb_server.transaction.TransactionLogger;
import com.harystolho.tdb_shared.QueryResult;

public class ClusterCommandTest {

	private static ClusterCatalog clusterCatalog;

	@BeforeAll
	public static void beforeAll() {
		var commandDispatcher = Mockito.mock(CommandDispatcher.class);
		var logger = Mockito.mock(TransactionLogger.class);

		clusterCatalog = new ClusterCatalog(commandDispatcher, logger);

		insertItems();
	}

	private static void insertItems() {
		clusterCatalog
				.handle(new InsertItemCommand(9, "SONGS", Map.of("name", "Long Life", "year", "2017", "score", "4")));
		clusterCatalog
				.handle(new InsertItemCommand(9, "SONGS", Map.of("name", "Dreams", "year", "2011", "score", "7")));
		clusterCatalog
				.handle(new InsertItemCommand(9, "SONGS", Map.of("name", "Baiave", "year", "2008", "score", "10")));
		clusterCatalog.handle(
				new InsertItemCommand(9, "SONGS", Map.of("name", "Cuiudo do Alegrete", "year", "2013", "score", "9")));
		clusterCatalog.handle(
				new InsertItemCommand(9, "SONGS", Map.of("name", "Klavier Concert 4", "year", "1754", "score", "10")));
		clusterCatalog
				.handle(new InsertItemCommand(9, "SONGS", Map.of("name", "Ra ro hill", "year", "2018", "score", "2")));
		clusterCatalog
				.handle(new InsertItemCommand(9, "SONGS", Map.of("name", "Java Island", "year", "1992", "score", "6")));
		clusterCatalog.handle(
				new InsertItemCommand(9, "SONGS", Map.of("name", "Overclocked World", "year", "2013", "score", "7")));
		clusterCatalog
				.handle(new InsertItemCommand(11, "SONGS", Map.of("name", "Blue Pill", "year", "1080", "score", "1")));
	}

	@Test
	public void readItems_SingleQuery() {
		QueryResult result = clusterCatalog.handle(new ReadItemCommand("SONGS", ItemFieldQuery.equal("year", "2008")));
		List<Map> list = result.getList("items", Map.class);

		assertEquals("Baiave", list.get(0).get("name"));
	}

	@Test
	public void readItems_NumberQuery() {
		QueryResult result = clusterCatalog.handle(new ReadItemCommand("SONGS", ItemFieldQuery.less("score", 3)));
		List<Map> list = result.getList("items", Map.class);

		assertEquals("Ra ro hill", list.get(0).get("name"));
	}

	@Test
	public void readItems_CompositeQuery() {
		QueryResult result = clusterCatalog.handle(new ReadItemCommand("SONGS",
				ItemFieldQuery.greater("year", 2012).and(ItemFieldQuery.greater("score", 8))));
		List<Map> list = result.getList("items", Map.class);

		assertEquals("Cuiudo do Alegrete", list.get(0).get("name"));
	}

	@Test
	public void deleteItem() {
		QueryResult result = clusterCatalog.handle(new ReadItemCommand("SONGS", ItemFieldQuery.equal("year", "1080")));
		List<Map> list = result.getList("items", Map.class);
		assertEquals("Blue Pill", list.get(0).get("name"));

		clusterCatalog.handle(new DeleteItemCommand(47, "SONGS", ItemFieldQuery.equal("year", "1080")));

		result = clusterCatalog.handle(new ReadItemCommand("SONGS", ItemFieldQuery.equal("year", "1080")));
		list = result.getList("items", Map.class);
		assertEquals(0, list.size());
	}

}
