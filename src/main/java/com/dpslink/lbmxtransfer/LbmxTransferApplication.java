package com.dpslink.lbmxtransfer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.ObjectAlreadyExistsException;

@SpringBootApplication
public class LbmxTransferApplication {
	
	@Value("${ftpOutDirectory}")
	static String ftpOutDirectory;

	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException, AS400SecurityException, ObjectAlreadyExistsException {
		SpringApplication.run(LbmxTransferApplication.class, args);
		
		DirectoryMonitor directoryMonitor = new DirectoryMonitor();
		directoryMonitor.monitorDirectory();
	}
			
}
