package com.harystolho.tda_client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionTest {

	@Mock
	private Connection conn;

	@Test
	public void commitTransaction_ShouldInsertTransactionIdInTheBeginning() {
		Transaction tx = new Transaction(4, conn);
		tx.commit();

		Mockito.verify(conn).execQuery(Mockito.eq("'4' COMMIT"));
	}

	@Test
	public void rollbackTransaction_ShouldInsertTransactionIdInTheBeginning() {
		Transaction tx = new Transaction(234, conn);
		tx.rollback();

		Mockito.verify(conn).execQuery(Mockito.eq("'234' ROLLBACK"));
	}

	@Test
	public void execQuery_ShouldInsertTransactionIdInTheBeginning() {
		Transaction tx = new Transaction(447, conn);
		tx.execQuery("INSERT name=log,age=1 | USERS");

		Mockito.verify(conn).execQuery(Mockito.eq("'447' INSERT name=log,age=1 | USERS"));
	}

	@Test
	public void transaction_ShouldBeEqualToItself() {
		Transaction tx1 = new Transaction(674, conn);
		Transaction tx2 = new Transaction(674, conn);

		assertTrue(tx1.equals(tx2));
	}

	@Test
	public void transaction_ShouldNotBeEqualToOtherTransaction() {
		Transaction tx1 = new Transaction(123, conn);
		Transaction tx2 = new Transaction(456, conn);

		assertFalse(tx1.equals(tx2));
	}

}
