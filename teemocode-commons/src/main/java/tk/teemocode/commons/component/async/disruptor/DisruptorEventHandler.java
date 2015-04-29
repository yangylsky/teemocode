package tk.teemocode.commons.component.async.disruptor;

import com.lmax.disruptor.EventHandler;

public interface DisruptorEventHandler<T> extends EventHandler<T>, Comparable<DisruptorEventHandler<T>> {
	public Class<T> getEventClass();
}
