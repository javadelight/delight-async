package delight.async;

import delight.async.callbacks.ValueCallback;
import delight.functional.Closure;

/**
 * The definition of an asynchronous operation with no parameters.
 *
 * @param <Result>
 */
public interface Operation<Result> extends Closure<ValueCallback<Result>> {

    @Override
    public void apply(ValueCallback<Result> callback);

}
