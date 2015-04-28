package com.teemocode.module.search.handler;

import io.searchbox.client.JestResult;

public class DeleteIndexHandler extends AbstractHandler {
	@Override
	public void completed(JestResult result) {
		logger.info("删除索引成功 - " + result.getJsonString());
	}

	@Override
	public void failed(Exception e) {
		logger.error("删除索引失败:\n" + e);
	}
}
