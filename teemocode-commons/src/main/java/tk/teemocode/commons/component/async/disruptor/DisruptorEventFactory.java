package tk.teemocode.commons.component.async.disruptor;

import tk.teemocode.commons.exception.ReflectException;
import com.lmax.disruptor.EventFactory;

public class DisruptorEventFactory<E extends DisruptorEvent<?>> implements EventFactory<E> {
	private Class<E> eventClass;

	public DisruptorEventFactory(Class<E> eventClass) {
		this.eventClass = eventClass;
	}

	@Override
	public E newInstance() {
		try {
			return eventClass.newInstance();
		} catch(InstantiationException e) {
			throw new ReflectException(e);
		} catch(IllegalAccessException e) {
			throw new ReflectException(e);
		}
	}
}
