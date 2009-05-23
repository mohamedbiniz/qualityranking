package br.ufrj.htmlbase;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import br.ufrj.htmlbase.db.hibernate.HibernateSessionFactory;

public class GenerateJung {

	
	/**
	 * Obtem do repositorio a pagina com o maior score,
	 * ainda nao foi extra�do links, e que existe a mais tempo no repositorio
	 * @return
	 */
	Collection<PageCrawler> obterVertices() {
		Criteria c = HibernateSessionFactory.currentSession().createCriteria(PageCrawler.class);
		c.addOrder(Order.asc("ordemDownload"));
		// RSN @SuppressWarnings("unchecked")
		@SuppressWarnings("unchecked")
		Collection<PageCrawler> pages = c.list();
		HibernateSessionFactory.closeSession();
		return pages;
	}

	// RSN determinar o tipo disso: Collection<Object> obterArcs()
	Collection<Object> obterArcs(){
		
		/*
		  SELECT P.URL AS HUBS,  O.URL AS AUTHORITIES,  P.ORDEM_DOWNLOAD, (SELECT P3.ORDEM_DOWNLOAD FROM PAGE P3 WHERE O.URL = P3.URL GROUP BY P3.URL) AS ORDEM_DOWNLOAD_FIM, '1'
		  FROM PAGE P, OUTPUTLINK O
		  WHERE P.ID = O.ID_PAGE
		  	AND O.ID_MD5 IN (SELECT P1.ID FROM PAGE P1)
		  	GROUP BY P.URL, O.URL
ORDER BY P.ORDEM_DOWNLOAD
		 */
		
	//	select p.url as purl, o.url as ourl, p.ordemDownload, (select p3)
		
		return null;
	}
}
