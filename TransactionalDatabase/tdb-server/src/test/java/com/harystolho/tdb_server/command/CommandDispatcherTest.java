package com.harystolho.tdb_server.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.harystolho.tdb_server.cluster.command.InsertItemCommand;
import com.harystolho.tdb_server.transaction.command.CommitTransactionCommand;

@ExtendWith(MockitoExtension.class)
public class CommandDispatcherTest {

	@Mock
	private CommandHandler<InsertItemCommand> iicHandler;
	@Mock
	private CommandHandler<CommitTransactionCommand> ctcHandler;

	@Test
	public void registerAndDispatcherCommandToHandler_ShouldWork() {
		CommandDispatcher cd = new CommandDispatcher();
		cd.register(InsertItemCommand.class, iicHandler);

		InsertItemCommand iic = Mockito.mock(InsertItemCommand.class);
		Mockito.doReturn(InsertItemCommand.class).when(iic).getHandlerClassType();

		cd.dispatch(iic);

		Mockito.verify(iicHandler).handle(Mockito.any());
	}

}
