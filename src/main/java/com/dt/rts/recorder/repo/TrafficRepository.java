package com.dt.rts.recorder.repo;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.dt.rts.recorder.entity.eRegHttpTraffic;

@Repository(value="trafficRepository")
public interface TrafficRepository  extends PagingAndSortingRepository<eRegHttpTraffic, Long> {
	
	public List<eRegHttpTraffic> findByRequesturiAndMethod(String requesturi, String method);

}
