package com.umbc.networkmonitor.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umbc.networkmonitor.service.NetworkMonitorService;
import com.umbc.networkmonitor.vo.HostInfo;
import com.umbc.networkmonitor.vo.PingResponse;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class NetworkMonitorController {
	@Autowired
	NetworkMonitorService service;

@GetMapping("/api/ping/{hostName}")
public ResponseEntity<PingResponse> pingHost(@PathVariable String hostName) throws IOException {
	return ResponseEntity.ok(service.pingHost(hostName)) ;
	
	
}

@GetMapping("/api/getAllHosts")
public ResponseEntity<List<PingResponse>>getAllHostsData() throws IOException {
	return ResponseEntity.ok(service.getAllHosts());
	
	
}

@PostMapping("/api/createHost")
public ResponseEntity<PingResponse> createHost(@RequestBody HostInfo hostInfo) throws IOException {
	return  ResponseEntity.ok(service.createHost(hostInfo));
	
	
}

@GetMapping("/api/command")
public ResponseEntity<String> sendCommand(@RequestParam String[] command) throws IOException {
	return ResponseEntity.ok(service.sendCommand(true, 1000, command));
	
	
}

@GetMapping("/api/iptables/{host}")
public ResponseEntity<List<String>> getIptables(@PathVariable String host) throws Exception {
	return ResponseEntity.ok(service.runIptablesCommand(host));
	
	
}
}
