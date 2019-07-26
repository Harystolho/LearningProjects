#Query Specification

All queries return a QueryResult object. This object is basically a map that contains the values returned by the query. If some error happens in  the database the client connection will throw an error.

**Begin Transaction**

Returns a unique transaction id. 

To retrieve the id call `queryResult.getLong("id")`

> `BEGIN TRANSACTION`

**Commit Transaction**

Commits a transaction using the transaction id. If there is no transaction with the specified id, nothing happens.

> `'{TX_ID}' COMMIT`

> `'123' COMMIT` - Commits the transaction with id 123

**Rollback Transaction**

Rollbacks a transaction using the transaction id. If there is no transaction with the specified id, nothing happens. 

> `'{TX_ID}' ROLLBACK`

> `'478' ROLLBACK` - Rollbacks the transaction with id 478