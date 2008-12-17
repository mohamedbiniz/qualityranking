/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

/**
 * 
 * @author Heraldo
 */
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class YahooSearch extends SearchEngine {

	@Override
	public List<Result> search(String query) throws SearchException {
		try {
			List<Result> results = new ArrayList<Result>(getMaxResults());

			query = URLEncoder.encode(query, CHARSET_UTF_8);

			String URL = "http://search.yahooapis.com/WebSearchService/V1/webSearch?appid="
					+ getAppID()
					+ "&query="
					+ query
					+ "&results="
					+ getMaxResults();

			// String URL =
			// "http://search.yahooapis.com/WebSearchService/V1/webSearch?appid="
			// + getAppID() + "&query=economia&results=" + getMaxResults();

			String content = (String) new WebFile(URL).getContent();
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(
					content.trim())));

			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate("/ResultSet/Result",
					doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Element elem = (Element) nodes.item(i);
				Result result = new Result();
				result.setTitle(elem.getElementsByTagName("Title").item(0)
						.getTextContent());
				result.setURL(elem.getElementsByTagName("Url").item(0)
						.getTextContent());
				result.setSummary(elem.getElementsByTagName("Summary").item(0)
						.getTextContent());
				results.add(result);
			}

			return results;
		} catch (Exception e) {
			throw new SearchException("Unable to perform Yahoo search", e);
		}
	}

	@Override
	public String getSearchEngineCode() {
		return "YAHOO";
	}
}
