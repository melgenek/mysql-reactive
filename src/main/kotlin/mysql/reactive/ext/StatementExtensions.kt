package mysql.reactive.ext

import com.mysql.cj.api.x.Row
import com.mysql.cj.api.x.SelectStatement
import reactor.core.publisher.Flux

val NOTHING = null

fun SelectStatement.toFlux(): Flux<Row> {
	return Flux.create<Row> { emitter ->
		this.executeAsync(NOTHING) { _, row ->
			emitter.next(row)
			NOTHING
		}.handle { _, error ->
			if (error != null) {
				emitter.error(error)
			} else {
				emitter.complete()
			}
		}
	}
}

