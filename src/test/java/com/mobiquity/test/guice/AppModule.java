package com.mobiquity.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        super.configure();
        try {
            Properties props = new Properties();
            props.load(getPropertiesFile());
            Names.bindProperties(binder(), props);
        } catch (IOException e) {
            log.error("Error during binding properties", e);
        }
    }

    private InputStream getPropertiesFile() {
        String environment = System.getProperty("environment", "uat.properties");
        log.info("Using properties file :: {}", environment);
        return AppModule.class
                .getClassLoader()
                .getResourceAsStream(environment);
    }
}
