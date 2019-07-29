package com.harystolho.tdb_server.cluster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.transaction.CommandLogger;

@ExtendWith(MockitoExtension.class)
public class ClusterTest {

	private Cluster cluster;

	private List<Item> items;

	@Mock
	private CommandLogger logger;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void beforeEach() {
		items = (List<Item>) Mockito.mock(List.class);
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

		Map<String, String> map = new HashMap<>();
		map.put("name", "john");
		map.put("age", "7");

		InsertItemCommand iic = new InsertItemCommand(4, "TEST_CLUSTER", map);

		cluster.handle(iic);
	}

}
