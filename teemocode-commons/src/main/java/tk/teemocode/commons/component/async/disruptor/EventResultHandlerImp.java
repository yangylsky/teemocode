package tk.teemocode.commons.component.async.disruptor;

import java.util.concurrent.TimeUnit;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;

public class EventResultHandlerImp implements EventResultHandler {
	protected EventResultProcessor valueEventProcessor;

	@SuppressWarnings({"unchecked", "rawtypes"})
	public EventResultHandlerImp(int timeoutforeturnResult) {
		super();
		RingBuffer ringBuffer = RingBuffer.createSingleProducer(new EventResultFactory(), 1, new TimeoutBlockingWaitStrategy(
				timeoutforeturnResult, TimeUnit.MILLISECONDS));
		valueEventProcessor = new EventResultProcessor(ringBuffer);
	}

	/**
	 * send event result
	 */
	@Override
	public void send(Object result) {
		valueEventProcessor.send(result);
	}

	@Override
	public Object get() {
		Object result = null;
		EventResult ve = valueEventProcessor.waitFor();
		if(ve != null) {
			result = ve.getValue();
			ve.clear();
			// clear();
		}
		return result;
	}

	@Override
	public Object getBlockedValue() {
		Object result = null;
		EventResult ve = valueEventProcessor.waitForBlocking();
		if(ve != null) {
			result = ve.getValue();
			ve.clear();
			// clear();
		}
		return result;
	}

	@Override
	public void clear() {
		valueEventProcessor.clear();
	}
}
