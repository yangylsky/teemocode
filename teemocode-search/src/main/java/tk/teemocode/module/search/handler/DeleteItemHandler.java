package tk.teemocode.module.search.handler;

import io.searchbox.client.JestResult;

public class DeleteItemHandler extends AbstractHandler {
	@Override
	public void completed(JestResult result) {
		logger.info("删除索引项成功 - " + result.getJsonString());
	}

	@Override
	public void failed(Exception e) {
		logger.error("删除索引项失败:\n" + e);
	}
}
