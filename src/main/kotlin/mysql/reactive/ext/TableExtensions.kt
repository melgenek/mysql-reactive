package mysql.reactive.ext

import com.mysql.cj.api.x.DatabaseObject
import com.mysql.cj.api.x.Table

fun Table.exists() = this.existsInDatabase() == DatabaseObject.DbObjectStatus.EXISTS

fun Table.notExists() = !this.exists()

