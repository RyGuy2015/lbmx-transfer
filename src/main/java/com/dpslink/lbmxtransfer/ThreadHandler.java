package com.dpslink.lbmxtransfer;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ObjectAlreadyExistsException;


@Service
public class ThreadHandler {
	@Autowired
	DirectoryMonitor directoryMonitor;
	
	@Autowired
	InboundMonitor inboundMonitor;
	
	
	public ThreadHandler() {
		
	}
	
	@PostConstruct
	public void startMonitors() {
			try {
				directoryMonitor.monitorDirectory();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				inboundMonitor.printTheThing();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
}
