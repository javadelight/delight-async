package delight.async;

import delight.async.callbacks.ValueCallback;

/**
 * The definition of an asynchronous operation with no parameters.
 *
 * @param <ResultType>
 */
public interface Operation<ResultType> {

    public void apply(ValueCallback<ResultType> callback);

}
