package delight.async.callbacks;

public interface ValueCallback<T> extends FailureCallback {
	
	/**
	 * Asynchronous operation successfully completed.
	 * @param value
	 */
	public void onSuccess(T value);
	
}
