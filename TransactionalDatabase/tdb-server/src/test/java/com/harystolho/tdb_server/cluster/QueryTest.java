package com.harystolho.tdb_server.cluster;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.harystolho.tdb_server.cluster.command.query.Query;

public class QueryTest {

	@Test
	public void notQuery() {
		Query<Integer> isPositive = Query.make(t -> t > 0);

		var negativeQuery = isPositive.not();

		assertTrue(negativeQuery.isSatisfiedBy(-1));

		assertFalse(negativeQuery.isSatisfiedBy(5));
	}

	@Test
	public void orQuery() {
		Query<Integer> greaterThan20 = Query.make(t -> t > 20);
		Query<Integer> lessThan5 = Query.make(t -> t < 5);

		var orQuery = greaterThan20.or(lessThan5);

		assertTrue(orQuery.isSatisfiedBy(25));
		assertTrue(orQuery.isSatisfiedBy(3));

		assertFalse(orQuery.isSatisfiedBy(15));
	}

	@Test
	public void andQuery() {
		Query<String> constainsA = Query.make(t -> t.contains("A"));
		Query<String> constainsZ = Query.make(t -> t.contains("Z"));

		var andQuery = constainsA.and(constainsZ);

		assertTrue(andQuery.isSatisfiedBy("BRAZIL"));

		assertFalse(andQuery.isSatisfiedBy("BRASIL"));
	}

}
