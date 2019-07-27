package com.harystolho.tdb_server.cluster.command;

public abstract class TransactionalClusterCommand extends ClusterCommand {

	protected long transactionId;

	public TransactionalClusterCommand(String cluster, long transactionId) {
		super(cluster);
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}

}
