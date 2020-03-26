package com.dpslink.lbmxtransfer;

import java.beans.PropertyVetoException;

import org.springframework.stereotype.Component;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.ProgramCall;

@Component
public class IbmCLHandler {
	
	
	public void callClProgram() {
		String programName = "/QSYS.LIB/RYANI.LIB/TESTCL.PGM";

		AS400 system = new AS400("dps.dpslink.com", "ryani", "Xcode2016");
		
		ProgramCall clProgram = new ProgramCall(system);
		try {
			clProgram.setProgram(programName);
			System.out.println("Just called TESTCL.PGM");
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		  // Done with the server.
		  system.disconnectAllServices();
	}
	
}
