package mysql.reactive

import com.mysql.cj.api.x.Type
import com.mysql.cj.mysqlx.devapi.ColumnDef
import com.mysql.cj.x.MysqlxSessionFactory
import mysql.reactive.ext.dropTable
import mysql.reactive.ext.notExists
import mysql.reactive.ext.toFlux


fun main(args: Array<String>) {
	val url = "mysqlx://localhost:33060/test?user=test&password=test"
	val session = MysqlxSessionFactory().getSession(url)
	val schemaList = session.schemas
	println("Available schemas in this session:")
	for (schema in schemaList) {
		println(schema)
	}
	if (session.getSchema("test").notExists()) {
		session.createSchema("test")
	}

	val testDb = session.defaultSchema
	println("Default schema : " + testDb)

	if (testDb.getTable("users").notExists()) {
		testDb.createTable("users")
				.addColumn(ColumnDef("name", Type.STRING, 255))
				.addColumn(ColumnDef("age", Type.INT))
				.execute()
	} else {
		testDb.getTable("users").delete().execute()
	}

	val usersTable = testDb.getTable("users")

	usersTable.insert("age", "name").values(30, "Sherlock").execute()
	usersTable.insert().values("John", 35).execute()
	usersTable.insert("age").values(40).execute()
	usersTable.insert("name").values("James").execute()

	usersTable.select().toFlux()
			.doOnNext { println(it.getString("name") + "::" + it.getBigDecimal("age")) }
			.then().block()

	testDb.dropTable("users")
	session.close()
}
