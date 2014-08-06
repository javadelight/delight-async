package de.mxro.async.internal;

import de.mxro.async.Promise;
import de.mxro.async.callbacks.ValueCallback;
import de.mxro.fn.Closure;

public class PromiseImpl<ResultType> implements Promise<ResultType> {

	private final Operation<ResultType>
	
	private ResultType resultCache;
	
	
	private final void requestResult(ValueCallback<ResultType> callback) {
		synchronized (resultCache) {
			if (resultCache != null) {
				callback.onSuccess(resultCache);
				return;
			}
		}
		
		
		
		
	}
	
	

	
	
}
