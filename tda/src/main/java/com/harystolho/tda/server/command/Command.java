package com.harystolho.tda.server.command;

import com.harystolho.tda.shared.QueryResult;

public interface Command<T> {
	public QueryResult execute(T t);

	public QueryResult undo(T t);

	public Class<?> getHandlerClassType();
}
