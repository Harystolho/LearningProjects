package com.harystolho.tdb_server.command;

import com.harystolho.tdb_shared.QueryResult;

public interface CommandHandler<C extends Command<?>> {

	public QueryResult handle(C command);
	
}
