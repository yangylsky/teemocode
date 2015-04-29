package tk.teemocode.module.index.async.handler;

import org.springframework.stereotype.Component;

import tk.teemocode.module.index.async.event.DeleteIndexItemEvent;
import tk.teemocode.module.search.Indexable;

@Component
public class DeleteIndexItemHandler extends IndexHandler<DeleteIndexItemEvent> {
	@Override
	public Class<DeleteIndexItemEvent> getEventClass() {
		return DeleteIndexItemEvent.class;
	}

	@Override
	public void onEvent(DeleteIndexItemEvent event, long sequence, boolean endOfBatch) {
		Indexable item = event.getEventObj();
		try {
			indexService.deleteItem(item.getIndexName(), item.getTypeName(), item.getId());
		} catch(Exception e) {
			logger.error("Create or update item failed[source:" + item + "]", e);
//			throw new ProjectException(e);
		}
	}
}
