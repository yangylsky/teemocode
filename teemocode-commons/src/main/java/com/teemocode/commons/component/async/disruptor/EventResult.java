package com.teemocode.commons.component.async.disruptor;

/**
 * A Consumer send response back to the Subscriber by this value object
 */
public class EventResult {
	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(final Object value) {
		this.value = value;
	}

	public void clear() {
		value = null;
	}
}
