package com.dt.rts.recorder.filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dt.rts.recorder.app.Constants;
import com.dt.rts.recorder.srvc.TrafficService;

@Component
@Order(1)
public class TrafficFilter  implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(TrafficFilter.class);
	
	@Autowired
	private TrafficService trafficService;
	
    @Override
    public void doFilter(
      ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
    	
    	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        
    	if(!Constants.VIRTUALIZE) {
    		logger.info("The application is NOT in Virtual mode....");
            logger.info("Starting a TrafficFilter for req : {} ", httpServletRequest.getRequestURI());
            chain.doFilter(httpServletRequest, httpServletResponse);
            logger.info("Committing a TrafficFilter for req : {} ", httpServletRequest.getRequestURI());
    	} else  {
    		logger.info("The application is in Virtual mode....");
    		logger.info("httpServletRequest.getContextPath()=" + httpServletRequest.getContextPath());
    		//lets say URL is http://localhost:8080/cpa/client/clientInfo?param=value
    		//getRequestURI() returns '/cpa/client/clientInfo'
    		logger.info("httpServletRequest.getRequestURI()=" + httpServletRequest.getRequestURI()); 			
    		//httpServletResponse.reset();
    		//httpServletResponse.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    		//httpServletResponse.setHeader("Location", url);
    		trafficService.virtualize(httpServletRequest, httpServletResponse);
    		return;
    	}
    }
    
    @Override
    public void destroy() {
    	logger.info("Destroy filter: {}", getClass().getSimpleName());
    }

}
