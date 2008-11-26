/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.util.ArrayList;
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
public class LiveSearch extends SearchEngine {

	@Override
	public List<Result> search(String query) throws SearchException {
		try {
			List<Result> results = new ArrayList<Result>(getMaxResults());

			SOAPCall msnLiveSearch = new SOAPCall(
					"http://soap.search.msn.com/webservices.asmx", "Search");
			SOAPElement bodyElement = msnLiveSearch.getBodyElement();
			SOAPElement req = bodyElement.addChildElement("Request");
			req.addChildElement("AppID").addTextNode(getAppID()).setAttribute(
					"xsi:type", "xsd:string");
			req.addChildElement("Query").addTextNode(query).setAttribute(
					"xsi:type", "xsd:string");
			req.addChildElement("CultureInfo").addTextNode("en-US")
					.setAttribute("xsi:type", "xsd:string");
			req.addChildElement("SafeSearch").addTextNode("Off").setAttribute(
					"xsi:type", "xsd:string");
			req.addChildElement("Flags").addTextNode("None").setAttribute(
					"xsi:type", "xsd:string");
			SOAPElement reqs = req.addChildElement("Requests");
			SOAPElement sourceReq = reqs.addChildElement("SourceRequest");
			sourceReq.addChildElement("Source").addTextNode("Web")
					.setAttribute("xsi:type", "xsd:string");
			sourceReq.addChildElement("Offset").addTextNode("0").setAttribute(
					"xsi:type", "xsd:integer");
			sourceReq.addChildElement("Count").addTextNode(
					Integer.toString(getMaxResults())).setAttribute("xsi:type",
					"xsd:integer");
			sourceReq.addChildElement("ResultFields").addTextNode("All")
					.setAttribute("xsi:type", "xsd:string");
			Document doc = msnLiveSearch.call();

			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList) xpath
					.evaluate(
							"//SearchResponse/Response/Responses/SourceResponse/Results/Result",
							doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Element elem = (Element) nodes.item(i);
				Result result = new Result();
				result.setTitle(elem.getElementsByTagName("Title").item(0)
						.getTextContent());
				result.setURL(elem.getElementsByTagName("Url").item(0)
						.getTextContent());
				NodeList desc = elem.getElementsByTagName("Description");
				if (desc.getLength() > 0)
					result.setSummary(desc.item(0).getTextContent());
				results.add(result);
			}

			return results;
		} catch (Exception se) {
			throw new SearchException("Unable to perform MSN Live search", se);
		}
	}

	@Override
	public String getSearchEngineCode() {
		return "LIVE";
	}
}
