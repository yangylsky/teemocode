package com.teemocode.commons.component.async.disruptor;

public interface EventResultHandler {
	/**
	 * get a Event Result
	 * @return
	 */
	Object get();

	/**
	 * Blocking until get a Event Result
	 * @return
	 */
	Object getBlockedValue();

	void send(Object eventResult);

	void clear();
}
