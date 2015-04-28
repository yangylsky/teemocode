package com.teemocode.commons.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class XmlParser {
	private Logger logger = LoggerFactory.getLogger(getClass());

	public Map<String, Object> load(String configFileName) {
		return load(new File(configFileName));
	}

	public Map<String, Object> load(File file) {
		Map<String, Object> mps = new LinkedHashMap<String, Object>();
		try {
			if(!file.exists()) {
				return mps;
			}

			Document doc = readDocument(file);
			if(doc == null) {
				return mps;
			}
			Element root = doc.getRootElement();
			parse(root, mps);

		} catch(Exception ex) {
			logger.error("", ex);
		}
		return mps;
	}

	private Document readDocument(File file) {
		logger.debug("locate configure file  :" + file.getName());
		Document doc = null;
		try {
			SAXReader reader = new SAXReader();
			// builder.setEntityResolver(new DTDEntityResolver());
			InputStream in = new FileInputStream(file);
			doc = reader.read(in);
			logger.debug("got mapping file.");
		} catch(Exception e) {
			logger.error("Exception error: " + e);
		}
		return doc;
	}

	public abstract void parse(Element root, Map<String, Object> results) throws Exception;
}
