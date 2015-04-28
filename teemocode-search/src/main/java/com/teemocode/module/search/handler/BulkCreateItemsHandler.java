package com.teemocode.module.search.handler;

import io.searchbox.client.JestResult;

public class BulkCreateItemsHandler extends AbstractHandler {
	@Override
	public void completed(JestResult result) {
		int num = result.getJsonObject().get("items").getAsJsonArray().size();
		long took = result.getJsonObject().get("took").getAsLong();
		logger.info("批量创建/更新索引项成功[总数:" + num + ", 耗时:" + took + "ms]");
	}

	@Override
	public void failed(Exception e) {
		logger.error("批量创建/更新索引项失败:\n" + e);
	}
}
