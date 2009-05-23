/**
 * 
 */
package br.ufrj.cos.foxset.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;

/**
 * @author Fabricio
 * 
 */
public class ThreadBuffer extends ThreadFoxSet {

	private InputStream stream = null;

	private StringBuffer content = null;

	public ThreadBuffer(InputStream stream) {
		setPronto(false);
		setStream(stream);
	}

	@Override
	public void execute() throws ConnectException {
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
