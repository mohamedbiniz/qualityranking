package br.ufrj.htmlbase.extractor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.htmlparser.beans.LinkBean;

import br.ufrj.htmlbase.filter.URLFilter;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Jan 22, 2006 Time: 2:41:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkExtractor {

	private static Logger logger = Logger.getLogger(LinkExtractor.class);

	public static Collection getLinks(String path, String domain, long idDataSet) {
		LinkBean sb = new LinkBean();
		sb.setURL(path);

		URL[] urls = sb.getLinks();

		logger
				.debug("Quantidade de links antes da filtragem :: "
						+ urls.length);

		/*
		 * A extracao dos links Ž realizado no arquivo localmente, sendo assim
		 * todos os links que tinham o dominio de origem se transformam em
		 * localhost, sendo necessario agora substituir localhost pelo dominio,
		 * e o dominio do link(localhost) pelo dominio da pagina
		 */

		for (int i = 0; i < urls.length; i++) {
			URL u = urls[i];
			if (u.toString().contains("file://localhost")) {

				String link = u.toString().replaceFirst("file://localhost",
						"http://" + domain);
				try {

					URL newURL = new URL(link);
					urls[i] = newURL;
				} catch (MalformedURLException ex) {
					logger.fatal(ex.getMessage());
				}
			}
		}

		Collection outLinks = URLFilter.getValidOutputLinks(urls, idDataSet);

		logger.debug("Quantidade de links apos a filtragem :: "
				+ outLinks.size());

		return outLinks;
	}
}
