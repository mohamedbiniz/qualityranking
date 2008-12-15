/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

import java.io.IOException;
import java.util.List;

import br.ufrj.cos.foxset.search.SearchEngine.Result;

/**
 * 
 * @author Heraldo
 */
public class Main {

	public static void main(String[] args) throws SearchException, IOException {
		System
				.setProperty("javax.xml.parsers.DocumentBuilderFactory",
						"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");

		for (int engine = 0; engine < 1; engine++) {
			SearchEngine se = null;
			if (engine == 0) {
				se = new GoogleSearch();
				se.setAppID("F4ZdLRNQFHKUvggiU+9+60sA8vc3fohb");
			} else if (engine == 1) {
				se = new YahooSearch();
				se
						.setAppID("j3ANBxbV34FKDH_U3kGw0Jwj5Zbc__TDAYAzRopuJMGa8WBt0mtZlj4n1odUtMR8hco-");
			} else if (engine == 2) {
				se = new LiveSearch();
				se.setAppID("6F4477E8615644FDA81A62E15217B400B121E0A4");
			}
			se.setMaxResults(10);
			List<Result> results = se.search("heraldo");
			for (Result r : results) {
				System.out.println("Engine: "
						+ String.valueOf(se.getSearchEngineCode()));
				System.out.println("Title: " + r.getTitle());
				System.out.println("URL: " + r.getURL());
				System.out.println("Summary: " + r.getSummary());
				System.out.println();
			}
		}

		// WebFile wf = new
		// WebFile("http://en.wikipedia.org/wiki/Heraldo_Munoz");
		// Map<String, Integer> links = wf.getForwardLinks();
		// System.out.println("Forward links para " + wf.getURL() + ":");
		// for (String url : links.keySet()) {
		// System.out.println(url);
		// }
	}
}
