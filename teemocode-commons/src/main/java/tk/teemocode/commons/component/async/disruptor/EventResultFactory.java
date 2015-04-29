package tk.teemocode.commons.component.async.disruptor;

import com.lmax.disruptor.EventFactory;


@SuppressWarnings("rawtypes")
public class EventResultFactory implements EventFactory {
	@Override
	public EventResult newInstance() {
		return new EventResult();
	}
}
