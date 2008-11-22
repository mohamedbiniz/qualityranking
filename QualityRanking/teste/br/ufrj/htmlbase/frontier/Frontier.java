/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 15, 2006
 * Time: 10:23:44 PM
 * To change this template use File | Settings | File Templates.
 */
package br.ufrj.htmlbase.frontier;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.ufrj.cos.bri.bean.DataSet;
import br.ufrj.cos.bri.db.HibernateDAO;
import br.ufrj.htmlbase.OutputLinkCrawler;
import br.ufrj.htmlbase.PageCrawler;
import br.ufrj.htmlbase.db.FactoryBD;
import br.ufrj.htmlbase.db.PageBD;
import br.ufrj.htmlbase.exception.HtmlBaseException;
import br.ufrj.htmlbase.util.HtmlBaseConfProvider;

public class Frontier {

	private static Logger logger = Logger.getLogger(Frontier.class);

	private static Frontier ourInstance = null;

	private Collection lista = new ArrayList();
	private Collection listaUpdate = new ArrayList();

	public static synchronized Frontier getInstance() {

		if (ourInstance == null)
			ourInstance = new Frontier();

		return ourInstance;
	}

	private Frontier() {
	}

	private synchronized Collection getOutputLinkList() {

		PageBD bd = FactoryBD.getInstance().createPage();

		Collection links = null;
		try {
			HtmlBaseConfProvider provider = HtmlBaseConfProvider.getInstance();

			links = bd.getLinksTop(provider.getTopLinks());
		} catch (SQLException e) {
			logger.fatal(e);
		}

		return links;

	}

	public synchronized OutputLinkCrawler getNextURL() throws HtmlBaseException {

		// TODO precisa melhorar isso, falha caso a tabela de links fique vazia
		if (!lista.iterator().hasNext()) {

			sendListUpdate();

			lista = getOutputLinkList();

			if (lista.isEmpty())
				throw new HtmlBaseException(new Date()
						+ " ===== Nao existem mais links para download ====");

		}

		OutputLinkCrawler o = (OutputLinkCrawler) lista.iterator().next();

		// String url = o.getUrl();

		listaUpdate.add(new Long(o.getIdTest()));

		lista.remove(o);

		if (o.getUrl().contains(PageCrawler.TEMP_PATH)
				|| o.getUrl().endsWith(".pdf") || o.getUrl().contains("?"))
			return getNextURL();
		else
			return o;
	}

	public void sendListUpdate() {

		if (!listaUpdate.isEmpty()) {

			PageBD bd = FactoryBD.getInstance().createPage();

			try {
				bd.updateLinks(listaUpdate);
				listaUpdate.clear();

			} catch (java.sql.BatchUpdateException bue) {
				logger
						.debug(new java.util.Date()
								+ " Tentativa de inserção de uma página com mesmo id MD5");
			} catch (SQLException e) {
				logger.fatal(e);
			}
		}

	}

	public void destroier() {
		ourInstance = null;

	}

	public void testeFim(DataSet dataSet) throws HtmlBaseException {

		HibernateDAO dao = HibernateDAO.getInstance();
		int min = HtmlBaseConfProvider.getInstance().getNumeroMaximoPaginas(
				dataSet);

		List<?> lista = dao.listAll("PAGE_CRAWLER");
		if (lista.size() > min) {
			throw new HtmlBaseException(new Date()
					+ " ===== Quantidade de páginas já atingiu o limite ====");

		}

	}
}
