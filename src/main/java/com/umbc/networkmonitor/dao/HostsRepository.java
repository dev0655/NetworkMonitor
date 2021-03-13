package com.umbc.networkmonitor.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umbc.networkmonitor.model.Hosts;

@Repository
public interface HostsRepository extends JpaRepository<Hosts, String>{
	


}
