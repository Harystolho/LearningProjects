package com.harystolho.tda.server.command;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.harystolho.tda.shared.QueryResult;

public class CommandDispatcher {

	private Map<Class<? extends Command<?>>, CommandHandler<? extends Command<?>>> handlers;

	public CommandDispatcher() {
		handlers = new ConcurrentHashMap<>();
	}

	public <C extends Command<?>> void register(Class<C> clazz, CommandHandler<C> handler) {
		handlers.put(clazz, handler);
	}

	@SuppressWarnings("unchecked")
	public <C extends Command<?>> QueryResult dispatch(C command) {
		CommandHandler<C> handler = (CommandHandler<C>) handlers.get(command.getClass());

		if (handler != null)
			return handler.handle(command);

		return null;
	}

}
