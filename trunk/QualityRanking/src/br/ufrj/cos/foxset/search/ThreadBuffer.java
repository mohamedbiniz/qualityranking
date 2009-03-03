/**
 * 
 */
package br.ufrj.cos.foxset.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Fabricio
 * 
 */
public class ThreadBuffer extends ThreadFoxSet {

	private InputStream stream = null;

	private StringBuffer content = null;

	private boolean pronto = false;

	public ThreadBuffer(InputStream stream) {
		setPronto(false);
		setStream(stream);
	}

	@Override
	public void run() {
		super.run();
		StringBuffer content = null;
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(stream));
			String line = null;
			content = new StringBuffer();
			while ((line = br.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		setContent(content);
		setPronto(true);
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
	 * @param pronto
	 *            the pronto to set
	 */
	public void setPronto(boolean pronto) {
		this.pronto = pronto;
	}

	/**
	 * @return the content
	 */
	public StringBuffer getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(StringBuffer content) {
		this.content = content;
	}

}
