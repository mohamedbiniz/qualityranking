/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

/**
 * 
 * @author Heraldo
 */
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URLEncoder;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class YahooSearch extends SearchEngine {

	// ver documentação em
	// http://developer.yahoo.com/search/news/V1/newsSearch.html
	private static int MAX_RESULTS_LOOP = 50;

	private static int FINISHING_POSITION = 1000;

	@Override
	public List<Result> searchImpl(String query) throws SearchException {
		try {
			List<Result> results = new ArrayList<Result>(
					(getMaxResults() > FINISHING_POSITION ? FINISHING_POSITION
							: getMaxResults()));

			query = URLEncoder.encode(query, CHARSET_UTF_8);

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

				String urlSearch = "http://search.yahooapis.com/WebSearchService/V1/webSearch?appid="
						+ getAppID()
						+ "&query="
						+ query
						+ "&results="
						+ qtdResults + "&start=" + start;

				results.addAll(extractResults(urlSearch));
			}

			return results;
		} catch (Exception e) {
			throw new SearchException("Unable to perform Yahoo search", e);
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
	 */
	private List<Result> extractResults(String urlSearch)
			throws MalformedURLException, IOException,
			ParserConfigurationException, SAXException,
			XPathExpressionException {
		List<Result> results = new ArrayList<Result>(MAX_RESULTS_LOOP);

		String content = (String) new WebFile(urlSearch).getContent();
		// System.out.println(content);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new StringReader(content
				.trim())));

		XPath xpath = XPathFactory.newInstance().newXPath();
		NodeList nodes = (NodeList) xpath.evaluate("/ResultSet/Result", doc,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element elem = (Element) nodes.item(i);
			Result result = new Result();
			result.setTitle(elem.getElementsByTagName("Title").item(0)
					.getTextContent());
			result.setURL(elem.getElementsByTagName("Url").item(0)
					.getTextContent());
			result.setSummary(elem.getElementsByTagName("Summary").item(0)
					.getTextContent());
			result.setLastModifiedUnixTime(elem.getElementsByTagName(
					"ModificationDate").item(0).getTextContent());
			results.add(result);
		}
		return results;
	}

	@Override
	public String getSearchEngineCode() {
		return "YAHOO";
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
