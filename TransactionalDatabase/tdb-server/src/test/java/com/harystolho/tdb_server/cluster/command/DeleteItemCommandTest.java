package com.harystolho.tdb_server.cluster.command;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.harystolho.tdb_server.cluster.Item;
import com.harystolho.tdb_server.command.Command;
import com.harystolho.tdb_server.transaction.LogBlock;

public class DeleteItemCommandTest {

	@Test
	public void undoDeleteCommand() {
		DeleteItemCommand dic = new DeleteItemCommand(0, "PLANES", null);

		Item i1 = Item.fromMap(Map.of("name", "peter"));
		Item i2 = Item.fromMap(Map.of("name", "john"));

		LogBlock logBlock = dic.toLogBlock(Arrays.asList(i1.toMap(), i2.toMap()));

		List<InsertItemCommand> undos = DeleteItemCommand.undo(logBlock);

		assertEquals(2, undos.size());

		InsertItemCommand iic1 = undos.get(0);
		InsertItemCommand iic2 = undos.get(1);

		assertEquals("PLANES", iic1.getClusterName());
		assertEquals("PLANES", iic2.getClusterName());

		assertEquals("peter", iic1.getValues().get("name"));
		assertEquals("john", iic2.getValues().get("name"));
	}

}
