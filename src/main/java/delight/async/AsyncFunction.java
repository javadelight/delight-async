package delight.async;

import delight.async.callbacks.ValueCallback;
import delight.functional.Closure2;

/**
 * A base template for an asynchronous function.
 * 
 * @author Max Rohde
 *
 * @param <Input>
 * @param <Output>
 */
public interface AsyncFunction<Input, Output> extends Closure2<Input, ValueCallback<Output>> {

    @Override
    public void apply(Input input, ValueCallback<Output> callback);

}
