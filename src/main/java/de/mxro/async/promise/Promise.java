package de.mxro.async.promise;

import de.mxro.fn.Closure;

public interface Promise<ResultType> extends Deferred<ResultType> {

	public ResultType get();
	
	public void catchExceptions(Closure<Throwable> closure);
	
	public void get(Closure<ResultType> closure);
	
	
}