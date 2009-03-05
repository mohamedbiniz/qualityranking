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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebDocument {

	public static int qtdLinks = 10;

	private Map<String, List<String>> headers;
	private URL url;
	private int responseCode = -1;
	private String mimeType;
	private String charset;
	private Object content;
	private Date lastModified;
	private Map<String, Integer> forwardLinks, backLinks;

	public WebDocument(String webAddress) throws MalformedURLException,
			IOException {
		System.out.println("Entrando...");
		url = new URL(webAddress);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setConnectTimeout(10000);
		httpConn.setReadTimeout(10000);
		httpConn.setInstanceFollowRedirects(true);
		httpConn
				.setRequestProperty(
						"User-agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US; rv:1.9.1b3pre) Gecko/20090105 Firefox/3.1b3pre");
		httpConn.connect();

		headers = httpConn.getHeaderFields();
		responseCode = httpConn.getResponseCode();
		this.url = httpConn.getURL();
		int length = httpConn.getContentLength();
		if (length > 512 * 1024) {
			throw new IOException("Tamanho grande!");
		}
		System.out.println("Length: " + length);
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

		/*
		 * InputStream stream = httpConn.getErrorStream(); if (stream != null) {
		 * content = readStream(length, stream); } else if ((content =
		 * httpConn.getContent()) != null && content instanceof InputStream) {
		 * content = readStream(length, (InputStream) content); }
		 */

		ThreadStream threadStream = new ThreadStream(url);
		waitThread(threadStream, 10);

		InputStream stream = threadStream.getStream();
		if (stream == null)
			throw new IOException("Erro ao ler stream da url " + url.toString());

		ThreadBuffer threadBuffer = new ThreadBuffer(stream);
		waitThread(threadBuffer, 3 * 60);
		StringBuffer sb = threadBuffer.getContent();
		if (sb == null)
			throw new IOException("Erro ao ler conteúdo stream da url "
					+ url.toString());
		content = sb.toString();

		httpConn.disconnect();
		System.out.println("Saindo... Length: " + sb.length());
	}

	/**
	 * @param threadBuffer
	 */
	private void waitThread(ThreadFoxSet thread, int sec) {
		int timeSleep = 500; // meio segundo
		int qtdMaxVer = (sec * 1000) / timeSleep;
		ExecutorService tpes = null;
		tpes = Executors.newFixedThreadPool(1);
		tpes.execute(thread);
		try {
			int qtdVerf = 0;
			while (!thread.isPronto()) {
				if (qtdVerf++ == qtdMaxVer)
					break;
				Thread.sleep(timeSleep);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			tpes.shutdown();
			tpes = null;
		}
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
			bytes = null;
			System.gc();
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

	public String getContentAsString() {
		if (content instanceof byte[]) {
			try {
				return new String((byte[]) content, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				Logger.getLogger(WebDocument.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			return null;
		} else if (content instanceof String) {
			return (String) content;
		} else {
			return null;
		}
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
			String str = getContentAsString();
			if (str == null) {
				return forwardLinks;
			}
			Matcher m = p.matcher(str);
			int j = 0;
			while (m.find()/* && (j++<=qtdLinks) */) {
				String matchedURL = m.group(1).trim();

				// Filtra sites proibidos.
				Pattern p1 = Pattern
						.compile("\\/ad\\/|\\/ads\\/|\\/ads\\.|\\/ad\\.|\\.\\?|\\.blog|\\/blog|\\/dblp");
				Matcher m1 = p1.matcher(matchedURL.toLowerCase());
				if (m1.find()) {
					System.out.println("URL desconsiderada FL= " + matchedURL);
					break;
				}
				Integer count = forwardLinks.get(matchedURL);
				forwardLinks.put(matchedURL, count == null ? 1 : count + 1);
			}
		}
		return forwardLinks;
	}

	public Map<String, Integer> getBackLinks(boolean discardSameDomain)
			throws MalformedURLException, IOException {
		if (backLinks == null) {
			backLinks = new HashMap<String, Integer>();
			int start = 0;
			boolean hasNext = false;
			do {
				WebDocument wf = new WebDocument(
						"http://www.google.com/search?q=link"
								+ URLEncoder.encode(":" + url.toString(),
										"UTF-8") + "&start=" + start);
				start += 10;
				Pattern pNext = Pattern.compile(Pattern
						.quote("<span>Next</span>"));
				Matcher mNext = pNext.matcher(wf.getContentAsString());
				hasNext = mNext.find();
				// <li class=g style="margin-left:3em"><h3 class=r><a
				// href="http://silveiraneto.net/downloads/?C=M;O=A"
				Pattern p = Pattern.compile(Pattern.quote("<li class=g")
						+ "[^>]*" + Pattern.quote("><h3 class=r><a href=\"")
						+ "(" + Pattern.quote("http://") + "[^"
						+ Pattern.quote("\"") + "]+)");
				Matcher m = p.matcher(wf.getContentAsString());
				while (m.find()) {
					String matchedURL = m.group(1).trim();

					// Filtra sites proibidos.
					if (discardUrl(matchedURL, discardSameDomain, url
							.toString()))
						break;
					Integer count = backLinks.get(matchedURL);
					backLinks.put(matchedURL, count == null ? 1 : count + 1);
				}
			} while (hasNext);
		}
		return backLinks;
	}

	public static boolean discardUrl(String urlNowStr,
			boolean discardSameDomain, String urlLinkStr) throws IOException {
		// Filtra sites proibidos.
		Pattern p1 = Pattern
				.compile("\\/ad\\/|\\/ads\\/|\\/ads\\.|\\/ad\\.|\\?|\\.blog|\\/blog|\\/dblp");
		Matcher m1 = p1.matcher(urlNowStr.toLowerCase());
		if (m1.find()) {
			System.out.println("URL desconsiderada = " + urlNowStr);
			return true;
		}
		if (discardSameDomain) {
			URL urlNow = new URL(urlNowStr);
			URL urlLink = new URL(urlLinkStr);
			if (urlNow.getHost().equalsIgnoreCase(urlLink.getHost())) {
				return true;
			}
		}
		return false;
	}
}