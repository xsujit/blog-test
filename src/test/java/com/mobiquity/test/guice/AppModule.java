package com.mobiquity.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppModule extends AbstractModule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void configure() {
        super.configure();
        try {
            Properties props = new Properties();
            props.load(getPropertiesFile());
            Names.bindProperties(binder(), props);
        } catch (IOException e) {
            logger.error("Error during binding properties", e);
        }
    }

    private InputStream getPropertiesFile() {
        String environment = System.getProperty("environment", "uat.properties");
        logger.info("Test environment :: {}", environment);
        return AppModule.class
                .getClassLoader()
                .getResourceAsStream(environment);
    }
}
