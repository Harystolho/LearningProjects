package com.harystolho.it;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tdb_server.cluster.ClusterCatalog;
import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.transaction.TransactionJournal;
import com.harystolho.tdb_server.transaction.TransactionLogger;
import com.harystolho.tdb_server.transaction.command.BeginTransactionCommand;
import com.harystolho.tdb_server.transaction.command.CommitTransactionCommand;
import com.harystolho.tdb_shared.QueryResult;

@ExtendWith(MockitoExtension.class)
public class TransactionCommandTest {

	private ClusterCatalog clusterCatalog;
	private TransactionJournal transactionJournal;
	private TransactionLogger transactionLogger;

	@Test
	public void test() {
		QueryResult beginTxResult = transactionJournal.handle(new BeginTransactionCommand());
		long txId = beginTxResult.getLong("id");

		InsertItemCommand iic = new InsertItemCommand(txId, "CROPS", Map.of("name", "wheat", "size", "large"));
		clusterCatalog.handle(iic);

		transactionJournal.handle(new CommitTransactionCommand(txId));
	}

}
