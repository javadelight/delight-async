package delight.async.jre;

import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.Value;
import delight.async.callbacks.ValueCallback;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Asynchronous utilities which are only available in Oracle Java, OpenJDK and
 * Android (and not for JavaScript environments).
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class Async extends AsyncCommon {

    /**
     * <p>
     * Executes the specified {@link Operation} operation and blocks the calling
     * thread until the operation is completed.
     * 
     * @param operation
     *            The deferred operation to be executed.
     * @return The result of the deferred operation.
     */
    public static final <T> T waitFor(final int timeout, final Operation<T> operation) {
        final CountDownLatch latch = new CountDownLatch(1);
        final Value<T> result = new Value<T>(null);
        final Value<Throwable> failure = new Value<Throwable>(null);

        operation.apply(new ValueCallback<T>() {

            @Override
            public void onFailure(final Throwable t) {
                failure.set(t);
                latch.countDown();
            }

            @Override
            public void onSuccess(final T value) {
                result.set(value);
                latch.countDown();
            }
        });

        try {
            latch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (latch.getCount() > 0) {
            throw new RuntimeException("Operation not completed in timeout: " + operation);
        }

        if (failure.get() != null) {
            throw new RuntimeException(failure.get());
            // throw new
            // RuntimeException("Exception while performing operation.",
            // failure.get());
        }

        return result.get();

    }

    /**
     * Executes the specified {@link Operation} operation and blocks the calling
     * thread until the operation is completed.
     * 
     * @param operation
     *            The deferred operation to be executed.
     * @return The result of the deferred operation.
     */
    public static final <T> T waitFor(final Operation<T> operation) {
        return waitFor(30000, operation);

    }

    public static <ResultType, OP extends Operation<ResultType>> void parallel(final OP[] operationsRaw) {
        parallel(Arrays.asList(operationsRaw));
    }

    public static <ResultType, OP extends Operation<ResultType>> void parallel(final List<OP> operationsRaw) {
        waitFor(new Operation<List<ResultType>>() {

            @Override
            public void apply(final ValueCallback<List<ResultType>> callback) {
                AsyncCommon.parallel(operationsRaw, callback);
            }

        });
    }
}
