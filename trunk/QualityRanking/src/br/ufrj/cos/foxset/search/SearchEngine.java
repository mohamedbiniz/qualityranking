/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.util.List;

/**
 * 
 * @author Heraldo
 */
public abstract class SearchEngine {

	protected static final String CHARSET_UTF_8 = "UTF-8";

	public static class Result {

		private String title, URL, summary;

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

	public abstract List<Result> search(String query) throws SearchException;

	public abstract String getSearchEngineCode();
}
