package com.dpslink.lbmxtransfer;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ObjectAlreadyExistsException;


@Component
public class ThreadHandler {
	@Autowired
	DirectoryMonitor directoryMonitor;
	
	@Autowired
	InboundMonitor inboundMonitor;
	
	@Autowired 
	IbmCLHandler ibmCLHandler;
	
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryMonitor.class);
    
	public ThreadHandler() {
		
	}
	
	@PostConstruct
	public void startMonitors() {
			try {
				directoryMonitor.monitorDirectory();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LOGGER.error("An exception occurred!", new Exception("Custom exception"));
			}
			try {
				inboundMonitor.importLbmxFiles();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error("An exception occurred!", new Exception("Custom exception"));
			}
	}
}
