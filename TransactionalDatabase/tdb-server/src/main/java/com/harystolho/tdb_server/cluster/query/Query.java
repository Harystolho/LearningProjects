package com.harystolho.tdb_server.cluster.query;

import java.util.function.Function;

public abstract class Query<T> {

	public abstract boolean isSatisfiedBy(T t);

	public static <T> Query<T> make(Function<T, Boolean> func) {
		return new Query<T>() {
			@Override
			public boolean isSatisfiedBy(T t) {
				return func.apply(t);
			}
		};
	}

	/**
	 * @param other
	 * @return a query that only returns <code>true</code> if the value is satisfied
	 *         by this query and by the other one
	 */
	public Query<T> and(Query<T> other) {
		return Query.make((t) -> isSatisfiedBy(t) && other.isSatisfiedBy(t));
	}

	/**
	 * @param other
	 * @return a query that returns <code>true</code> if the value is satisfied by
	 *         this query or by the other one
	 */
	public Query<T> or(Query<T> other) {
		return Query.make((t) -> isSatisfiedBy(t) || other.isSatisfiedBy(t));
	}

	/**
	 * @param other
	 * @return a query that only returns <code>true</code> if the value is not
	 *         satisfied by this query
	 */
	public Query<T> not() {
		return Query.make((t) -> !isSatisfiedBy(t));
	}

}
