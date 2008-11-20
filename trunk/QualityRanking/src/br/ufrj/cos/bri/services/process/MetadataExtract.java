/**
 * 
 */
package br.ufrj.cos.bri.services.process;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import br.ufrj.cos.bri.enume.MetadataType;

/**
 * @author Fabricio
 * 
 */
public class MetadataExtract {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private URL url;

	public MetadataExtract(String url) throws MalformedURLException {
		setUrlPath(url);
	}

	public HashMap<MetadataType, byte[]> extract() throws IOException,
			ParserException {
		HashMap<MetadataType, byte[]> listMetadatas = new HashMap<MetadataType, byte[]>();
		MetadataType[] metadataTypes = MetadataType.values();
		HttpURLConnection connection = null;

		connection = (HttpURLConnection) getUrl().openConnection();
		connection.setUseCaches(false);

		for (MetadataType metadataType : metadataTypes) {
			byte[] value = extractMetadata(metadataType, connection);
			if (value != null)
				listMetadatas.put(metadataType, value);
		}

		connection.disconnect();

		return listMetadatas;
	}

	private byte[] extractMetadata(MetadataType metadataType,
			URLConnection connection) throws ParserException {
		byte[] value = null;
		if (metadataType.equals(MetadataType.FORMAT)) {
			value = extractFormat(connection);
		} else if (metadataType.equals(MetadataType.DATE)) {
			value = extractDate(connection);
		} else if (metadataType.equals(MetadataType.ENCODED)) {
			value = extractEncoded(connection);
		} else if (metadataType.equals(MetadataType.TITLE)) {
			value = extractTitle(connection);
		}
		return value;
	}

	private byte[] extractFormat(URLConnection connection) {
		byte[] result = null;
		String contentType = connection.getContentType();
		if (contentType != null) {
			result = contentType.getBytes();
		}
		return result;
	}

	// private byte[] extractCreateDate(URLConnection connection) {
	// byte[] result = null;
	// Date createDate = new Date(connection.getDate());
	// if (createDate != null) {
	// DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	// result = dateFormat.format(createDate).getBytes();
	// }
	// return result;
	// }

	private byte[] extractEncoded(URLConnection connection) {
		byte[] result = null;
		String encoded = connection.getContentEncoding();
		if (encoded != null) {
			result = encoded.getBytes();
		}
		return result;
	}

	private byte[] extractDate(URLConnection connection) {
		byte[] result = null;
		Date lastModified = new Date(connection.getLastModified());
		if (lastModified != null) {
			DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			result = dateFormat.format(lastModified).getBytes();
		}
		return result;
	}

	private byte[] extractTitle(URLConnection connection)
			throws ParserException {
		byte[] result = null;
		String title = null;

		Parser parser = null;
		parser = new Parser(connection);
		NodeList nl = parser.parse(new TagNameFilter("TITLE"));

		if (nl.size() != 0) {
			TitleTag t = (TitleTag) nl.elementAt(0);
			title = t.getTitle();
		}
		if (title != null) {
			result = title.getBytes();
		}
		return result;
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
	 * @return the url
	 */
	public String getUrlPath() {
		return url.toString();
	}

	/**
	 * @param url
	 *            the url to set
	 * @throws MalformedURLException
	 */
	public void setUrlPath(String urlPath) throws MalformedURLException {
		this.url = new URL(urlPath);
	}

}
