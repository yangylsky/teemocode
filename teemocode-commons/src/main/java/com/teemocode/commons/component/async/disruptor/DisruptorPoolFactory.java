package com.teemocode.commons.component.async.disruptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.dsl.Disruptor;

public class DisruptorPoolFactory {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public final static String module = DisruptorPoolFactory.class.getName();

	private static ConcurrentHashMap<String, DisruptorProcessor<?, ?>> topicProcessors = new ConcurrentHashMap<>();

	private ScheduledExecutorService scheduExecStatic = Executors.newScheduledThreadPool(1);

	public void start() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				stopDisruptor();
			}
		};
		scheduExecStatic.scheduleAtFixedRate(task, 60 * 60, 60 * 60, TimeUnit.SECONDS);
	}

	public void stop() {
		if(topicProcessors != null) {
			stopDisruptor();
			topicProcessors.clear();
			topicProcessors = null;
		}

		scheduExecStatic.shutdownNow();
	}

	private static void stopDisruptor() {
		Map<String, DisruptorProcessor<?, ?>> myDisruptors = new HashMap<>(topicProcessors);
		topicProcessors.clear();
		try {
			Thread.sleep(10000);// wait a while until all disruptor is done;
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		for(String topic : myDisruptors.keySet()) {
			DisruptorProcessor<?, ?> topicProcessor = myDisruptors.get(topic);
			try {
				topicProcessor.halt();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		myDisruptors.clear();
	}

	@SuppressWarnings("unchecked")
	static <T> DisruptorProcessor<?, T> getProcessor(String topic) {
		return (DisruptorProcessor<?, T>) topicProcessors.get(topic);
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	DisruptorProcessor<?, ?> getProcessor(DisruptorFactory disruptorFactory, String topic, int ringBufferSize,
			Class<? extends DisruptorEvent<?>> eventClass) {
		DisruptorProcessor<?, ?> topicProcessor = topicProcessors.get(topic);
		if(topicProcessor == null) {
			Disruptor<?> disruptor = disruptorFactory.createSingleDw(topic, ringBufferSize, eventClass);
			if(disruptor == null) {
				logger.error("[Disruptor]create disruptor for '" + topic + "' failed.", module);
				return null;
			}
			topicProcessor = new DisruptorProcessor(topic, disruptor);
			DisruptorProcessor<?, ?> topicProcessorOld = topicProcessors.putIfAbsent(topic, topicProcessor);
			if(topicProcessorOld != null) {
				topicProcessor = topicProcessorOld;
				logger.info("[Disruptor]create disruptor for '" + topic + "' successfully.", module);
			}
		}
		return topicProcessor;
	}
}
