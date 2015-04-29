package tk.teemocode.module.index.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tk.teemocode.commons.component.async.disruptor.DisruptorFactory;
import tk.teemocode.commons.component.async.disruptor.DisruptorProcessor;
import tk.teemocode.module.search.Indexable;

public abstract class IndexActionInterceptor {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected DisruptorProcessor<?, Indexable> getCreateIndexItemProcessor() {
		return DisruptorFactory.getProcessor("createIndexItem");
	}

	protected DisruptorProcessor<?, Indexable> getDeleteIndexItemProcessor() {
		return DisruptorFactory.getProcessor("deleteIndexItem");
	}
}
