package com.teemocode.commons.component.async.disruptor.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

public class SimpleDisruptorTest {
	private static final int BUFFER_SIZE = 1024 * 16;

	private static final int EVENT_NUMBER = 10000000;

	@Test(enabled = true)
	@SuppressWarnings("unchecked")
	public void normalTest() {
		ExecutorService exec = Executors.newCachedThreadPool();
		// Preallocate RingBuffer with 1024 ValueEvents
		Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(ValueEvent.EVENT_FACTORY, BUFFER_SIZE, exec);
		final EventHandler<ValueEvent> handler = new EventHandler<ValueEvent>() {
			// event will eventually be recycled by the Disruptor after it wraps
			@Override
			public void onEvent(final ValueEvent event, final long sequence, final boolean endOfBatch) throws Exception {
				if(sequence % 1000000 == 0) {
					System.out.println("Sequence: " + sequence + "	ValueEvent: " + event.getValue());
				}
			}
		};

		disruptor.handleEventsWith(handler);
		RingBuffer<ValueEvent> ringBuffer = disruptor.start();
		long startTime = System.currentTimeMillis();
		for(long i = 10; i < 10 + EVENT_NUMBER; i++) {
			String uuid = String.valueOf(i);
			long seq = ringBuffer.next();
			ValueEvent valueEvent = ringBuffer.get(seq);
			valueEvent.setValue(uuid);
			ringBuffer.publish(seq);
		}
		System.out.println("Trigger events: " + EVENT_NUMBER + ", process time: " + (System.currentTimeMillis() - startTime)
				+ "ms");
		disruptor.shutdown();
		exec.shutdown();
	}
}
