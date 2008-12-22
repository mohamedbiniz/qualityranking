package br.ufrj.htmlbase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.cos.services.ServiceCrawler;
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
				if (!agrupa(link)) {
					PageCrawler page = new PageCrawler(link);
					System.gc();
					if (page.process()) {

						PageBD dao = FactoryBD.getInstance().createPage();
						dao.save(page, dataSet);
						dao.shutdown();
					}
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

	private boolean agrupa(OutputLinkCrawler link) throws IOException {

		LinkedList<OutputLinkCrawler> antepassados = findAllFathers(link);

		for (OutputLinkCrawler antepassado : antepassados) {
			if (isPersistedInPage(antepassado)) {
				if (antepassado.getDomain().equalsIgnoreCase(link.getDomain())) {

					long id = generatePageId(antepassado);
					PageCrawler page = (PageCrawler) PageHibernateImpl
							.loadById(PageCrawler.class, id);

					page.addAgregatedLink(link);

					PageHibernateImpl.update(page);
					return true;
				} else {
					link.setIdPage(generatePageId(antepassado));
					PageHibernateImpl.update(link);
					return false;
				}
			}
		}

		return false;
	}

	private long generatePageId(OutputLinkCrawler antepassado)
			throws IOException {
		return (new PageCrawler(antepassado, false)).getId();
	}

	private boolean isPersistedInPage(OutputLinkCrawler antepassado)
			throws IOException {
		long id = generatePageId(antepassado);

		Criteria criteria = HibernateDAO.getInstance().openSession()
				.createCriteria(PageCrawler.class).add(
						Restrictions.eq("id", id));

		if (criteria.list().isEmpty()) {
			return false;

		}
		return true;
	}

	private LinkedList<OutputLinkCrawler> findAllFathers(OutputLinkCrawler link) {
		LinkedList<OutputLinkCrawler> antepassados = new LinkedList<OutputLinkCrawler>();
		Criteria criteria = HibernateDAO.getInstance().openSession()
				.createCriteria(OutputLinkCrawler.class).add(
						Restrictions.eq("idTest", link.getIdPage()))
				.setMaxResults(1);
		antepassados.add((OutputLinkCrawler) criteria.uniqueResult());
		for (OutputLinkCrawler linkAntepassado : antepassados) {
			antepassados.addAll(findAllFathers(linkAntepassado));
		}
		return antepassados;
	}

}
