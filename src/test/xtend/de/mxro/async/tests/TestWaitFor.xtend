package de.mxro.async.tests

import de.oehme.xtend.junit.JUnit
import delight.async.jre.Async
import delight.functional.Success
import org.junit.Test

@JUnit
class TestWaitFor {
	
	@Test
	def void test() {
		
		Async.waitFor([cb | cb.onSuccess(Success.INSTANCE)])
		
	}
	
	
}