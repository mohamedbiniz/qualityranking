/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Heraldo
 */
public class WebFile {

	private Map<String, List<String>> headers;
	private URL url;
	private int responseCode = -1;
	private String mimeType;
	private String charset;
	private Object content;
	private Date lastModified;
	private Map<String, Integer> forwardLinks, backLinks;

	public WebFile(String webAddress) throws MalformedURLException, IOException {
		url = new URL(webAddress);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setConnectTimeout(10000);
		httpConn.setReadTimeout(10000);
		httpConn.setInstanceFollowRedirects(true);
		httpConn.setRequestProperty("User-agent", "FoxSet-Crawler");
		httpConn.connect();

		headers = httpConn.getHeaderFields();
		responseCode = httpConn.getResponseCode();
		this.url = httpConn.getURL();
		int length = httpConn.getContentLength();
		String type = httpConn.getContentType();
		lastModified = new Date(httpConn.getLastModified());
		if (type != null) {
			String[] parts = type.split(";");
			mimeType = parts[0].trim();
			for (int i = 1; i < parts.length && charset == null; i++) {
				String t = parts[i].trim();
				int index = t.toLowerCase().indexOf("charset=");
				if (index != -1) {
					charset = t.substring(index + 8);
				}
			}
		}

		InputStream stream = httpConn.getErrorStream();
		if (stream != null) {
			content = readStream(length, stream);
		} else if ((content = httpConn.getContent()) != null
				&& content instanceof InputStream) {
			content = readStream(length, (InputStream) content);
		}
		httpConn.disconnect();
	}

	private Object readStream(int length, InputStream stream)
			throws IOException {
		int buflen = Math.max(1024, Math.max(length, stream.available()));
		byte[] buf = new byte[buflen];
		byte[] bytes = null;

		for (int nRead = stream.read(buf); nRead != -1; nRead = stream
				.read(buf)) {
			if (bytes == null) {
				bytes = buf;
				buf = new byte[buflen];
				continue;
			}
			final byte[] newBytes = new byte[bytes.length + nRead];
			System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
			System.arraycopy(buf, 0, newBytes, bytes.length, nRead);
			bytes = newBytes;
		}

		if (charset == null) {
			return bytes;
		}
		try {
			return new String(bytes, charset);
		} catch (UnsupportedEncodingException uee) {
		}
		return bytes;
	}

	public Object getContent() {
		return content;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public Map<String, List<String>> getHeaderFields() {
		return headers;
	}

	public URL getURL() {
		return url;
	}

	public String getMIMEType() {
		return mimeType;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public Map<String, Integer> getForwardLinks() {
		if (forwardLinks == null) {
			forwardLinks = new HashMap<String, Integer>();
			Pattern p = Pattern.compile(Pattern.quote("href=\"") + "("
					+ Pattern.quote("http://") + "[^" + Pattern.quote("\"")
					+ "]+)");
			Matcher m = p.matcher((String) content);
			while (m.find()) {
				String matchedURL = m.group(1);
				Integer count = forwardLinks.get(matchedURL);
				forwardLinks.put(matchedURL, count == null ? 1 : count + 1);
			}
		}
		return forwardLinks;
	}

	public Map<String, Integer> getBackLinks() throws MalformedURLException,
			IOException {
		if (backLinks == null) {
			backLinks = new HashMap<String, Integer>();
			int start = 0;
			boolean hasNext = false;
			do {
				WebFile wf = new WebFile("http://www.google.com/search?q=link:"
						+ URLEncoder.encode(url.toString(), "utf-8")
						+ "&start=" + start);
				start += 10;
				Pattern pNext = Pattern.compile(Pattern
						.quote("<span>Next</span>"));
				Matcher mNext = pNext.matcher((String) wf.content);
				hasNext = mNext.find();

				Pattern p = Pattern.compile(Pattern.quote("<div class=g")
						+ "[^>]*" + Pattern.quote("><a href=\"") + "("
						+ Pattern.quote("http://") + "[^" + Pattern.quote("\"")
						+ "]+)");
				Matcher m = p.matcher((String) wf.content);
				while (m.find()) {
					String matchedURL = m.group(1);
					Integer count = backLinks.get(matchedURL);
					backLinks.put(matchedURL, count == null ? 1 : count + 1);
				}
			} while (hasNext);
		}
		return backLinks;
	}
}