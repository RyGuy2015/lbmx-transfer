package com.dpslink.lbmxtransfer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400SecurityException;
import com.ibm.as400.access.IFSFile;
import com.ibm.as400.access.ObjectAlreadyExistsException;

@Service	
@ConfigurationProperties
public class DirectoryMonitor {
	
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
	
	LbmxFile lbmxFile = new LbmxFile();
	
	AS400 system = new AS400("dps.dpslink.com", "ryani", "Xcode2016");

	public DirectoryMonitor() {
		super();
	} 
	
	private FtpClient ftpClient;

	
	@PostConstruct
	public void monitorDirectory() throws IOException, InterruptedException, URISyntaxException, AS400SecurityException, ObjectAlreadyExistsException {
		WatchKey key;		
		WatchService watchService = FileSystems.getDefault().newWatchService();
	    Path path = Paths.get(ftpOutDirectory);
	    
	    ftpClient = new FtpClient(ftpAddress, 21, username, password);
		path.register(watchService,StandardWatchEventKinds.ENTRY_CREATE);

		
		while((key=watchService.take())!=null)
		{
			
			for (WatchEvent<?> event : key.pollEvents()) {
				
				ftpClient.open();
				
				lbmxFile.setOriginalFileName(event.context().toString());
				lbmxFile = parseLbmxFile(lbmxFile);//				System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
				File file = new File(ftpOutDirectory + event.context());
		        ftpClient.putFileToPath(file, ftpRemoteDirectory + lbmxFile.getPoDateFileName());
		        
	            Collection<String> files = ftpClient.listFiles(ftpRemoteDirectory);
	            if (files.contains(lbmxFile.getPoDateFileName())) {
	            	moveFile(ftpOutDirectory + lbmxFile.getOriginalFileName(), ftpSentDirectory + lbmxFile.getOriginalFileName());
	            }
	            
				key.reset();
				ftpClient.close();
		        
			}
		}

		watchService.close();
	}
	
//	@PostConstruct
//	public void monitorDirectory() throws IOException, InterruptedException, URISyntaxException {
//		WatchKey key;		
//		WatchService watchService = FileSystems.getDefault().newWatchService();
//	    Path path = Paths.get(ftpOutDirectory);
//	    
//	    ftpClient = new FtpClient(ftpAddress, 21, username, password);
//		path.register(watchService,StandardWatchEventKinds.ENTRY_CREATE);
//
//		
//		while((key=watchService.take())!=null)
//		{
//			ftpClient.open();
//			
//			for (WatchEvent<?> event : key.pollEvents()) {
//				lbmxFile.setOriginalFileName(event.context().toString());
//				lbmxFile = parseLbmxFile(lbmxFile);//				System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
//				File file = new File(ftpOutDirectory + event.context());
//		        ftpClient.putFileToPath(file, ftpRemoteDirectory + lbmxFile.getPoDateFileName());
//		        
//	            Collection<String> files = ftpClient.listFiles(ftpRemoteDirectory);
//	            if (files.contains(lbmxFile.getPoDateFileName())) {
//	            	moveFile(ftpOutDirectory + lbmxFile.getOriginalFileName(), ftpSentDirectory + lbmxFile.getOriginalFileName());
//	            }
//		        
//			}
//			key.reset();
//			ftpClient.close();
//		}
//
//		watchService.close();
//	}
	
	private void moveFile(String fromPath, String toPath) throws IOException, AS400SecurityException, ObjectAlreadyExistsException {
		copyLbmxFile(system, fromPath, toPath);
		deleteLbmxFile(system, fromPath);
	}
	
	/**
	   Copy integrated file system file or directory
	   @param system The system that contains the file.
	   @param srcPath The source path.
	   @param destpath The destination path.
	   @throws ObjectAlreadyExistsException.
	   @throws AS400SecurityException
	   @throws AS400SecurityException
	 **/
	   public void copyLbmxFile(AS400 system, String srcPath, String destPath
	   ) throws IOException, AS400SecurityException, ObjectAlreadyExistsException{
	      IFSFile file = new IFSFile(system, srcPath);
	      file.copyTo(destPath);
	   }
	
	/**
	   Deletes a directory or file.
	   @param system The system that contains the file.
	   @param path The absolute path name of the file.
	   @throws IOException
	 **/
	   public void deleteLbmxFile(AS400 system, String path) throws IOException{
	      IFSFile file = new IFSFile(system, path);
	      if (file.exists()){
	      file.delete();
	   }
	 }
	
	private LbmxFile parseLbmxFile(LbmxFile lbmxFile) {
		System.out.println("I'm in parseLbmxFile");
		String[] parts = lbmxFile.getOriginalFileName().split("_");
		lbmxFile.setLbmxKey(parts[0]);
		lbmxFile.setCompanyNumber(parts[1]);
		lbmxFile.setPoNumber(parts[2].substring(2));
		lbmxFile.setPoDateFileName(parts[2] + "_" + parts[3]);
		for (String part: parts) { 
			System.out.println(part);
		}
		System.out.println("LBMX Key: " + lbmxFile.getLbmxKey());
		System.out.println("Cono    : " + lbmxFile.getCompanyNumber());
		System.out.println("PO Num  : " + lbmxFile.getPoNumber());
		System.out.println("FileName: " + lbmxFile.getPoDateFileName());

		
		
		
		return lbmxFile;	
	}
}
