/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.soap.SOAPElement;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Heraldo
 */
public class GoogleSearch extends SearchEngine {

	@Override
	public List<Result> searchImpl(String query) throws SearchException {
		try {
			List<Result> results = new ArrayList<Result>(getMaxResults());

			SOAPCall googleSearch = new SOAPCall(
					"http://api.google.com/search/beta2", "doGoogleSearch",
					"foxset", "urn:GoogleSearch");
			SOAPElement bodyElement = googleSearch.getBodyElement();
			bodyElement.addChildElement("key").addTextNode(getAppID())
					.setAttribute("xsi:type", "xsd:string");
			bodyElement.addChildElement("q").addTextNode(query).setAttribute(
					"xsi:type", "xsd:string");
			bodyElement.addChildElement("start").addTextNode("0").setAttribute(
					"xsi:type", "xsd:int");
			bodyElement.addChildElement("maxResults").addTextNode(
					Integer.toString(getMaxResults())).setAttribute("xsi:type",
					"xsd:int");
			bodyElement.addChildElement("filter").addTextNode("true")
					.setAttribute("xsi:type", "xsd:boolean");
			bodyElement.addChildElement("restrict").addTextNode("")
					.setAttribute("xsi:type", "xsd:string");
			bodyElement.addChildElement("safeSearch").addTextNode("true")
					.setAttribute("xsi:type", "xsd:boolean");
			bodyElement.addChildElement("lr").addTextNode("lang_en|lang_pt")
					.setAttribute("xsi:type", "xsd:string");
			bodyElement.addChildElement("ie").addTextNode("").setAttribute(
					"xsi:type", "xsd:string");
			bodyElement.addChildElement("oe").addTextNode("").setAttribute(
					"xsi:type", "xsd:string");
			Document doc = googleSearch.call();

			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(
					"//doGoogleSearchResponse/return/resultElements/item", doc,
					XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Element elem = (Element) nodes.item(i);
				Result result = new Result();
				result.setTitle(elem.getElementsByTagName("title").item(0)
						.getTextContent());
				result.setURL(elem.getElementsByTagName("URL").item(0)
						.getTextContent());
				result.setSummary(elem.getElementsByTagName("snippet").item(0)
						.getTextContent());
				results.add(result);
			}

			return results;
		} catch (Exception se) {
			throw new SearchException("Unable to perform Google search", se);
		}
	}

	@Override
	public String getSearchEngineCode() {
		return "GOOGLE";
	}

	@Override
	public Date findModificationDateImpl(String urlStr) throws SearchException {
		// TODO Auto-generated method stub
		return null;
	}
}
