package com.umbc.networkmonitor.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.umbc.networkmonitor.vo.HostInfo;
import com.umbc.networkmonitor.vo.PingResponse;

@Component
public interface NetworkMonitorService {

	
	public PingResponse pingHost(String hostName) throws IOException;
	public String sendCommand(boolean waitToResponse, long timeout, String... command);
	public  List<String> runIptablesCommand(String host) throws Exception;
	public PingResponse createHost(HostInfo hostInfo) throws IOException;
	public List<PingResponse> getAllHosts() throws IOException;
}
