package com.github.rmee.boot.utils.proxy;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties("proxy")
@EnableConfigurationProperties({ ProxyProperties.class })
public class ProxyConfiguration implements InitializingBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyConfiguration.class);

	@Autowired
	private ProxyProperties properties;

	@Override
	public void afterPropertiesSet() {
		if (properties.getHost() != null && properties.getPort() > 0) {
			LOGGER.debug("using proxy {}:{}", properties.getHost(), properties.getPort());

			Properties props = System.getProperties();
			props.put("http.proxyHost", properties.getHost());
			props.put("http.proxyPort", properties.getPort());
			props.put("https.proxyHost", properties.getHost());
			props.put("https.proxyPort", properties.getPort());
			if (properties.getExclude() != null) {
				LOGGER.debug("using no proxy for {}", properties.getExclude());
				props.put("no_proxy", properties.getExclude());
			}
		}
		else {
			ProxyDiscovery.readFromEnv();
		}
	}
}


