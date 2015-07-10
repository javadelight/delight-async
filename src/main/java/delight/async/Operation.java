package delight.async;

import delight.async.callbacks.ValueCallback;

/**
 * The definition of an asynchronous operation with no parameters.
 *
 * @param <Result>
 */
public interface Operation<Result> {

    public void apply(ValueCallback<Result> callback);

}
