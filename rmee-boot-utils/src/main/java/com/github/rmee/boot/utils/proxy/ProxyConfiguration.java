package com.github.rmee.boot.utils.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

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
	public void afterPropertiesSet() throws Exception {
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
		else if (getEnv("HTTP_PROXY") != null || getEnv("HTTPS_PROXY") != null) {
			setupProxy("http");
			setupProxy("https");
		}
		else {
			LOGGER.debug("no proxy configured");
		}

		String no_proxy = getEnv("NO_PROXY");
		if (no_proxy != null) {
			// see https://docs.oracle.com/javase/7/docs/api/java/net/doc-files/net-properties.html
			String javaNoProxy = Arrays.asList(no_proxy.split("\\,")).stream().map(it -> it.startsWith(".") ? "*" + it : it).collect(Collectors.joining("|"));
			System.setProperty("http.nonProxyHosts", javaNoProxy);
			System.setProperty("https.nonProxyHosts", javaNoProxy);
		}
	}

	private static void setupProxy(String name) {
		String envName = name.toUpperCase() + "_PROXY";
		String value = getEnv(envName);
		if (value != null) {
			URL url;
			try {
				url = new URL(value);
			}
			catch (MalformedURLException e) {
				throw new IllegalStateException("invalid proxy url " + value + " in env " + envName);
			}

			System.setProperty(name + ".proxyHost", url.getHost());
			System.setProperty(name + ".proxyPort", Integer.toString(url.getPort()));
		}
	}

	private static String getEnv(String envName) {
		String value = System.getenv(envName.toUpperCase());
		if (value == null) {
			value = System.getenv(envName.toLowerCase());
		}
		return value;
	}
}


