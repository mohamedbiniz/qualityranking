/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class GoogleWebSearch extends SearchEngine {

	@Override
	public List<Result> search(String query) throws SearchException {
		try {
			List<Result> results = new ArrayList<Result>(getMaxResults());
			int start = 0;
			boolean hasNext = false;
			do {
				WebFile wf = new WebFile("http://www.google.com/search?q="
						+ URLEncoder.encode(query, "utf-8") + "&hl=en&start=" + start);
				start += 10;
				Pattern pNext = Pattern.compile(Pattern
						.quote("<img src=\"nav_next.gif\""));
				Matcher mNext = pNext.matcher((String) wf.getContent());
				hasNext = mNext.find();

				Pattern p = Pattern.compile(Pattern.quote("<li class=g")
						+ "(?:.+?)" + Pattern.quote("<a href=\"") + "("
						+ Pattern.quote("http://") + "[^" + Pattern.quote("\"")
						+ "]+?)" + Pattern.quote("\" class=l>") + "(.+?)"
						+ Pattern.quote("</a>") + "(?:.+?)"
						+ Pattern.quote("<div class=\"s") + "(?:.+?)\\>(.+?)"
						+ Pattern.quote("<br>"));
				Matcher m = p.matcher((String) wf.getContent());
				while (m.find()) {
					Result r = new Result();
					r.setTitle(m.group(2).replaceAll("\\<(.+?)\\>", ""));
					r.setURL(m.group(1));
					r.setSummary(m.group(3).replaceAll("\\<(.+?)\\>", ""));
					results.add(r);
					// Fabrício, pode tirar esse print!!
					System.out.println(results.size() + ": " + r.getURL());
					if (results.size() == getMaxResults()) {
						hasNext = false;
						break;
					}
				}
			} while (hasNext);
			return results;
		} catch (Exception se) {
			throw new SearchException("Unable to perform Google search", se);
		}
	}

	@Override
	public String getSearchEngineCode() {
		return "GOOGLE";
	}
}
