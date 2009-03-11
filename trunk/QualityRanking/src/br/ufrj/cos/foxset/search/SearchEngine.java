/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Heraldo
 */
public abstract class SearchEngine {

	static {
		setPropertiesForSearch();
	}

	protected static final String CHARSET_UTF_8 = "UTF-8";

	public static class Result implements Comparable<Result> {

		private String title, URL, summary;

		private Date modificationDate;

		public String getURL() {
			return URL;
		}

		public void setURL(String URL) {
			this.URL = URL.trim();
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Date getModificationDate() {
			return modificationDate;
		}

		public void setModificationDate(Date modificationDate) {
			this.modificationDate = modificationDate;
		}

		public void setModificationDate(String modificationDateStrUnixTime) {
			int unixTime = new Integer(modificationDateStrUnixTime).intValue();
			long timestamp = unixTime * 1000; // msec
			setModificationDate(new Date(timestamp));
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Result) {
				Result r = (Result) obj;
				int c = compareTo(r);
				if (c == 0)
					return true;
			}
			return false;
		}

		public int compareTo(Result r) {
			if (getURL() == null && r.getURL() == null) {
				return 0;
			} else if (getURL() == null) {
				return -1;
			} else {
				return getURL().compareToIgnoreCase(r.getURL());
			}
		}
		
		@Override
		public String toString() {			
			return String.format("%s", getURL());
		}
	}

	private String appID;
	private int maxResults;

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public final List<Result> search(String query) throws SearchException {
		setPropertiesForSearch();
		return searchImpl(query);
	}

	protected static void setPropertiesForSearch() {
		System
				.setProperty("javax.xml.parsers.DocumentBuilderFactory",
						"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
	}

	public abstract List<Result> searchImpl(String query)
			throws SearchException;

	public abstract String getSearchEngineCode();

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public final Date findModificationDate(String urlStr)
			throws SearchException {
		setPropertiesForSearch();

		return findModificationDateImpl(urlStr);
	}

	public abstract Date findModificationDateImpl(String urlStr)
			throws SearchException;
}
