package com.dt.rts.recorder.entity;

import java.io.Serializable;
import java.util.List;

public class PostData  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	String mimeType;
	String text;
	List<KeyValue> params;
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<KeyValue> getParams() {
		return params;
	}
	public void setParams(List<KeyValue> params) {
		this.params = params;
	}
	
}
