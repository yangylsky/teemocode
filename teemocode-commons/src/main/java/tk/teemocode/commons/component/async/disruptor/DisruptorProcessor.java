package tk.teemocode.commons.component.async.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorProcessor<E extends DisruptorEvent<T>, T> {
	private String topic;

	private Disruptor<E> disruptor;

	private long waitAtSequence = 0;

	public DisruptorProcessor(String topic, Disruptor<E> disruptor) {
		this.topic = topic;
		this.disruptor = disruptor;
	}

	public String getTopic() {
		return topic;
	}

	public Disruptor<E> getDisruptor() {
		return disruptor;
	}

	public long send(T eventObj) {
		RingBuffer<E> ringBuffer = disruptor.getRingBuffer();
		waitAtSequence = ringBuffer.next();
		E event = ringBuffer.get(waitAtSequence);
		event.setEventObj(eventObj);
		ringBuffer.publish(waitAtSequence);
		return waitAtSequence;
	}

	public void shutdown() {
		disruptor.shutdown();
	}

	public void halt() {
		disruptor.halt();
	}
}
