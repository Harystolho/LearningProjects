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

**Delete Item**

Deletes item(s) from a cluster. 

> `'{TX_ID}' DELETE (key=value,key=value,...) | {CLUSTER_NAME}`

> `'194' DELETE (year=2008) | SONGS` - Deletes all songs that have the field "year" equal to "2008" from the SONGS cluster

**Update Item**

Updates item(s) in a cluster. 

> `'{TX_ID}' UPDATE (key=value,key=value,...) (key=value,key=value,...) | {CLUSTER_NAME}`

> `'974' UPDATE (color=blue) (quality=high) | PLANTS` - Sets the "quality" field to "high" on all items that have the "color" field equal to "blue" on the PLANTS cluster

**Read Item**

Reads items from a cluster. 

To retrieve the items call `queryResult.getList("items")`, the items are a regular Map

> `READ (key=value,key=value,...) | {CLUSTER_NAME}`

> `READ (seed=wheat,quality=low) | CROPS` - Returns all the items that have the seed field equal to "wheat" and quality to "low" from the CROPS cluster





