package com.dt.rts.recorder.srvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;

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
		String actualURL = traffic.getUrl();
		String relativeUrl = identifyRelativeUrl(actualURL);
		traffic.setRequesturi(relativeUrl);
	}
	
	public String identifyRelativeUrl(String url) {
		try {
			URL aURL = new URL(url);
			//lets say URL is http://localhost:8080/cpa/client/clientInfo?param=value
	    	//getUrl() returns '/cpa/client/clientInfo'
			//But I want queryParams as well.
			String relativeUrl = aURL.getPath();
			relativeUrl = url.substring(url.indexOf(relativeUrl));
			return relativeUrl; 
		}
		catch(Exception e) {
			return url;
		}
	}
	
	public void virtualize(HttpServletRequest httpRequest, HttpServletResponse httpResponse)  throws IOException, ServletException {
		//String relativeUrl = identifyRelativeUrl(httpRequest.getRequestURL().toString());
		String relativeUrl = httpRequest.getRequestURI().toString();
		String queryParam = httpRequest.getQueryString();
		if(queryParam != null && queryParam.length() > 0) {
			relativeUrl = relativeUrl + "?" + queryParam;
		}
		logger.info("Querying DB to check getRequestURI=" + relativeUrl + ", Method=" + httpRequest.getMethod() + " exists.");
		
		List<eRegHttpTraffic> list = trafficRepository.findByRequesturiAndMethod(relativeUrl, httpRequest.getMethod());
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
					logger.info("Content=" + capturedResponse.getContent());
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
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			out.println("{\"name\":\"Greetings from TrafficFilter Virtual service.!\"}");
		}
	}
	/**
	public static void main(String[] args) {
		TrafficService obj = new TrafficService();
		eRegHttpTraffic traffic = new eRegHttpTraffic();
		traffic.setUrl("http://localhost:8080/cpa/client/clientInfo?param=value");
		obj.populateRelativeUrl(traffic);
		System.out.println(traffic.getRequesturi());
	}*/
}
