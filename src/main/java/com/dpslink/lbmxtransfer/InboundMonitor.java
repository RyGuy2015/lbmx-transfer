package com.dpslink.lbmxtransfer;

import java.awt.List;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class InboundMonitor {
	
	@Value( "${ftpRemoteDirectory}" )
	private String ftpRemoteDirectory;
	@Value( "${ftpOutDirectory}" )
	private String ftpOutDirectory;
	@Value( "${ftpSentDirectory}" )
	private String ftpSentDirectory;
	@Value( "${username}" )
	private String username;
	@Value( "${password}" )
	private String password;
	@Value( "${ftpAddress}" ) 
	private String ftpAddress;
	@Value( "${ftpInDirectory}" )
	private String ftpInDirectory;
	
	private FtpClient ftpClient;
	
	String[] inboundTypes = new String[] {"810/", "855/", "856/", "997/"};
	
	Collection<String> documents;
	
	public InboundMonitor() {
		
	}
	
	@Async
	public CompletableFuture<Void> importLbmxFiles() throws IOException {
		
	    try {
	    	while(true) {
				ftpClient = new FtpClient(ftpAddress, 21, username, password);
				
				ftpClient.open();
				
				for (String inboundType : inboundTypes) {
					System.out.println("Type is: " + inboundType);
					documents = ftpClient.listFiles("/lbmx/in/" + inboundType);
					documents.forEach(document -> {
						if (!document.contains("arc")) {
							try {
								ftpClient.downloadFile("/lbmx/in/" + inboundType + document, ftpInDirectory + document);
								System.out.println(document);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					
				}
				
				ftpClient.close();
				Thread.sleep(60 * 1000);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}



}
