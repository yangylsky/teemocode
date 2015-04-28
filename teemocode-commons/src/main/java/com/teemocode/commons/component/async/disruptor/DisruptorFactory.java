package com.teemocode.commons.component.async.disruptor;

import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teemocode.commons.component.async.Startable;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DisruptorFactory implements Startable {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public final static String module = DisruptorFactory.class.getName();

	/**
	 * 格式 - &lttopic, events&gt
	 */
	private static final ConcurrentHashMap<String, TreeSet<? extends EventHandler>> handlesMap = new ConcurrentHashMap<>();

	private static DisruptorPoolFactory disruptorPoolFactory = new DisruptorPoolFactory();

	<E extends DisruptorEvent<?>> Disruptor<E> createSingleDw(String topic, int ringBufferSize, Class<E> eventClass) {
		WaitStrategy waitStrategy = new BlockingWaitStrategy();
		return new Disruptor<>(new DisruptorEventFactory<E>(eventClass), ringBufferSize, Executors.newCachedThreadPool(),
				ProducerType.SINGLE, waitStrategy);
	}

	/**
	 * one topic one DisruptorProcessor
	 */
	public DisruptorProcessor<?, ?> createProcessor(String topic, TreeSet<DisruptorEventHandler> handlers) {
		return createProcessor(topic, 1024, handlers);
	}

	/**
	 * one topic one DisruptorProcessor
	 */
	public DisruptorProcessor<?, ?> createProcessor(String topic, int ringBufferSize, TreeSet<? extends DisruptorEventHandler> handlers) {
		if(CollectionUtils.isEmpty(handlers)) {
			throw new IllegalArgumentException("[Disruptor]" + topic + "'s handlers can not be a null");
		}
		Class<? extends DisruptorEvent<?>> eventClass = findEventClass(handlers);
		DisruptorProcessor<?, ?> processor = getProcessor(topic, ringBufferSize, eventClass);
		Disruptor<?> disruptor = addEventHandler(processor.getDisruptor(), handlers);
		if(disruptor == null) {
			return null;
		}
		disruptor.start();
		return processor;
	}

	private Class<? extends DisruptorEvent<?>> findEventClass(TreeSet<? extends DisruptorEventHandler> handlers) {
		return handlers.iterator().next().getEventClass();
	}

	private DisruptorProcessor<?, ?> getProcessor(String topic, int ringBufferSize, Class<? extends DisruptorEvent<?>> eventClass) {
		return disruptorPoolFactory.getProcessor(this, topic, ringBufferSize, eventClass);
	}

	private <T, EH extends EventHandler> Disruptor<T> addEventHandler(Disruptor<T> disruptor, TreeSet<EH> handlers) {
		if(CollectionUtils.isEmpty(handlers)) {
			return null;
		}
		for(EH handler : handlers) {
			disruptor.handleEventsWith(handler);
		}
		return disruptor;
	}

	public TreeSet<? extends EventHandler> getHandles(String topic) {
		TreeSet<? extends EventHandler> handlers = handlesMap.get(topic);
		if(CollectionUtils.isEmpty(handlers)) {
			logger.error("[Disruptor]not found any handle in topic(" + topic + ") ", module);
			return null;
		}
		return handlers;
	}

	public static <T> DisruptorProcessor<?, T> getProcessor(String topic) {
		return DisruptorPoolFactory.getProcessor(topic);
	}

	public void releaseDisruptor(Object owner) {
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		handlesMap.clear();
		disruptorPoolFactory.stop();
	}
}
