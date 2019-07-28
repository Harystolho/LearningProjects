#Query Specification

All queries return a QueryResult object. This object is basically a map that contains the values returned by the query. If some error happens in the database the client connection will throw an error.

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


**Transactional Commands**

Transactional Commands are those commands that can be executed in a transaction, they are:

- Insert Item command
- Update Item command
- Delete Item command

They can also be executed outside a transaction. The syntax for the commands above can be written in two ways. Specifying the transaction id in the query or leaving it out. The examples below will show the transaction id in the query but it's optional.  

**Insert Item**

Inserts an item in a cluster. 

> `'{TX_ID}' INSERT (key=value,key=value,...) | {CLUSTER_NAME}`

> `'445' INSERT (name=Peter,age=15) | USERS` - Inserts an item that has 2 fields(name and age) into the USERS cluster







