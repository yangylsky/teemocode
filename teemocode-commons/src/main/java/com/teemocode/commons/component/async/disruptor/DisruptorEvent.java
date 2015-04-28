package com.teemocode.commons.component.async.disruptor;

public abstract class DisruptorEvent<T> {
	private String topic;

	private T eventObj;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public T getEventObj() {
		return eventObj;
	}

	public void setEventObj(T eventObj) {
		this.eventObj = eventObj;
	}
}
