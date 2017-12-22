package com.uway.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NmapScanningResult {
	private long scanResultId;
	private String hostIp;
	private String osName;
	private String osNameAccuracy;
	private String osPortUsed;
	private String hostState;
	private int scanPort;
	private int scanPortOpen;
	private int scanPortNOpen;
	private int scanPortFiltered;
	private String scanDetails;
	private Timestamp createTime;
	private Timestamp modifyTime;
	
	private List<NmapScanningPortDetails> scanningPortDetails = new ArrayList<NmapScanningPortDetails>();
	
	public long getScanResultId() {
		return scanResultId;
	}

	public void setScanResultId(long scanResultId) {
		this.scanResultId = scanResultId;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsNameAccuracy() {
		return osNameAccuracy;
	}

	public void setOsNameAccuracy(String osNameAccuracy) {
		this.osNameAccuracy = osNameAccuracy;
	}

	public String getOsPortUsed() {
		return osPortUsed;
	}

	public void setOsPortUsed(String osPortUsed) {
		this.osPortUsed = osPortUsed;
	}

	public String getHostState() {
		return hostState;
	}

	public void setHostState(String hostState) {
		this.hostState = hostState;
	}

	public int getScanPort() {
		return scanPort;
	}

	public void setScanPort(int scanPort) {
		this.scanPort = scanPort;
	}

	public int getScanPortOpen() {
		return scanPortOpen;
	}

	public void setScanPortOpen(int scanPortOpen) {
		this.scanPortOpen = scanPortOpen;
	}

	public int getScanPortFiltered() {
		return scanPortFiltered;
	}

	public void setScanPortFiltered(int scanPortFiltered) {
		this.scanPortFiltered = scanPortFiltered;
	}

	public String getScanDetails() {
		return scanDetails;
	}

	public void setScanDetails(String scanDetails) {
		this.scanDetails = scanDetails;
	}

	public String getCreateTime() {
		if(createTime == null) return "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(createTime);
		} catch (Exception e) {
			return createTime.toString();
		}
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Timestamp modifyTime) {
		this.modifyTime = modifyTime;
	}

	public int getScanPortNOpen() {
		scanPortNOpen = scanPort - scanPortOpen - scanPortFiltered;
		return scanPortNOpen;
	}

	public List<NmapScanningPortDetails> getScanningPortDetails() {
		return scanningPortDetails;
	}

	public void setScanningPortDetails(List<NmapScanningPortDetails> scanningPortDetails) {
		this.scanningPortDetails = scanningPortDetails;
	}


}
