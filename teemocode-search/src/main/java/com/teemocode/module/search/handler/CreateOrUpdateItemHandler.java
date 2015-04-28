package com.teemocode.module.search.handler;

import io.searchbox.client.JestResult;

public class CreateOrUpdateItemHandler extends AbstractHandler {
	@Override
	public void completed(JestResult result) {
		logger.info("创建/更新索引项成功 - " + result.getJsonString());
	}

	@Override
	public void failed(Exception e) {
		logger.error("创建/更新索引项失败:\n" + e);
	}
}
