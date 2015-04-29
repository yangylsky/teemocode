package tk.teemocode.commons.component.async.disruptor.demo;

import com.lmax.disruptor.EventFactory;

public class ValueEvent {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public final static EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {
		@Override
		public ValueEvent newInstance() {
			return new ValueEvent();
		}
	};
}
