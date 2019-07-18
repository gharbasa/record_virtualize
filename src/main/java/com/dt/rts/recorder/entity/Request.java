package com.dt.rts.recorder.entity;

import java.io.Serializable;
import java.util.List;

public class Request  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	List<KeyValue> headers;
	PostData postData; //Post Request payload
	public List<KeyValue> getHeaders() {
		return headers;
	}
	public void setHeaders(List<KeyValue> headers) {
		this.headers = headers;
	}
	public PostData getPostData() {
		return postData;
	}
	public void setPostData(PostData postData) {
		this.postData = postData;
	}
}
