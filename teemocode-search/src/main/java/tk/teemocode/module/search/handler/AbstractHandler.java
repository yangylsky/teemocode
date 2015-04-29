package tk.teemocode.module.search.handler;

import io.searchbox.client.JestResult;
import io.searchbox.client.JestResultHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandler implements JestResultHandler<JestResult> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
}
