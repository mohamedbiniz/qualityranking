package com.dialogy.odb.test.vo.crawler;

import java.util.Date;

public class OutputLink {
	private long id;
	private Date date;
	private String url;
	
	
	public OutputLink() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OutputLink(long id, Date date, String url) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.date = date;
		this.url = url;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
