package it.attocchi.web.config;

import it.attocchi.utils.PropertiesUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class SoftwareProperties {

	protected static final Logger logger = Logger.getLogger(SoftwareProperties.class.getName());

	public static final String WEB_INF_CONFIG_PROPERTIES = "/WEB-INF/software.properties";
	// public static final String PU_NAME = "AtreeFLOW-WS";
	public static final String PU_NAME = "DEFAULT_PU";

	public static final String PROPERTY_connString = "connString";
	public static final String PROPERTY_driverClass = "driverClass";
	public static final String PROPERTY_userName = "userName";
	public static final String PROPERTY_password = "password";

	private static Properties properties;

	public static Properties getProperties() {
		return properties;
	}

	public static void setProperties(Properties properties) {
		SoftwareProperties.properties = properties;
	}

	private static Map<String, String> jpaDbProps;

	public static Map<String, String> getJpaDbProps() {
		return jpaDbProps;
	}

	public static void setJpaDbProps(Map<String, String> dbProps) {
		SoftwareProperties.jpaDbProps = dbProps;
	}

	/**
	 * Inizializza i Valori della BL e della PersistenceUnit
	 */
	public static void init(ServletContext servletContext) {

		if (jpaDbProps == null) {	

			InputStream fileProperties = servletContext.getResourceAsStream(WEB_INF_CONFIG_PROPERTIES);
			if (fileProperties != null) {
				
				logger.warn("Loading " + WEB_INF_CONFIG_PROPERTIES);
				properties = PropertiesUtils.getProperties(fileProperties);

				if (properties != null) {

					// RapportoServerBL.connString =
					// properties.getProperty("connString");
					// RapportoServerBL.driverClass =
					// properties.getProperty("driverClass");
					// RapportoServerBL.userName =
					// properties.getProperty("userName");
					// RapportoServerBL.password =
					// properties.getProperty("password");

					connString = StringUtils.trim(properties.getProperty(PROPERTY_connString));
					driverClass = StringUtils.trim(properties.getProperty(PROPERTY_driverClass));
					userName = StringUtils.trim(properties.getProperty(PROPERTY_userName));
					password = StringUtils.trim(properties.getProperty(PROPERTY_password));

					jpaDbProps = new HashMap<String, String>();

					jpaDbProps.put("javax.persistence.jdbc.url", connString);
					jpaDbProps.put("javax.persistence.jdbc.driver", driverClass);
					jpaDbProps.put("javax.persistence.jdbc.user", userName);
					jpaDbProps.put("javax.persistence.jdbc.password", password);

					/*
					 * Se uno dei Parametri da File e' nullo annullo tutti gli
					 * oggetti
					 */
					if (StringUtils.isEmpty(connString) || StringUtils.isEmpty(driverClass)) {
						properties = null;
						jpaDbProps = null;

						logger.warn("SoftwareConfig loaded but Empty Database Configuration");
					}
				}
			}
		} else {
			logger.warn("SoftwareConfig already inizialized");
		}
	}

	public static String connString;
	public static String driverClass;
	public static String userName;
	public static String password;
}
