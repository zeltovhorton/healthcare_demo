package com.hortonworks.se.healthcare.storm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public abstract class BaseEventTopology {

	
	private static final Logger LOG = Logger.getLogger(BaseEventTopology.class);

	protected Properties topologyConfig;
	
	public BaseEventTopology(String configFileLocation) throws Exception {
		
		topologyConfig = new Properties();
		try {
	    	System.out.println("configfile=" + configFileLocation);

			//topologyConfig.load(ClassLoader.getSystemResourceAsStream(configFileLocation));
			FileInputStream in = new FileInputStream(configFileLocation);
			topologyConfig.load(in);
			
		} catch (FileNotFoundException e) {
			LOG.error("Encountered error while reading configuration properties: "
					+ e.getMessage());
			throw e;
		} catch (IOException e) {
			LOG.error("Encountered error while reading configuration properties: "
					+ e.getMessage());
			throw e;
		}			
	}
}
