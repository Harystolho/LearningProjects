package com.harystolho.tdb_server.cluster.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tdb_server.cluster.Item;
import com.harystolho.tdb_server.command.Command;
import com.harystolho.tdb_server.transaction.LogBlock;

@ExtendWith(MockitoExtension.class)
public class UpdateItemCommandTest {

	@SuppressWarnings("unchecked")
	@Test
	public void toLogBlock_ShouldWork() {
		UpdateItemCommand uic = new UpdateItemCommand(6, "PRODUCTS", null, Map.of());

		LogBlock logBlock = uic.toLogBlock(Arrays.asList(Map.of("_id", "457", "name", "peterson", "age", "47")));

		assertEquals(6, logBlock.getTransactionId());
		assertEquals("UPDATE_ITEM", logBlock.getType());

		Map<String, Object> values = (Map<String, Object>) logBlock.getObject();

		assertEquals("PRODUCTS", values.get("_cluster"));
		assertEquals(1, ((List<?>) values.get("items")).size());
	}

	@Test
	public void createOneCommand_fromLogBlock_ShouldWork() {
		UpdateItemCommand uic = new UpdateItemCommand(0, "BOATS", null, null);
		List<Map<String, String>> items = new ArrayList<>();
		Item i1 = new Item();

		items.add(Map.of("_id", i1.get("_id"), "year", "2004"));

		LogBlock block = uic.toLogBlock(items);
		List<UpdateItemCommand> undo = UpdateItemCommand.undo(block);

		assertEquals(1, undo.size());

		UpdateItemCommand uic2 = undo.get(0);

		assertEquals("BOATS", uic2.getClusterName());

		assertTrue(uic2.getQuery().isSatisfiedBy(i1));
	}

	@Test
	public void createTwoCommands_fromLogBlock_ShouldWork() {
		UpdateItemCommand updateCommand = new UpdateItemCommand(0, "BOATS", null, null);
		List<Map<String, String>> items = new ArrayList<>();
		Item i1 = new Item();
		Item i2 = new Item();

		items.add(Map.of("_id", String.valueOf(i1.getId()), "name", "peter"));
		items.add(Map.of("_id", String.valueOf(i2.getId()), "name", "john"));

		LogBlock block = updateCommand.toLogBlock(items);

		List<UpdateItemCommand> undo = UpdateItemCommand.undo(block);

		assertEquals(2, undo.size());

		UpdateItemCommand uic1 = undo.get(0);
		UpdateItemCommand uic2 = undo.get(1);

		assertEquals("peter", uic1.getNewValues().get("name"));
		assertEquals("john", uic2.getNewValues().get("name"));

		assertTrue(uic1.getQuery().isSatisfiedBy(i1));
		assertTrue(uic2.getQuery().isSatisfiedBy(i2));
	}

}
