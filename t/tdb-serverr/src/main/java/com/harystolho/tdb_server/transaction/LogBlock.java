package com.harystolho.tdb_server.transaction;

public final class LogBlock {

	private final long transactionId;
	private final String type;
	private final Object object;

	public LogBlock(long transactionId, String type, Object object) {
		this.transactionId = transactionId;
		this.type = type;
		this.object = object;
	}

	public LogBlock(long transactionId, String type) {
		this(transactionId, type, 0);
	}

	public long getTransactionId() {
		return transactionId;
	}

	public String getType() {
		return type;
	}

	public Object getObject() {
		return object;
	}

	@Override
	public String toString() {
		return "LogBlock [transactionId=" + transactionId + ", type=" + type + ", object=" + object + "]";
	}

}
