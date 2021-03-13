package com.umbc.networkmonitor.vo;

public class PingResponse {

	private String hostName;
	private String success;
	
	
	
	public PingResponse(String hostName, String success) {
		super();
		this.hostName = hostName;
		this.success = success;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	@Override
	public String toString() {
		return "PingResponse [hostName=" + hostName + ", success=" + success + "]";
	}
	
	
	
}
