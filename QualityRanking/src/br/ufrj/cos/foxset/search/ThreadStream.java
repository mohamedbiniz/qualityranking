/**
 * 
 */
package br.ufrj.cos.foxset.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;

/**
 * @author Fabricio
 * 
 */
public class ThreadStream extends ThreadFoxSet {

	private URL url = null;

	private InputStream stream = null;

	public ThreadStream(URL url) {
		setPronto(false);
		setUrl(url);
	}

	@Override
	public void execute() throws ConnectException {
		InputStream stream = null;
		try {
			stream = getUrl().openStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setStream(stream);
	}

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * @return the stream
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * @param stream
	 *            the stream to set
	 */
	public void setStream(InputStream stream) {
		this.stream = stream;
	}

}
