package com.harystolho.tda.server.command;

import com.harystolho.tda.shared.QueryResult;

public interface Command<T> {
	public QueryResult execute(T t);

	public QueryResult undo(T t);

	/**
	 * Commands are handled by {@link CommandHandler CommandHandlers}, these
	 * handlers need to register themselves in the {@link CommandDispatcher} to
	 * receive commands. Usually multiple commands are handled by the same handler
	 * and because Java doesn't allow generic multiple inheritance(implement the
	 * same generic interface with different types) the way to work around that is
	 * to create a super class that extends {@link Command}, register the handler
	 * using the super class, make all classes handled by the handler extend the
	 * super class and return the super class in this method in the child classes.
	 * 
	 * 
	 * @return
	 */
	public Class<?> getHandlerClassType();
}
