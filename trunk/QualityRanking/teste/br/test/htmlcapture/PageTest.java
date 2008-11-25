package br.test.htmlcapture;

import java.io.IOException;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.htmlbase.OutputLinkCrawler;
import br.ufrj.htmlbase.PageCrawler;
import br.ufrj.htmlbase.db.FactoryBD;
import br.ufrj.htmlbase.db.PageBD;
import br.ufrj.htmlbase.frontier.Frontier;
import br.ufrj.htmlbase.util.HtmlBaseConfProvider;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Feb 3, 2006 Time: 5:41:36 PM To
 * change this template use File | Settings | File Templates.
 */
public class PageTest extends TestCase {

	private static Logger logger;
	DataSet dataSet;

	public PageTest(DataSet dataSet) {
		logger = Logger.getLogger(PageTest.class);
		this.dataSet = dataSet;
	}

	public void testGetPage() {

		int numeroMaximoPaginas = HtmlBaseConfProvider.getInstance()
				.getNumeroMaximoPaginas(dataSet);

		logger.debug(new Date() + " Numero maximo de paginas para download :: "
				+ numeroMaximoPaginas);
		int i;
		for (i = 0; i < numeroMaximoPaginas; i++) {
			OutputLinkCrawler url = null;

			try {
				url = Frontier.getInstance().getNextURL(dataSet);// "http://www.aprendendoingles.com.br/ebooks/";
			} catch (Exception e) {
				e.printStackTrace(); // To change body of catch statement use
				// File | Settings | File Templates.
			}
			PageCrawler p = null;
			try {
				p = new PageCrawler(url);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

			if (p.process()) {

				try {
					PageBD bd = FactoryBD.getInstance().createPage();

					bd.save(p, dataSet);
					bd.shutdown();

				} catch (java.sql.BatchUpdateException bue) {
					logger
							.debug(new java.util.Date()
									+ " Tentativa de inserção de uma página com mesmo id MD5");
				} catch (Exception e) {
					e.printStackTrace(); // To change body of catch statement
					// use File | Settings | File
					// Templates.
				}
			} else {
				if (i > 0)
					i--;

			}

			logger.debug(new Date()
					+ " Foi realizado o acesso a URL de numero :: " + (i + 1));
		}
	}

}
