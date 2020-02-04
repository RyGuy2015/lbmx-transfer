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
import org.springframework.stereotype.Service;

@Service
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
	
	private FtpClient ftpClient;
	
	public InboundMonitor() {
		
	}
	
	@Async
	public CompletableFuture<Void> printTheThing() throws IOException {
		
	    try {
	    	while(true) {
				ftpClient = new FtpClient(ftpAddress, 21, username, password);
				
				ftpClient.open();
				
				Collection<String> documents = ftpClient.listFiles("/lbmx/in/997/");
				
	//        ftpClient.putFileToPath(file, ftpRemoteDirectory + lbmxFile.getPoDateFileName());
				
				ftpClient.close();
				
				documents.forEach(document -> {
					System.out.println(document);
				});
				Thread.sleep(30 * 1000);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//	    try {
//	        while (true) {
//	            System.out.println("Inside Inbound Monitor");
//	            Thread.sleep(5 * 1000);
//	        }
//	    } catch (InterruptedException e) {
//	        e.printStackTrace();
//	    }
		return null;
	}



}
