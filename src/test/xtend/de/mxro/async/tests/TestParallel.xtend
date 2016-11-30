package de.mxro.async.tests

import de.oehme.xtend.junit.JUnit
import delight.async.AsyncCommon
import delight.async.Operation
import delight.async.jre.Async
import java.util.ArrayList
import java.util.List
import org.junit.Test

@JUnit
class TestParallel {

	@Test
	def void test_with_list() {

		val ops = new ArrayList<Operation<String>>

		ops.add(
			[ cb |
				cb.onSuccess("123")
			])

		ops.add(
			[ cb |
				cb.onSuccess("456")
			])

		val res = Async.waitFor(
			[ cb |
				AsyncCommon.parallel(ops, cb)
			])

		(res.size == 2) => true

	}

	
	@Test
	def void test_with_array() {

		val Operation<?>[] ops = #[
			[  cb |
				cb.onSuccess("123")
			],
			[  cb |
				cb.onSuccess("456")
			]
		]

		val res = Async.waitFor(
			[ cb |
				AsyncCommon.parallelAr(ops, cb)
			])

		(res.size == 2) => true

	}
	
	@Test
	def void test_with_maxOps() {

		val List<Operation<String>> ops = new ArrayList
		
		for (i:1..100) {
			ops.add [cb | cb.onSuccess("1")]
		}
		
		val res = Async.waitFor(
			[ cb |
				AsyncCommon.parallel(ops, 10, cb)
			])

		(res.size == 100) => true

	}

}
