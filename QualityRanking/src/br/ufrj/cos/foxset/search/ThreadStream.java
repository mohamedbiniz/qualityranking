/**
 * 
 */
package br.ufrj.cos.foxset.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Fabricio
 * 
 */
public class ThreadStream extends Thread {

	private URL url = null;

	private InputStream stream = null;
	
	private boolean pronto = false;
	
	

	public ThreadStream(URL url) {
		setPronto(false);
		setUrl(url);		
	}

	@Override
	public void run() {
		super.run();
		InputStream stream = null;
		try {
			stream = getUrl().openStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		setStream(stream);
		setPronto(true);
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
	
	/**
	 * @return the pronto
	 */
	public boolean isPronto() {
		return pronto;
	}

	/**
	 * @param pronto the pronto to set
	 */
	public void setPronto(boolean pronto) {
		this.pronto = pronto;
	}

}
