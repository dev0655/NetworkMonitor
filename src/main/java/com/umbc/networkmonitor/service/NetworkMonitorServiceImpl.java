package com.umbc.networkmonitor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.io.CharStreams;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.umbc.networkmonitor.dao.HostsRepository;
import com.umbc.networkmonitor.model.Hosts;
import com.umbc.networkmonitor.vo.HostInfo;
import com.umbc.networkmonitor.vo.PingResponse;

@Service
public class NetworkMonitorServiceImpl implements NetworkMonitorService {
	@Autowired
	private HostsRepository hostRepository;
	
	private String ONLINE="online";
	private String OFFLINE="offline";

	@Override
	public PingResponse pingHost(String hostName) throws IOException {
		String success = OFFLINE;
		InetAddress test = InetAddress.getByName(hostName); 
	    
	    if (test.isReachable(50)) {
	      success= ONLINE;
	    }
		return new PingResponse(hostName, success);
	}
	
	@Override
	public String sendCommand(boolean waitToResponse, long timeout, String... command) {
        String result = null;
        try {
            ProcessBuilder ps = new ProcessBuilder(command);
            ps.redirectErrorStream(true);
            Process pr = ps.start();
            if(waitToResponse) {
                try (InputStream inputStream = pr.getInputStream(); InputStreamReader isr = new InputStreamReader(inputStream); BufferedReader in = new BufferedReader(isr)) {
                    StringBuilder content = new StringBuilder();

                    String line;
                    while ((line = in.readLine()) != null) {
                        if(line != null) {
                            content.append(line);
                        }
                    }
                    pr.waitFor(timeout, TimeUnit.SECONDS);

                    result = content.toString();
                    if (result == null || result.isEmpty()) {
                        result = "OK";
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
           
        }

        return result;
    }
	
	@Override
	public  List<String> runIptablesCommand(String host) throws Exception {
		ChannelExec channel =null;
		Session session = null;
		Optional<Hosts> data =hostRepository.findById(host);
		if(data.isPresent()) {
        try {
        	 session = setupSshSession(data.get().getUserName(),host,data.get().getPassword());
            session.connect();

             channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("echo "+data.get().getPassword()+" | sudo -S iptables -L");
            channel.setInputStream(null);
            InputStream output = channel.getInputStream();
            channel.connect();

            String result = CharStreams.toString(new InputStreamReader(output));
            return Arrays.asList(result.split("\n"));

        } catch (JSchException | IOException e) {
            closeConnection(channel, session);
            throw new RuntimeException(e);

        } finally {
            closeConnection(channel, session);
        }
		} else {
			throw new Exception("host not found in database");
		}
    }

    private  Session setupSshSession(String login, String host, String password) throws JSchException {
        Session session = new JSch().getSession(login, host, 22);
        session.setPassword(password);
        session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
        session.setConfig("StrictHostKeyChecking", "no"); // disable check for RSA key
        return session;
    }

    private  void closeConnection(ChannelExec channel, Session session) {
        try {
            channel.disconnect();
        } catch (Exception ignored) {
        }
        session.disconnect();
    }

	@Override
	public PingResponse createHost(HostInfo hostInfo) throws IOException {
		Hosts host = new Hosts();
		host.setHostName(hostInfo.getHostName());
		host.setUserName(hostInfo.getUserName());
		host.setPassword(hostInfo.getPassword());

		PingResponse ping = pingHost(hostInfo.getHostName());
		host.setStatus(ping.getSuccess());
		Hosts hosts = hostRepository.save(host);
		
		return new PingResponse(hosts.getHostName(), hosts.getStatus());
	}

	@Override
	public List<PingResponse> getAllHosts() throws IOException {
		List<Hosts> hosts =hostRepository.findAll();
		List<PingResponse> response = new ArrayList<PingResponse>();
		for (Hosts hosts2 : hosts) {
			
			PingResponse ping = pingHost(hosts2.getHostName());
			hosts2.setStatus(ping.getSuccess());
			hostRepository.save(hosts2);
			response.add(ping);
		}
		return response;
	}


}
