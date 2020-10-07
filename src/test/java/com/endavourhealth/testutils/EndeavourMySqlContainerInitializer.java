package com.endavourhealth.testutils;

import java.io.IOException;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class EndeavourMySqlContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		
	EndeavourMySqlContainer mysqlContainer;

	public EndeavourMySqlContainerInitializer(EndeavourMySqlContainer mysqlContainer) {
		this.mysqlContainer = mysqlContainer;
	}

	public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
		mysqlContainer.start();
		
		//System.out.println("--> ...MySql started " + mysqlContainer.getContainerInfo());
		//System.out.println("--> ...MySql logs " + mysqlContainer.getLogs());
		
		try {
			if(mysqlContainer.execInitShellScript()) {
				TestPropertyValues
				.of("spring.datasource.url=" + mysqlContainer.getJdbcUrl(),
						"spring.datasource.username=" + mysqlContainer.getUsername(),
						"spring.datasource.password=" + mysqlContainer.getPassword())
				.applyTo(configurableApplicationContext.getEnvironment());			
			}
		} catch (UnsupportedOperationException | IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		//System.out.println("--> ...MySql initialised");
	}
}
