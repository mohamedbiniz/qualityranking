package br.ufrj.htmlbase;

import static br.ufrj.htmlbase.io.MD5Hash.digest;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.htmlbase.io.MD5Hash;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Feb 2, 2006 Time: 3:29:07 PM To
 * change this template use File | Settings | File Templates.
 */
public class OutputLinkCrawler implements Serializable {

	private final long SCORE = 100;

	private String url;
	private String domain;
	private boolean visited;
	private boolean seed;
	private long idTest;
	private long idPage;
	private long idDataSet;
	private double score;
	private Date dateCreate;
	private Date lastModified;
	private Date nextFetch;
	private PageCrawler pageCrawler;

	public OutputLinkCrawler(URL u, long idDataSet) {
		setDomain(u.getHost());
		setUrl(u.toString());
		MD5Hash md5 = digest(getUrl());
		setIdTest(md5.halfDigest());
		setDateCreate(new Date());
		setLastModified(new Date());
		setScore(SCORE);
		setIdDataSet(idDataSet);
		setSeed(false);
	}

	public OutputLinkCrawler() {

	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isVisited() {
		return visited;
	}

	public boolean getVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public long getIdTest() {
		return idTest;
	}

	public void setIdTest(long id) {
		this.idTest = id;
	}

	public long getIdPage() {
		return idPage;
	}

	public void setIdPage(long idPage) {
		this.idPage = idPage;
	}

	public int hashCode() {

		MD5Hash md5 = digest(getUrl());
		return md5.hashCode();
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

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

	public Date getNextFetch() {
		return nextFetch;
	}

	public void setNextFetch(Date nextFetch) {
		this.nextFetch = nextFetch;
	}

	/**
	 * @return the idDataSet
	 */
	public long getIdDataSet() {
		return idDataSet;
	}

	/**
	 * @param idDataSet
	 *            the idDataSet to set
	 */
	public void setIdDataSet(long idDataSet) {
		this.idDataSet = idDataSet;
	}

	public void setDataSet(DataSet dataSet) {
		setIdDataSet(dataSet.getId());
	}

	/**
	 * @return the seed
	 */
	public boolean isSeed() {
		return seed;
	}

	/**
	 * @param seed
	 *            the seed to set
	 */
	public void setSeed(boolean seed) {
		this.seed = seed;
	}

	/**
	 * @return the pageCrawler
	 */
	public PageCrawler getPageCrawler() {
		return pageCrawler;
	}

	/**
	 * @param pageCrawler
	 *            the pageCrawler to set
	 */
	public void setPageCrawler(PageCrawler pageCrawler) {
		this.pageCrawler = pageCrawler;
	}

	@Override
	public boolean equals(Object obj) {

		boolean result = false;
		if ((obj != null) && (obj instanceof OutputLinkCrawler)) {
			OutputLinkCrawler link = (OutputLinkCrawler) obj;
			result = (this.getIdTest() == link.getIdTest());
		}
		return result;
	}

	@Override
	public String toString() {
		return String.format("%d\t%s", getIdTest(), getUrl());
	}

}
