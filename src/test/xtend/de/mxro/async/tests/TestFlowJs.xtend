package de.mxro.async.tests

import java.util.Arrays
import org.junit.Assert
import org.junit.Test

class TestFlowJs {
	
	@Test
	def void test() {
		
		val parameters = #[12, new Object(), new Object(), new Object()].toArray
		
		val Object[] operations = Arrays.copyOfRange(parameters, 1, parameters.length);
		
		Assert.assertEquals(3, operations.length)
		
		
	}
	
}