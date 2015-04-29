package tk.teemocode.module.index.async.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.commons.component.async.disruptor.DisruptorEvent;
import tk.teemocode.commons.component.async.disruptor.DisruptorEventHandler;
import tk.teemocode.module.search.Indexable;
import tk.teemocode.module.search.service.ESIndexService;

public abstract class IndexHandler<E extends DisruptorEvent<Indexable>> implements DisruptorEventHandler<E> {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Resource
	protected ESIndexService indexService;

	@Override
	public int compareTo(DisruptorEventHandler<E> o) {
		return o == null ? 1 : hashCode() - o.hashCode();
	}
}
