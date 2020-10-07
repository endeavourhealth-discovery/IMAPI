package com.endavourhealth.testutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MySQLContainer;

@SuppressWarnings("rawtypes")
public class EndeavourMySqlContainer extends MySQLContainer {

	private static final String IMAGE_VERSION = "mysql:5.7.31";
	public static final String DEFAULT_PASSWORD = "password";

	private static final int SUCCESS_EXIT_CODE = 0;

	private static final String INIT_SCRIPT = "/initDb.sh";
	private String[] initSqlScripts;

	public EndeavourMySqlContainer() {
		super(IMAGE_VERSION);
	
		withPassword(DEFAULT_PASSWORD);
		this.withClasspathResourceMapping(INIT_SCRIPT, INIT_SCRIPT, BindMode.READ_ONLY);
	}

	public EndeavourMySqlContainer withInitSqlScripts(String ... initSqlScripts) {
		this.initSqlScripts = initSqlScripts;
		
		for(String initSqlScript : initSqlScripts) {
			this.withClasspathResourceMapping(initSqlScript, initSqlScript, BindMode.READ_ONLY);
		}
		//return (EndeavourMySqlContainer) this.withClasspathResourceMapping(initSql, "/initDb.sql", BindMode.READ_ONLY);
		
		return this;
	}

	boolean execInitShellScript() throws UnsupportedOperationException, IOException, InterruptedException {
		boolean success = false;
		
		List<String> command = new ArrayList<String>();
		command.add(INIT_SCRIPT);
		command.addAll(Arrays.asList(initSqlScripts));
		
		ExecResult res = execInContainer(command.toArray(new String[] {}));

		success = SUCCESS_EXIT_CODE == res.getExitCode();
		
		if(success == false) {
			throw new IOException(String.format("Unable to load test data into database. Exit code: %n Stdout: %s Stderr: %s" + res.getExitCode(), res.getStdout(), res.getStderr()));
		}
		
		return success;
	}
}
