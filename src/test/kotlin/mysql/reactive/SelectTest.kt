package mysql.reactive

import com.mysql.cj.api.x.Type
import com.mysql.cj.mysqlx.devapi.ColumnDef
import com.mysql.cj.x.MysqlxSessionFactory
import mysql.reactive.ext.notExists
import mysql.reactive.ext.toFlux
import org.junit.Before
import org.junit.Test
import reactor.test.StepVerifier

class SelectTest {

	val url = "mysqlx://localhost:33060/test?user=test&password=test"
	val session = MysqlxSessionFactory().getSession(url)
	val testDb = session.defaultSchema
	val usersTable = testDb.getTable("users")

	@Before
	fun setUp() {
		createUsersTable()
		insertSampleData()
	}

	private fun createUsersTable() {
		if (usersTable.notExists()) {
			testDb.createTable("users")
					.addColumn(ColumnDef("name", Type.STRING, 255))
					.addColumn(ColumnDef("age", Type.INT))
					.execute()
		} else {
			usersTable.delete().execute()
		}
	}

	private fun insertSampleData() {
		usersTable.insert("age", "name").values(30, "Sherlock Holmes").execute()
		usersTable.insert().values("John Watson", 35).execute()
		usersTable.insert("age").values(40).execute()
		usersTable.insert("name").values("James Moriarty").execute()
	}

	@Test
	fun `should get all users with name`() {
		val users = usersTable.select("name")
				.where("name IS NOT NULL")
				.orderBy("name")
				.toFlux()
				.map { it.getString("name") }


		StepVerifier.create(users)
				.expectNext("James Moriarty", "John Watson", "Sherlock Holmes")
				.expectComplete()
				.verify()
	}

	@Test
	fun `should get all users with name using flux methods`() {
		val users = usersTable.select()
				.toFlux()
				.filter { it.getString("name") != null }
				.map { it.getString("name") }
				.sort()

		StepVerifier.create(users)
				.expectNext("James Moriarty", "John Watson", "Sherlock Holmes")
				.expectComplete()
				.verify()
	}


}


