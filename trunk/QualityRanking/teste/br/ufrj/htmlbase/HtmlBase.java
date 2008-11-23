package br.ufrj.htmlbase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import br.ufrj.cos.bri.bean.DataSet;
import br.ufrj.cos.bri.services.ServiceCrawler;
import br.ufrj.htmlbase.db.FactoryBD;
import br.ufrj.htmlbase.db.PageBD;
import br.ufrj.htmlbase.db.hibernate.PageHibernateImpl;
import br.ufrj.htmlbase.exception.HtmlBaseException;
import br.ufrj.htmlbase.frontier.Frontier;
import br.ufrj.htmlbase.util.HtmlBaseConfProvider;

/**
 * Created by IntelliJ IDEA. User: mayworm Date: Feb 2, 2006 Time: 3:11:33 PM To
 * change this template use File | Settings | File Templates.
 */

public class HtmlBase extends Thread {

	private static int qtdThreads;
	private static Logger logger = Logger.getLogger(HtmlBase.class);

	private int workerNumber;
	private DataSet dataSet;

	private ServiceCrawler serviceCrawler;

	static {
		qtdThreads = 0;
	}

	public HtmlBase(int number, DataSet dataSet) {
		workerNumber = number;
		this.dataSet = dataSet;
		qtdThreads++;
	}

	public HtmlBase(int number, DataSet dataSet, ServiceCrawler serviceCrawler) {
		this(number, dataSet);
		this.serviceCrawler = serviceCrawler;
	}

	// @TODO extrair esse metodo para algum action de um botao
	public void run() {

		try {

			int numeroMaximoPaginas = HtmlBaseConfProvider.getInstance()
					.getNumeroMaximoPaginas(dataSet);

			logger.debug(new Date() + " - Thread " + workerNumber
					+ " - Numero maximo de paginas para download :: "
					+ numeroMaximoPaginas);

			int i = 0;
			int qtdPage = 0;
			PageHibernateImpl pageDao = new PageHibernateImpl();

			// for (i = 0; i < numeroMaximoPaginas; i++) {
			qtdPage = pageDao.countAll("Page_Crawler");
			while (qtdPage < numeroMaximoPaginas) {

				OutputLinkCrawler link = Frontier.getInstance().getNextURL(
						dataSet);
				PageCrawler page = new PageCrawler(link);
				System.gc();
				if (page.process()) {

					PageBD dao = FactoryBD.getInstance().createPage();
					dao.save(page, dataSet);
					dao.shutdown();
				}
				// else {
				// if (i > 0)
				// i--;
				// }

				logger.debug(new Date() + " - Thread " + workerNumber
						+ " - Executando o download de numero :: " + ++i);

				qtdPage = pageDao.countAll("Page_Crawler");
				System.gc();
			}

			// }

		} catch (org.hibernate.exception.ConstraintViolationException cve) {
			logger.debug(new java.util.Date()
					+ " Tentativa de inserção de uma página com mesmo id MD5");
		} catch (java.sql.BatchUpdateException bue) {
			logger.debug(new java.util.Date()
					+ " Tentativa de inserção de uma página com mesmo id MD5");
		} catch (HtmlBaseException e) {
			logger.fatal("****************************");
			logger.fatal("Erro o ocorrido em HtmlBase :: " + e.getMessage());
			logger.fatal("****************************");
		} catch (SQLException e) {
			logger.fatal("****************************");
			logger.fatal("Erro o ocorrido em HtmlBase :: " + e.getMessage());
			logger.fatal("****************************");
		} catch (IOException e) {
			logger.fatal("****************************");
			logger.fatal("Erro o ocorrido em HtmlBase :: " + e.getMessage());
			logger.fatal("****************************");
		} finally {
			logger
					.debug(new Date()
							+ " Inicio da execucao para atualizar ultimos links em memoria ");

			Frontier frontier = Frontier.getInstance();
			frontier.sendListUpdate(dataSet);
			synchronized (this) {
				if (qtdThreads > 0) {
					qtdThreads--;
				}
				if (qtdThreads == 0 && serviceCrawler != null) {
					serviceCrawler.setPausado(false);
				}
			}

		}

	}
}
