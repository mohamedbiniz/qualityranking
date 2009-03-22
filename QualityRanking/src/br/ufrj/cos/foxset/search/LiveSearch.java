/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * @author Heraldo
 */
public class LiveSearch extends SearchEngine {

	private static final String DATE_FORMAT_LIVE_SEARCH = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	private static int MAX_RESULTS_LOOP = 50;

	private static int FINISHING_POSITION = 1000;

	@Override
	public List<Result> searchImpl(String query) throws SearchException {
		try {
			List<Result> results = new ArrayList<Result>(
					(getMaxResults() > FINISHING_POSITION ? FINISHING_POSITION
							: getMaxResults()));

			int qtdLoops = (getMaxResults() / MAX_RESULTS_LOOP);

			for (int l = 0; l <= qtdLoops; l++) {

				int start = (l * MAX_RESULTS_LOOP) + 1;

				int qtdRestsResults = Math.min(getMaxResults(),
						FINISHING_POSITION)
						- start + 1;
				if (qtdRestsResults == 0) {
					break;
				}
				int qtdResults = Math.min(qtdRestsResults, MAX_RESULTS_LOOP);

				start = Math.min(start, FINISHING_POSITION - MAX_RESULTS_LOOP
						+ 1);

				String urlSearch = String.format(
						"http://api.search.live.net/xml.aspx?" + "AppId=%s"
								+ "&market=%s" + "&Query=%s" + "&Sources=%s"
								+ "&web.offset=%d" + "&web.count=%d"
								+ "&xmltype=%s", getAppID(), "en-US", query,
						"web+spell", start, qtdResults, "elementbased");

				results.addAll(extractResults(urlSearch));
			}
			return results;

		} catch (Exception se) {
			throw new SearchException("Unable to perform MSN Live search", se);
		}
	}

	/**
	 * 
	 * @param urlSearch
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws XPathExpressionException
	 * @throws ParseException
	 * @throws DOMException
	 */
	private List<Result> extractResults(String urlSearch)
			throws MalformedURLException, IOException,
			ParserConfigurationException, SAXException,
			XPathExpressionException, DOMException, ParseException {
		List<Result> results = new ArrayList<Result>();

		String content = new WebDocument(urlSearch).getContentAsString().trim();

		System.out.println(content);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(content)));

		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList) xpath.evaluate(
				"/SearchResponse/Web/Results/WebResult", doc,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element elem = (Element) nodes.item(i);
			Result result = new Result();
			result.setTitle(elem.getElementsByTagName("web:Title").item(0)
					.getTextContent());
			result.setURL(elem.getElementsByTagName("web:Url").item(0)
					.getTextContent());
			Node elemLastModified = elem.getElementsByTagName("web:DateTime")
					.item(0);
			if (elemLastModified != null) {
				result.setLastModified(elemLastModified.getTextContent(),
						DATE_FORMAT_LIVE_SEARCH);
			}
			Node elemRank = elem.getElementsByTagName("web:Rank").item(0);
			if (elemRank != null) {
				result.setRank(Double.parseDouble(elemRank.getTextContent()));
			}
			NodeList desc = elem.getElementsByTagName("web:Description");
			if (desc.getLength() > 0)
				result.setSummary(desc.item(0).getTextContent());
			results.add(result);
		}
		return results;
	}

	@Override
	public String getSearchEngineCode() {
		return "LIVE";
	}

	@Override
	public Date findModificationDateImpl(String urlStr) throws SearchException {
		List<Result> results = searchImpl(urlStr);
		for (Result result : results) {
			if (result.getURL().equalsIgnoreCase(urlStr)) {
				return result.getLastModified();
			}
		}
		return null;
	}
}
