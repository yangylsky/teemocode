package com.teemocode.module.search.factory;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teemocode.commons.util.ResourceHelper;

public class ESFactory {
	public static final String Property_Server = "server";

	public static final String Property_DateFormat = "dateFormat";

	public static String Server = "http://localhost:9200";

	public static String DateFormat = "yyyy-MM-dd HH:mm:ss";

	private static JestClient client;

	private ESFactory() {
	}

	public synchronized static JestClient getClient() {
		if(client == null) {
			Properties props = ResourceHelper.loadProperties("classpath:elasticsearch-config.properties");
			if(StringUtils.isNotBlank(props.getProperty(Property_Server))) {
				Server = props.getProperty(Property_Server);
			}
			if(StringUtils.isNotBlank(props.getProperty(Property_DateFormat))) {
				DateFormat = props.getProperty(Property_DateFormat);
			}
			Set<String> servers = new LinkedHashSet<String>();
			servers.add(Server);
			Gson gson = new GsonBuilder().setDateFormat(DateFormat).create();
			HttpClientConfig clientConfig = new HttpClientConfig.Builder(Server).multiThreaded(true).maxTotalConnection(500).gson(gson)
					.build();
			JestClientFactory factory = new JestClientFactory();
			factory.setHttpClientConfig(clientConfig);
			client = factory.getObject();
		}
		return client;
	}
}
