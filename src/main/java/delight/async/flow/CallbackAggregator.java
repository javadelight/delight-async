package delight.async.flow;

import delight.async.Value;
import delight.async.callbacks.ValueCallback;
import delight.async.helper.Aggregator;
import delight.functional.collections.CollectionsUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CallbackAggregator<V> implements Aggregator<V> {

    final int expected;

    final ValueCallback<List<V>> callback;

    Value<Integer> callbacksDefined;
    Map<Integer, V> resultsMap;
    List<V> results;
    Value<Throwable> exceptionReceived;
    Throwable exception;

    @Override
    public final ValueCallback<V> createCallback() {

        synchronized (callbacksDefined) {

            final int callbackIdx = callbacksDefined.get();

            callbacksDefined.set(callbackIdx + 1);

            if (callbackIdx > expected - 1) {
                throw new IllegalStateException("Too many callbacks defined.");
            }

            return new ValueCallback<V>() {

                @Override
                public void onFailure(final Throwable t) {
                    synchronized (exceptionReceived) {
                        // t.printStackTrace();
                        // only trigger onFailure for first exception received
                        if (exceptionReceived.get() != null) {
                            return;
                        }

                        exceptionReceived.set(t);

                    }
                    callback.onFailure(t);
                }

                @Override
                public void onSuccess(final V value) {

                    boolean callWithMap = false;
                    synchronized (resultsMap) {
                        assert resultsMap.get(callbackIdx) == null : "Callback for aggregator called twice: "
                                + callback;

                        resultsMap.put(callbackIdx, value);

                        if (resultsMap.size() == expected) {
                            assert CollectionsUtils.isMapComplete(resultsMap, expected);
                            callWithMap = true;
                        }
                    }
                    // so that it's out of the synchronized block.
                    if (callWithMap) {
                        callback.onSuccess(CollectionsUtils.toOrderedList(resultsMap));
                        return;
                    }

                }
            };
        }

    }

    public CallbackAggregator(final int expected, final ValueCallback<List<V>> callback) {
        super();

        this.expected = expected;
        this.callback = callback;

        this.exceptionReceived = new Value<Throwable>(null);
        this.callbacksDefined = new Value<Integer>(0);
        this.results = new ArrayList<V>(expected);
        this.resultsMap = new HashMap<Integer, V>();
        this.exception = null;

        if (expected == 0) {
            callback.onSuccess(new ArrayList<V>(0));
            return;
        }

    }
}
