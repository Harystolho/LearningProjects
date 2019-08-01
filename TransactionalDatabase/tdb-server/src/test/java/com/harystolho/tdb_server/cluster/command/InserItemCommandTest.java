package com.harystolho.tdb_server.cluster.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.harystolho.tdb_server.cluster.Item;
import com.harystolho.tdb_server.transaction.LogBlock;

public class InserItemCommandTest {

	@Test
	public void undoInsertCommand_ShouldWork() {
		InsertItemCommand iic = new InsertItemCommand(0, "PLANTS", null);

		Item i1 = new Item();
		LogBlock logBlock = iic.toLogBlock(i1.getId());

		DeleteItemCommand undo = InsertItemCommand.undo(logBlock);

		assertEquals("PLANTS", undo.getClusterName());
		assertTrue(undo.getQuery().isSatisfiedBy(i1));
	}

}
