package br.ufrj.htmlbase.filter;

import java.net.URL;
import java.util.Collection;
import java.util.Hashtable;

import br.ufrj.htmlbase.OutputLinkCrawler;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Feb 2, 2006 Time: 3:34:29 PM To
 * change this template use File | Settings | File Templates.
 */
public class URLFilter {

	public static Collection getValidOutputLinks(URL[] urls, long idDataSet) {

		Hashtable outLinksHash = new Hashtable();

		for (URL u : urls) {

			OutputLinkCrawler l = new OutputLinkCrawler(u, idDataSet);

			outLinksHash.put(l.hashCode(), l);
		}

		Collection<OutputLinkCrawler> uniqueOutLinks = outLinksHash.values();

		// @todo disponibilizar configuracao de filtros atraves de telas de
		// configuracao

		uniqueOutLinks = filterUrls(uniqueOutLinks);

		return uniqueOutLinks;
	}

	private static Collection filterUrls(Collection<OutputLinkCrawler> uniqueOutLinks) {

		ValidFilters filter1 = new ValidFilterDomain();

		ValidFilters filter2 = new ValidFilterImage();

		filter1.setSucessor(filter2);

		return filter1.handleRequest(uniqueOutLinks);

	}

}
