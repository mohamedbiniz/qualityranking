package br.ufrj.htmlbase.extractor.metadata;

import java.util.Date;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Jan 22, 2006 Time: 3:53:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataCrawler {
	public static final String DATA = "dataCriacao";
	public static final String LAST_MODIFIED = "lastModified";
	public static final String CONTENT_TYPE = "contentType";
	public static final String ENCODED = "encoded";
	public static final String TITLE = "title";

	public MetadataCrawler() {

	}

	public MetadataCrawler(String p, String v) {
		this.name = p;
		this.value = v;
		/*
		 * //@TODO precisa entender com calma pq p ta vindo null em alguns casos
		 * if(p == null) p = "tempValue";
		 * 
		 * MD5Hash md5 = MD5Hash.digest(p); setId(md5.halfDigest());
		 */
		setId(System.currentTimeMillis());
		setDateCreate(new Date());
		setLastModified(new Date());

	}

	public String getName() {
		return name;
	}

	public void setName(String property) {
		this.name = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private String name;

	private String value;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private long id;

	private long idPage;

	public long getIdPage() {
		return idPage;
	}

	public void setIdPage(long idPage) {
		this.idPage = idPage;
	}

	private Date dateCreate;
	private Date lastModified;

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
