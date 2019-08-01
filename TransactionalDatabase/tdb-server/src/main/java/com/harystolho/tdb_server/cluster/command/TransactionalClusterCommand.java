package com.harystolho.tdb_server.cluster.command;

public abstract class TransactionalClusterCommand extends ClusterCommand {

	public static final long NO_TRANSACTION = -1;

	protected long transactionId;

	public TransactionalClusterCommand(String cluster, long transactionId) {
		super(cluster);
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}

}
