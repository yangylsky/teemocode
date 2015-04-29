package tk.teemocode.module.index.async.handler;

import org.springframework.stereotype.Component;

import tk.teemocode.module.index.async.event.CreateIndexItemEvent;
import tk.teemocode.module.search.Indexable;

@Component
public class CreateIndexItemHandler extends IndexHandler<CreateIndexItemEvent> {
	@Override
	public Class<CreateIndexItemEvent> getEventClass() {
		return CreateIndexItemEvent.class;
	}

	@Override
	public void onEvent(CreateIndexItemEvent event, long sequence, boolean endOfBatch) {
		Indexable item = event.getEventObj();
		try {
			logger.debug("开始创建/更新索引项[" + item.getTypeName() + ":" + item.getId() + "].");
			indexService.createItem(item);
			logger.debug("索引项创建/更新成功[" + item.getTypeName() + ":" + item.getId() + "].");
		} catch(Exception e) {
			logger.error("Create or update item failed[source:" + item + "]", e);
//			throw new ProjectException(e);
		}
	}
}
