package com.dt.rts.recorder.entity;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable{
	private static final long serialVersionUID = 1L;
	
	String content;//THis is JSON response
	List<KeyValue> headers;
	long status;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<KeyValue> getHeaders() {
		return headers;
	}
	public void setHeaders(List<KeyValue> headers) {
		this.headers = headers;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	
}
