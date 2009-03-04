/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.util.Date;
import java.util.List;

import com.mathworks.toolbox.instrument.icb.IJFrame.SetParameterRunnable;

import sun.reflect.Reflection;

/**
 * 
 * @author Heraldo
 */
public abstract class SearchEngine {

	static {
		setPropertiesForSearch();
	}

	protected static final String CHARSET_UTF_8 = "UTF-8";

	public static class Result {

		private String title, URL, summary;

		private Date modificationDate;

		public String getURL() {
			return URL;
		}

		public void setURL(String URL) {
			this.URL = URL;
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
