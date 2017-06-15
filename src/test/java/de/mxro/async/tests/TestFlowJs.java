package de.mxro.async.tests;

import java.util.Arrays;
import java.util.Collections;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class TestFlowJs {
  @Test
  public void test() {
    Object _object = new Object();
    Object _object_1 = new Object();
    Object _object_2 = new Object();
    final Object[] parameters = Collections.<Object>unmodifiableList(CollectionLiterals.<Object>newArrayList(Integer.valueOf(12), _object, _object_1, _object_2)).toArray();
    final Object[] operations = Arrays.<Object>copyOfRange(parameters, 1, parameters.length);
    Assert.assertEquals(3, operations.length);
  }
}
