package delight.async.helper;

import delight.async.callbacks.ValueCallback;

public interface Aggregator<V> {

    public ValueCallback<V> createCallback();

}
