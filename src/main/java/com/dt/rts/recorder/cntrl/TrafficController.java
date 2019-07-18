package com.dt.rts.recorder.cntrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dt.rts.recorder.entity.eRegHttpTraffic;
import com.dt.rts.recorder.srvc.TrafficService;

@RestController
public class TrafficController {
    
	private static final Logger logger = LoggerFactory.getLogger(TrafficController.class);
	
	@Autowired
	private TrafficService trafficService;
	
	@RequestMapping("/")
    public String index() {
        return "Greetings from TrafficController!";
    }
    
    @RequestMapping(value = "/record", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String record(@RequestBody eRegHttpTraffic body) {
    	logger.info(body.toString());
    	trafficService.save(body);
    	
    	return "Successful!";
    }
}
