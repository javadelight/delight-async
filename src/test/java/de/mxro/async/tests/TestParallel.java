package de.mxro.async.tests;

import de.oehme.xtend.junit.JUnit;
import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure0;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

@JUnit
@SuppressWarnings("all")
public class TestParallel {
  @Test
  public void test_with_list() {
    final ArrayList<Operation<String>> ops = new ArrayList<Operation<String>>();
    final Operation<String> _function = new Operation<String>() {
      @Override
      public void apply(final ValueCallback<String> cb) {
        cb.onSuccess("123");
      }
    };
    ops.add(_function);
    final Operation<String> _function_1 = new Operation<String>() {
      @Override
      public void apply(final ValueCallback<String> cb) {
        cb.onSuccess("456");
      }
    };
    ops.add(_function_1);
    final Operation<List<String>> _function_2 = new Operation<List<String>>() {
      @Override
      public void apply(final ValueCallback<List<String>> cb) {
        AsyncCommon.<String, Operation<String>>parallel(ops, cb);
      }
    };
    final List<String> res = Async.<List<String>>waitFor(_function_2);
    int _size = res.size();
    boolean _equals = (_size == 2);
    TestParallel.<Boolean, Boolean>operator_doubleArrow(Boolean.valueOf(_equals), Boolean.valueOf(true));
  }
  
  @Test
  public void test_with_array() {
    final Operation<Object> _function = new Operation<Object>() {
      @Override
      public void apply(final ValueCallback<Object> cb) {
        cb.onSuccess("123");
      }
    };
    final Operation<Object> _function_1 = new Operation<Object>() {
      @Override
      public void apply(final ValueCallback<Object> cb) {
        cb.onSuccess("456");
      }
    };
    final Operation<?>[] ops = { _function, _function_1 };
    final Operation<List<Object>> _function_2 = new Operation<List<Object>>() {
      @Override
      public void apply(final ValueCallback<List<Object>> cb) {
        AsyncCommon.<Object, Operation<?>>parallelAr(ops, cb);
      }
    };
    final List<Object> res = Async.<List<Object>>waitFor(_function_2);
    int _size = res.size();
    boolean _equals = (_size == 2);
    TestParallel.<Boolean, Boolean>operator_doubleArrow(Boolean.valueOf(_equals), Boolean.valueOf(true));
  }
  
  @Test
  public void test_with_maxOps() {
    final List<Operation<String>> ops = new ArrayList<Operation<String>>();
    IntegerRange _upTo = new IntegerRange(1, 100);
    for (final Integer i : _upTo) {
      final Operation<String> _function = new Operation<String>() {
        @Override
        public void apply(final ValueCallback<String> cb) {
          cb.onSuccess("1");
        }
      };
      ops.add(_function);
    }
    final Operation<List<String>> _function_1 = new Operation<List<String>>() {
      @Override
      public void apply(final ValueCallback<List<String>> cb) {
        AsyncCommon.<String, Operation<String>>parallel(ops, 10, cb);
      }
    };
    final List<String> res = Async.<List<String>>waitFor(_function_1);
    int _size = res.size();
    boolean _equals = (_size == 100);
    TestParallel.<Boolean, Boolean>operator_doubleArrow(Boolean.valueOf(_equals), Boolean.valueOf(true));
  }
  
  @Test
  public void test_with_maxOps_threads() {
    final List<Operation<String>> ops = new ArrayList<Operation<String>>();
    IntegerRange _upTo = new IntegerRange(1, 100);
    for (final Integer i : _upTo) {
      final Operation<String> _function = new Operation<String>() {
        @Override
        public void apply(final ValueCallback<String> cb) {
          final Runnable _function = new Runnable() {
            @Override
            public void run() {
              try {
                Random _random = new Random();
                int _nextInt = _random.nextInt(100);
                int _plus = (_nextInt + 1);
                Thread.sleep(_plus);
                cb.onSuccess("1");
              } catch (Throwable _e) {
                throw Exceptions.sneakyThrow(_e);
              }
            }
          };
          Thread _thread = new Thread(_function);
          _thread.start();
        }
      };
      ops.add(_function);
    }
    final Operation<List<String>> _function_1 = new Operation<List<String>>() {
      @Override
      public void apply(final ValueCallback<List<String>> cb) {
        AsyncCommon.<String, Operation<String>>parallel(ops, 15, cb);
      }
    };
    final List<String> res = Async.<List<String>>waitFor(_function_1);
    int _size = res.size();
    boolean _equals = (_size == 100);
    TestParallel.<Boolean, Boolean>operator_doubleArrow(Boolean.valueOf(_equals), Boolean.valueOf(true));
  }
  
  private static void assertArrayEquals(final Object[] expecteds, final Object[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final byte[] expecteds, final byte[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final char[] expecteds, final char[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final short[] expecteds, final short[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final int[] expecteds, final int[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final long[] expecteds, final long[] actuals) {
    Assert.assertArrayEquals(expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final Object[] expecteds, final Object[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final byte[] expecteds, final byte[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final char[] expecteds, final char[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final short[] expecteds, final short[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final int[] expecteds, final int[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final String message, final long[] expecteds, final long[] actuals) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals);
  }
  
  private static void assertArrayEquals(final double[] expecteds, final double[] actuals, final double delta) {
    Assert.assertArrayEquals(expecteds, actuals, delta);
  }
  
  private static void assertArrayEquals(final float[] expecteds, final float[] actuals, final float delta) {
    Assert.assertArrayEquals(expecteds, actuals, delta);
  }
  
  private static void assertArrayEquals(final String message, final double[] expecteds, final double[] actuals, final double delta) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals, delta);
  }
  
  private static void assertArrayEquals(final String message, final float[] expecteds, final float[] actuals, final float delta) throws ArrayComparisonFailure {
    Assert.assertArrayEquals(message, expecteds, actuals, delta);
  }
  
  private static void assertEquals(final Object expected, final Object actual) {
    Assert.assertEquals(expected, actual);
  }
  
  private static void assertEquals(final long expected, final long actual) {
    Assert.assertEquals(expected, actual);
  }
  
  private static void assertEquals(final String message, final Object expected, final Object actual) {
    Assert.assertEquals(message, expected, actual);
  }
  
  private static void assertEquals(final String message, final long expected, final long actual) {
    Assert.assertEquals(message, expected, actual);
  }
  
  private static void assertEquals(final double expected, final double actual, final double delta) {
    Assert.assertEquals(expected, actual, delta);
  }
  
  private static void assertEquals(final String message, final double expected, final double actual, final double delta) {
    Assert.assertEquals(message, expected, actual, delta);
  }
  
  private static void assertFalse(final boolean condition) {
    Assert.assertFalse(condition);
  }
  
  private static void assertFalse(final String message, final boolean condition) {
    Assert.assertFalse(message, condition);
  }
  
  private static void assertNotNull(final Object object) {
    Assert.assertNotNull(object);
  }
  
  private static void assertNotNull(final String message, final Object object) {
    Assert.assertNotNull(message, object);
  }
  
  private static void assertNotSame(final Object unexpected, final Object actual) {
    Assert.assertNotSame(unexpected, actual);
  }
  
  private static void assertNotSame(final String message, final Object unexpected, final Object actual) {
    Assert.assertNotSame(message, unexpected, actual);
  }
  
  private static void assertNull(final Object object) {
    Assert.assertNull(object);
  }
  
  private static void assertNull(final String message, final Object object) {
    Assert.assertNull(message, object);
  }
  
  private static void assertSame(final Object expected, final Object actual) {
    Assert.assertSame(expected, actual);
  }
  
  private static void assertSame(final String message, final Object expected, final Object actual) {
    Assert.assertSame(message, expected, actual);
  }
  
  private static <T extends Object> void assertThat(final T actual, final Matcher<T> matcher) {
    Assert.<T>assertThat(actual, matcher);
  }
  
  private static <T extends Object> void assertThat(final String reason, final T actual, final Matcher<T> matcher) {
    Assert.<T>assertThat(reason, actual, matcher);
  }
  
  private static void assertTrue(final boolean condition) {
    Assert.assertTrue(condition);
  }
  
  private static void assertTrue(final String message, final boolean condition) {
    Assert.assertTrue(message, condition);
  }
  
  private static void fail() {
    Assert.fail();
  }
  
  private static void fail(final String message) {
    Assert.fail(message);
  }
  
  private static <T extends Object, U extends T> void operator_doubleArrow(final T actual, final U expected) {
    Assert.assertEquals(expected, actual);
  }
  
  private static <T extends Exception> void isThrownBy(final Class<T> expected, final Procedure0 block) {
    try {
    	block.apply();
    	Assert.fail("Expected a " + expected.getName());
    } catch (Exception e) {
    	Class<?> actual = e.getClass();
    	Assert.assertTrue(
    		"Expected a " + expected.getName() + " but got " + actual.getName(), 
    		expected.isAssignableFrom(actual)
    	);
    }
  }
}
