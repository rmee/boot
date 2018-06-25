package com.github.rmee.boot.utils.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;

public class EndpointInfoLogger implements ApplicationListener<WebServerInitializedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EndpointInfoLogger.class);

	@Value("${server.ssl.enabled}")
	private boolean sslEnabled;

	@Value("${server.address:0.0.0.0}")
	private String address;

	@Override
	public void onApplicationEvent(final WebServerInitializedEvent event) {
		WebServer webServer = event.getWebServer();
		int port = webServer.getPort();
		String protocol = sslEnabled ? "https" : "http";
		LOGGER.info("service endpoint running at {}://{}:{}", protocol, address, port);
	}
}
