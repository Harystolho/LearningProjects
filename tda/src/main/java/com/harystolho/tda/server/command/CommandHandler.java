package com.harystolho.tda.server.command;

import com.harystolho.tda.shared.QueryResult;

public interface CommandHandler<C extends Command<?>> {

	public QueryResult handle(C command);
	
}
