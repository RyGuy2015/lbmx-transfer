package com.dpslink.lbmxtransfer;

import java.awt.List;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.CommandCall;
import com.ibm.as400.access.ProgramCall;

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
	@Value( "${ftpRemoteInDirectory}" )
	private String ftpRemoteInDirectory;
	
	private FtpClient ftpClient;
	
	String[] inboundTypes = new String[] {"810/", "855/", "856/", "997/"};
	
	Collection<String> documents;
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryMonitor.class);

	
	public InboundMonitor() {
		
	}
	
	@Async
	public CompletableFuture<Void> importLbmxFiles() throws IOException {
		
	    try {
	    	while(true) {
				ftpClient = new FtpClient(ftpAddress, 21, username, password);
				
				ftpClient.open();
				
				for (String inboundType : inboundTypes) {
					documents = ftpClient.listFiles(ftpRemoteInDirectory + inboundType);
					documents.forEach(document -> {
						if (!document.contains("arc")) {
							try {
								ftpClient.downloadFile(ftpRemoteInDirectory + inboundType + document, ftpInDirectory + inboundType + document);				
								ftpClient.moveFile(ftpRemoteInDirectory + inboundType + document, ftpRemoteInDirectory + inboundType + "arc/" + document);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								LOGGER.error("An exception occurred!", new Exception("Custom exception"));
							}
						}
					});
					
				}
				
				//ftpClient.moveFile("/lbmx/in/997/997-1.txt", "/lbmx/in/997/arc/997-1.txt");
				
				callClProgram();
				
				ftpClient.close();
				Thread.sleep(100 * 1000);
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error("An exception occurred!", new Exception("Custom exception"));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			LOGGER.error("An exception occurred!", new Exception("Custom exception"));
		}
		
		return null;
	}
	
	public void callClProgram() {
		System.out.println("At least we hit the CL");
		String programName = "/QSYS.LIB/RYANI.LIB/TESTCL.PGM";
//		String programName = "CALL RYANI/TESTCL";

		AS400 system = new AS400("dps.dpslink.com", "ryani", "Xcode2016");
		
		ProgramCall clProgram = new ProgramCall(system);
//		CommandCall clProgram = new CommandCall(system);
		try {
			clProgram.setProgram(programName);
			boolean success = clProgram.run();
//			boolean success = clProgram.run(programName);
			System.out.println("Just called TESTCL.PGM");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}



}
