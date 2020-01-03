package com.dpslink.lbmxtransfer;

public class LbmxFile {
	
	private String originalFileName;
	private String lbmxKey;
	private String companyNumber;
	private String poDateFileName;
	private String poNumber;
	private String poDate;
	
	public LbmxFile() {};
	
	public LbmxFile(String originalFileName, String lbmxKey, String companyNumber, String poDateFileName, String poNumber, String poDate) {
		super();
		this.originalFileName = originalFileName;
		this.lbmxKey = lbmxKey;
		this.companyNumber = companyNumber;
		this.poDateFileName = poDateFileName;
		this.poNumber = poNumber;
		this.poDate = poDate;
	}
	
	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}
	
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
	
	public String getLbmxKey() {
		return lbmxKey;
	}
	
	public void setLbmxKey(String lbmxKey) {
		this.lbmxKey = lbmxKey;
	}
	
	public String getCompanyNumber() {
		return companyNumber;
	}
	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}
	
	public String getPoDateFileName() {
		return poDateFileName;
	}
	
	public void setPoDateFileName(String poDateFileName) {
		this.poDateFileName = poDateFileName;
	}
	

}
