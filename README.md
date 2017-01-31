# Reactive Mysql access using [X DevApi](https://dev.mysql.com/doc/x-devapi-userguide/en/devapi-users-introduction.html) and [Spring Reactor](https://github.com/reactor/reactor-core)

This repository provides an example of using MySQL Connector/J adapted to Reactor `Flux`. The `X DevAPI` seems to be appropriate
to implement [reactive streams](http://www.reactive-streams.org/) compatible api for db interaction.



### Sample usage

Following snippet shows builder-like query transformed to `Flux`. 
The full example with data setup and tests can be found [here](https://github.com/melgene/mysql-reactive/blob/master/src/test/kotlin/mysql/reactive/SelectTest.kt).

```kotlin
usersTable.select("name")
          .where("name IS NOT NULL")
          .orderBy("name")
          .toFlux()
          .map { it.getString("name") }
```

### Running examples

To use `X Protocol` for database quering you should install `X plugin`
For instructions refer to [documentation](https://dev.mysql.com/doc/refman/5.7/en/document-store-setting-up.html)

You can also use `docker-compose.yml` provided. 
Just open `/scrips` folder of current repository and start docker from command line.

```shell
docker-compose up
```
