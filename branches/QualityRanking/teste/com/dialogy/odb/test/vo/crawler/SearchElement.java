package com.dialogy.odb.test.vo.crawler;

/**
 * Created by IntelliJ IDEA.
 * User: olivier s
 * Date: 04/12/2005
 * Time: 19:19:19
 * To change this template use File | Settings | File Templates.
 */
public class SearchElement {
    //private static final byte DEFAULT_INTERVAL = (byte)CrawlerConf.get().getInt("default.fetch.interval", 30);

      private long id;
      private byte version;
      private String url;
      private long nextFetch = System.currentTimeMillis();
      private byte retries;
      private byte fetchInterval = 30;
      private int numOutlinks;
      private float score = 1.0f;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getNextFetch() {
        return nextFetch;
    }

    public void setNextFetch(long nextFetch) {
        this.nextFetch = nextFetch;
    }

    public byte getRetries() {
        return retries;
    }

    public void setRetries(byte retries) {
        this.retries = retries;
    }

    public byte getFetchInterval() {
        return fetchInterval;
    }

    public void setFetchInterval(byte fetchInterval) {
        this.fetchInterval = fetchInterval;
    }

    public int getNumOutlinks() {
        return numOutlinks;
    }

    public void setNumOutlinks(int numOutlinks) {
        this.numOutlinks = numOutlinks;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
