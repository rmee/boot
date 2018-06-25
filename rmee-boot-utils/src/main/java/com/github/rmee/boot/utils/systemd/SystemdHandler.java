package com.github.rmee.boot.utils.systemd;

import info.faljse.SDNotify.SDNotify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

public class SystemdHandler implements ApplicationListener<WebServerInitializedEvent> {


	@Override
	public void onApplicationEvent(final WebServerInitializedEvent event) {
		Logger logger = LoggerFactory.getLogger(SystemdHandler.class);
		logger.debug("notified systemd that application started");

		// notify systemd that start was successful
		SDNotify.sendNotify();
	}
}
