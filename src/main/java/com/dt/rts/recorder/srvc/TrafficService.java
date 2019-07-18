package com.dt.rts.recorder.srvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dt.rts.recorder.entity.KeyValue;
import com.dt.rts.recorder.entity.Response;
import com.dt.rts.recorder.entity.eRegHttpTraffic;
import com.dt.rts.recorder.repo.TrafficRepository;

@Service(value="trafficService")
public class TrafficService {
	@Autowired
	private TrafficRepository trafficRepository;
	private static final Logger logger = LoggerFactory.getLogger(TrafficService.class);
	
	public void save(eRegHttpTraffic traffic) {
		
		populateRelativeUrl(traffic);
		trafficRepository.save(traffic);
	}
	
	public void populateRelativeUrl(eRegHttpTraffic traffic) {
		try {
			URL aURL = new URL(traffic.getUrl());
			traffic.setRequesturi(aURL.getPath());
		}
		catch(Exception e) {
			
		}
	}
	
	public void virtualize(HttpServletRequest httpRequest, HttpServletResponse httpResponse)  throws IOException, ServletException {
		logger.info("Querying DB to check getRequestURI=" + httpRequest.getRequestURI() + ", Method=" + httpRequest.getMethod() + " exists.");
		List<eRegHttpTraffic> list = trafficRepository.findByRequesturiAndMethod(httpRequest.getRequestURI(), httpRequest.getMethod());
		PrintWriter out = httpResponse.getWriter();
		String contentType = "application/json";
		if(list != null && list.size() > 0) {
			logger.info("There are " + list.size() + " virtual matching responses.");
			eRegHttpTraffic capturedTrafffic = list.get(0); //always take the 1st one
			Response capturedResponse = capturedTrafffic.getResponse();
			if(capturedResponse != null) {
				logger.info("There is a response");
				List<KeyValue> capturedResponseHeaders = capturedResponse.getHeaders();
				if(capturedResponseHeaders != null && capturedResponseHeaders.size() > 0) {
					logger.info("There are response headers");
					for(int i=0; i<capturedResponseHeaders.size(); i++) {
						KeyValue keyValue = capturedResponseHeaders.get(i);
						httpResponse.setHeader(keyValue.getName(), keyValue.getValue());
					}
					out.println(capturedResponse.getContent());
					/*
					KeyValue contentTypeKeyValue = capturedResponseHeaders.stream().filter(item -> "content-type".equalsIgnoreCase(item.getName())).findFirst().orElse(null);
					if(contentTypeKeyValue != null) {
						contentType = contentTypeKeyValue.getValue();
						logger.info("TrafficService virtualize found contentType=" + contentType);
						httpResponse.setContentType(contentType);
						httpResponse.setStatus(new Long(capturedResponse.getStatus()).intValue());
						
					} else {
						logger.info("There is NO content-type response header");
					}*/
				}
			}
		} else {
			logger.info("There are NO virtual matching responses. Returning generic response");
			httpResponse.setContentType(contentType);
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			out.println("{\"name\":\"Greetings from TrafficFilter Virtual service.!\"}");
		}
	}
	
}
