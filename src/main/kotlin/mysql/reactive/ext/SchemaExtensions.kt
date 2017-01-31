package mysql.reactive.ext

import com.mysql.cj.api.x.DatabaseObject
import com.mysql.cj.api.x.Schema


fun Schema.dropTable(tableName: String) = this.session.dropTable(this.name, tableName)

fun Schema.exists() = this.existsInDatabase() == DatabaseObject.DbObjectStatus.EXISTS

fun Schema.notExists() = !this.exists()



