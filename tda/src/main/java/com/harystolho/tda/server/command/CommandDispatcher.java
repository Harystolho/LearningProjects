package com.harystolho.tda.server.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.harystolho.tda.shared.QueryResult;
import com.harystolho.tda.shared.exception.DatabaseException;

public class CommandDispatcher {

	private Map<Class<? extends Command<?>>, CommandHandler<?>> handlers;

	public CommandDispatcher() {
		handlers = new ConcurrentHashMap<>();
	}

	public <C extends Command<?>> void register(Class<C> clazz, CommandHandler<C> handler) {
		handlers.put(clazz, handler);
	}

	@SuppressWarnings("unchecked")
	public <C extends Command<?>> QueryResult dispatch(C command) {
		CommandHandler<C> handler = (CommandHandler<C>) handlers.get(command.getHandlerClassType());

		if (handler != null)
			return handler.handle(command);

		return new QueryResult(new DatabaseException("HANDLER_NOT_FOUND"));
	}

}
