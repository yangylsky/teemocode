package tk.teemocode.module.search.handler;

import io.searchbox.client.JestResult;

public class CreateOrUpdateIndexHandler extends AbstractHandler {
	@Override
	public void completed(JestResult result) {
		logger.info("创建/更新索引成功 - " + result.getJsonString());
	}

	@Override
	public void failed(Exception e) {
		logger.error("创建/更新索引失败:\n" + e);
	}
}
