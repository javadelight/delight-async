package delight.async;

import delight.async.callbacks.ListCallback;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.flow.CallbackAggregator;
import delight.async.flow.CallbackMap;
import delight.async.helper.Aggregator;
import delight.functional.Closure;
import delight.functional.Closure2;
import delight.functional.Success;
import delight.functional.SuccessFail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Asynchronous operations which can be applied in a Java and JavaScript
 * environment.
 * 
 * @author <a href="http://www.mxro.de">Max Rohde</a>
 *
 */
public class AsyncCommon {

    /**
     * <p>
     * Tries to resolve a {@link Operation} immediately without waiting for the
     * asynchronous operation.
     * <p>
     * This is useful for operations which actually resolve in a synchronous
     * fashion (which might be added for legacy logic).
     * 
     * @param deferred
     * @return
     */
    public static final <ResultType> ResultType getDirty(final Operation<ResultType> deferred) {

        final Value<Boolean> resolved = new Value<Boolean>(false);
        final Value<ResultType> value = new Value<ResultType>(null);
        final Value<Throwable> exception = new Value<Throwable>(null);

        deferred.apply(new ValueCallback<ResultType>() {

            @Override
            public void onFailure(final Throwable t) {
                exception.set(t);
            }

            @Override
            public void onSuccess(final ResultType result) {

                value.set(result);
                resolved.set(true);
            }
        });

        if (exception.get() != null) {
            throw new RuntimeException(exception.get());
        }

        if (!resolved.get()) {
            throw new RuntimeException("Asynchronous get could not be resolved for " + deferred);
        }

        return value.get();

    }

    /**
     * <p>
     * Will apply the <b>asynchronous</b> operation operation to all inputs and
     * call the callback once all operations are completed.
     * <p>
     * Callback is also called upon the first operation which fails.
     * <p>
     * ValueCallback must be called in closure.
     * 
     * @param inputs
     * @param operation
     * @param callback
     */
    public static <InputType, ResultType> void map(final List<InputType> inputs,
            final AsyncFunction<InputType, ResultType> operation, final ListCallback<ResultType> callback) {

        final CallbackMap<InputType, ResultType> callbackMap = new CallbackMap<InputType, ResultType>(inputs, callback);

        for (final InputType input : inputs) {
            operation.apply(input, callbackMap.createCallback(input));
        }
    }

    public final static SimpleCallback asSimpleCallbackAndReturnSuccess(final ValueCallback<Object> callback) {
        return new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(Success.INSTANCE);
            }
        };
    }

    public final static SimpleCallback asSimpleCallback(final ValueCallback<Success> callback) {
        return new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(Success.INSTANCE);
            }
        };
    }

    public static final <T> ValueCallback<T> asValueCallback(final SimpleCallback callback) {
        return new ValueCallback<T>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final T value) {
                callback.onSuccess();
            }
        };
    }

    public static final Closure<SuccessFail> wrapAsClosure(final ValueCallback<Success> callback) {
        return new Closure<SuccessFail>() {

            @Override
            public void apply(final SuccessFail o) {
                if (o.isFail()) {
                    callback.onFailure(o.getException());
                    return;
                }

                callback.onSuccess(Success.INSTANCE);

            }
        };
    }

    public final static SimpleCallback doNothing() {
        return new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onSuccess() {

            }
        };
    }

    public final static SimpleCallback onSuccess(final Closure<Success> closure) {

        return new SimpleCallback() {

            @Override
            public void onFailure(final Throwable t) {
                throw new RuntimeException(t);
            }

            @Override
            public void onSuccess() {
                closure.apply(Success.INSTANCE);
            }
        };
    }

    /**
     * <p>
     * Creates a factory for callbacks. When these callbacks are called, their
     * results are aggregated <b>in the order in which the callbacks have been
     * created</b>.
     * <p>
     * If the factory has not been called the same number of times as results
     * are expected, the defined callback will be called when the created
     * callbacks have been called the number of expected times.
     * 
     * @param results
     * @param callWhenCollected
     * @return
     */
    public final static <V> Aggregator<V> collect(final int results, final ValueCallback<List<V>> callWhenCollected) {
        return new CallbackAggregator<V>(results, callWhenCollected);
    }

    public static <R, OP extends Operation<R>> void parallelAr(final OP[] operations,
            final ValueCallback<List<R>> callback) {
        parallel(Arrays.asList(operations), callback);
    }

    public static <R, OP extends Operation<R>> void parallel(final List<OP> operations,
            final ValueCallback<List<R>> callback) {
        final Aggregator<R> aggregator = collect(operations.size(), callback);

        for (final Operation<R> op : operations) {
            op.apply(aggregator.createCallback());
        }

    }

    /**
     * Perform the listed operations sequentially.
     * 
     * @param operations
     * @param callback
     */
    public static <R> void sequential(final List<Operation<R>> operations, final ValueCallback<List<R>> callback) {

        sequentialInt(operations, 0, new ArrayList<R>(operations.size()), callback);

    }

    private static <R> void sequentialInt(final List<Operation<R>> operations, final int idx, final List<R> results,
            final ValueCallback<List<R>> callback) {

        if (idx >= operations.size()) {
            callback.onSuccess(results);
            return;
        }

        operations.get(idx).apply(new ValueCallback<R>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final R value) {
                results.add(value);
                sequentialInt(operations, idx + 1, results, callback);
            }
        });

    }

    /**
     * <p>
     * Performs the provided operation on all elements of the provided list and
     * calls a callback with all results when completed.
     * <p>
     * Does not wait for the asynchronous result of one operation to proceed
     * with the other. Thus if operations are implemented asynchronously, the
     * operations will run in parallel.
     * 
     * @param elements
     * @param operation
     * @param callback
     */
    public final static <E, V> void forEach(final List<E> elements, final Closure2<E, ValueCallback<V>> operation,
            final ValueCallback<List<V>> callback) {

        final Aggregator<V> agg = collect(elements.size(), callback);

        for (final E element : elements) {
            final ValueCallback<V> itmcb = agg.createCallback();

            operation.apply(element, itmcb);
        }

    }

    /**
     * <p>
     * Embeds the closure within the provided callback.
     * <p>
     * Useful for cascading callbacks while avoiding to define onFailure method
     * declarations.
     * 
     * @param toCallback
     * @param onSuccess
     * @return
     */
    public final static <V> ValueCallback<V> embed(final ValueCallback<?> toCallback, final Closure<V> onSuccess) {
        return new ValueCallback<V>() {

            @Override
            public void onFailure(final Throwable t) {
                toCallback.onFailure(t);
            }

            @Override
            public void onSuccess(final V value) {
                onSuccess.apply(value);
            }
        };
    }

    public static <V> ValueCallback<List<V>> asValueCallback(final ListCallback<V> callback) {
        return new ValueCallback<List<V>>() {

            @Override
            public void onFailure(final Throwable t) {
                callback.onFailure(t);
            }

            @Override
            public void onSuccess(final List<V> value) {
                callback.onSuccess(value);
            }
        };
    }

    public static <V> ValueCallback<V> asValueCallback(final ValueCallback<Success> callback) {
        return AsyncCommon.embed(callback, new Closure<V>() {

            @Override
            public void apply(final V o) {
                callback.onSuccess(Success.INSTANCE);
            }
        });
    }

}
