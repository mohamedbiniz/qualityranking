/*
 * PageHibernateImpl.java
 *
 * Created on March 21, 2006, 9:39 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.ufrj.htmlbase.db.hibernate;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.SeedDocument;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.htmlbase.HtmlBase;
import br.ufrj.htmlbase.OutputLinkCrawler;
import br.ufrj.htmlbase.PageCrawler;
import br.ufrj.htmlbase.db.PageBD;
import br.ufrj.htmlbase.extractor.metadata.MetadataCrawler;

/**
 * 
 * @author mayworm
 */
public class PageHibernateImpl implements PageBD {

	private static final String PAGE_CRAWLER = "page_crawler";
	private static final String OUTPUTLINK_CRAWLER = "outputlink_crawler";
	private static final String METADATA_CRAWLER = "METADATA_crawler";
	private static Logger logger = Logger.getLogger(PageHibernateImpl.class);

	private static long idDataSet;
	private static long ordemDownload;

	static {
		idDataSet = 0;
		ordemDownload = 0;
	}

	/** Creates a new instance of PageHibernateImpl */
	public PageHibernateImpl() {
	}

	public void shutdown() throws SQLException {
	}

	public synchronized void insertSeedsLinks(
			Collection<SeedDocument> seedDocuments) throws IOException {
		Session ss = null;
		Transaction tx = null;

		clearCrawlerDB();

		/*
		 * Esse codigo da persistencia de links precisa ser melhorado
		 */

		for (SeedDocument seedDocument : seedDocuments) {

			ss = HibernateSessionFactory.currentSession();
			tx = ss.beginTransaction();

			OutputLinkCrawler link = new OutputLinkCrawler();

			link.setDomain(seedDocument.getDomain());
			link.setUrl(seedDocument.getUrl());
			link.setDataSet(seedDocument.getDataSet());
			link.setVisited(false);
			link.setIdPage(0);
			Date date = new Date();
			link.setDateCreate(date);
			link.setLastModified(date);
			link.setNextFetch(date);
			link.setSeed(true);
			link.setIdTest(HtmlBase.generatePageId(link));

			List result = ss.createCriteria(OutputLinkCrawler.class).add(
					Expression.eq("idTest", link.getIdTest())).add(
					Expression.eq("idPage", link.getIdPage())).list();

			if (result.isEmpty()) {
				ss.save(link);
			}

			ss.flush();

			tx.commit();
			HibernateSessionFactory.closeSession();

		}
		logger.debug(new java.util.Date()
				+ " Links Sementes persistidos com sucesso : "
				+ seedDocuments.size());

	}

	/**
	 * @param ss
	 */
	private void clearCrawlerDB() {
		Session ss = null;
		Transaction tx = null;

		ss = HibernateSessionFactory.currentSession();
		List<OutputLinkCrawler> links = (List<OutputLinkCrawler>) ss
				.createCriteria(OutputLinkCrawler.class).list();
		HibernateSessionFactory.closeSession();

		for (OutputLinkCrawler link : links) {
			ss = HibernateSessionFactory.currentSession();
			tx = ss.beginTransaction();

			ss.delete(link);

			ss.flush();
			tx.commit();
			HibernateSessionFactory.closeSession();
		}

		ss = HibernateSessionFactory.currentSession();
		List<PageCrawler> pages = (List<PageCrawler>) ss.createCriteria(
				PageCrawler.class).list();
		HibernateSessionFactory.closeSession();

		for (PageCrawler page : pages) {
			ss = HibernateSessionFactory.currentSession();
			tx = ss.beginTransaction();

			ss.delete(page);

			ss.flush();
			tx.commit();
			HibernateSessionFactory.closeSession();
		}

		ss = HibernateSessionFactory.currentSession();
		List<MetadataCrawler> metadatas = (List<MetadataCrawler>) ss
				.createCriteria(MetadataCrawler.class).list();
		HibernateSessionFactory.closeSession();

		for (MetadataCrawler metadata : metadatas) {
			ss = HibernateSessionFactory.currentSession();
			tx = ss.beginTransaction();

			ss.delete(metadata);

			ss.flush();
			tx.commit();
			HibernateSessionFactory.closeSession();
		}
	}

	public synchronized Collection getLinksTop(int top) throws SQLException {
		Session session = HibernateSessionFactory.currentSession();

		List result = session.createCriteria(OutputLinkCrawler.class).add(
				Restrictions.eq("visited", false)).addOrder(
				Order.asc("dateCreate")).setMaxResults(top).list();

		HibernateSessionFactory.closeSession();

		logger.debug(new Date()
				+ " ======== Os proximos links a capturar ===========");

		for (Object l : result) {
			OutputLinkCrawler u = (OutputLinkCrawler) l;
			logger.debug(u.getUrl() + " - Foi visitado anteriormente? -> "
					+ u.getVisited());
		}

		logger.debug(new Date()
				+ " =================================================");
		return result;

	}

	public synchronized void save(PageCrawler p, DataSet dataSet)
			throws java.sql.BatchUpdateException {
		if (dataSet.getId() != idDataSet) {
			idDataSet = dataSet.getId();
			ordemDownload = 0;
		}

		try {

			Session ss = HibernateSessionFactory.currentSession();
			Transaction tx = ss.beginTransaction();

			p.setIdDataSet(dataSet.getId());
			p.setOrdemDownload(ordemDownload++);

			ss.saveOrUpdate(p);
			logger.debug(new java.util.Date() + " URL persistida :: "
					+ p.getUrl());

			ss.flush();

			tx.commit();

			HibernateSessionFactory.closeSession();

			Collection urls = p.getUrls();

			/*
			 * Esse codigo da persistencia de links precisa ser melhorado
			 */

			for (Object url : urls) {

				ss = HibernateSessionFactory.currentSession();
				tx = ss.beginTransaction();

				OutputLinkCrawler link = (OutputLinkCrawler) url;
				link.setIdPage(p.getId());

				Criteria criteria = ss.createCriteria(OutputLinkCrawler.class)
						.add(Restrictions.eq("idTest", link.getIdTest()));
				List result = criteria.list();

				// List result = ss.createCriteria(OutputLinkCrawler.class).add(
				// Expression.eq("id", link.getIdTest())).add(
				// Expression.eq("idPage", p.getId())).list();

				if (result.isEmpty()) {

					link.setIdPage(p.getId());
					ss.save(link);

				}
				ss.flush();
				tx.commit();
				HibernateSessionFactory.closeSession();

			}
			logger.debug(new java.util.Date()
					+ " Links persistidos com sucesso : " + urls.size());
			urls = null;

			// Collection metadatas = p.getMetadata().getMetadatas();
			//
			// for (Object metadata : metadatas) {
			//
			// ss = HibernateSessionFactory.currentSession();
			// tx = ss.beginTransaction();
			//
			// MetadataCrawler m = (MetadataCrawler) metadata;
			//
			// Criteria criteria = ss.createCriteria(MetadataCrawler.class)
			// .add(Restrictions.eq("id", m.getId()));
			// List result = criteria.list();
			//
			// // List result = ss.createCriteria(OutputLinkCrawler.class).add(
			// // Expression.eq("id", link.getIdTest())).add(
			// // Expression.eq("idPage", p.getId())).list();
			//
			// if (result.isEmpty()) {
			//
			// m.setIdPage(p.getId());
			// ss.save(m);
			// }
			// try {
			// ss.flush();
			// tx.commit();
			// } catch (Exception e) {
			// tx.rollback();
			// // e.printStackTrace();
			// } finally {
			// System.out.println(m.getName());
			// System.out.println(m.getValue());
			// }
			//
			// HibernateSessionFactory.closeSession();
			// }
			// logger.debug(new java.util.Date()
			// + " Metadados persistidos com sucesso : "
			// + metadatas.size());
			// metadatas = null;

		} catch (org.hibernate.exception.ConstraintViolationException cve) {
			logger.debug(new java.util.Date()
					+ " Tentativa de inserção de uma página com mesmo id MD5");
		} catch (HibernateException e) {
			e.printStackTrace();
			logger.fatal(e);

		}
	}

	public static synchronized void update(Object obj) {

		Session session = HibernateSessionFactory.currentSession();
		Transaction tx = session.beginTransaction();

		session.update(obj);

		session.flush();
		tx.commit();

		HibernateSessionFactory.closeSession();

	}

	public synchronized void updateLinks(Collection c, DataSet dataSet)
			throws java.sql.BatchUpdateException {

		// if (dataSet.getId() != idDataSet) {
		// idDataSet = dataSet.getId();
		// ordemDownload = 0;
		// }

		try {
			Session session = HibernateSessionFactory.currentSession();
			Transaction tx = session.beginTransaction();
			logger.debug("=============== Links Visitados =================");

			for (Object url : c) {
				System.gc();
				List result = session.createCriteria(OutputLinkCrawler.class)
						.add(Expression.eq("idTest", url)).list();

				for (int i = 0; i < result.size(); i++) {

					OutputLinkCrawler u = (OutputLinkCrawler) result.get(i);
					u.setVisited(true);
					// u.setOrdemDownload(ordemDownload++);
					session.update(u);

					logger.debug(u.getUrl() + " - Foi visitado -> "
							+ u.getVisited());

				}
			}
			logger.debug("=================================================");

			session.flush();
			tx.commit();

			HibernateSessionFactory.closeSession();

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

	}

	public static Object loadById(Class<?> klass, Serializable id) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = ss.beginTransaction();
		Object obj = null;
		try {
			obj = ss.load(klass, id, LockMode.READ);
		} catch (ObjectNotFoundException e) {
			// nothing
		}
		ss.flush();

		tx.commit();
		HibernateSessionFactory.closeSession();
		return obj;
	}

	public Collection<Document> exportPages(DataSet dataSet, HibernateDAO dao)
			throws Exception {

		long maxDownload = maxPropertyValue("Page_Crawler", "ordem_download");

		Collection<PageCrawler> pages = new ArrayList<PageCrawler>();
		Collection<Document> documents = new ArrayList<Document>();
		HashMap<Long, Document> documentsMap = new HashMap<Long, Document>();

		for (long i = 0; i <= maxDownload; i++) {
			System.gc();
			List<?> list = loadByFields(PageCrawler.class, "ordemDownload", i,
					"idDataSet", dataSet.getId());
			if (!list.isEmpty()) {
				PageCrawler pageCrawler = (PageCrawler) list.get(0);
				pages.add(pageCrawler);
				Document document = new Document();
				document.setDataSet(dataSet);
				document.setUrl(pageCrawler.getUrl());
				dao.create(document);
				documentsMap.put(pageCrawler.getId(), document);
				documents.add(document);
			}
		}
		// Collection<PageCrawler> pages = (Collection<PageCrawler>)
		// listAllOrderBy(
		// PageCrawler.class, "ordemDownload");
		// Collection<Document> documents = new ArrayList<Document>();
		// HashMap<Long, Document> documentsMap = new HashMap<Long, Document>();
		// for (PageCrawler pageCrawler : pages) {
		// Document document = new Document();
		// document.setDataSet(dataSet);
		// document.setUrl(pageCrawler.getUrl());
		// dao.create(document);
		// documentsMap.put(pageCrawler.getId(), document);
		// documents.add(document);
		// }

		updateLinks(documentsMap, pages, dao);

		return documents;
	}

	private void updateLinks(HashMap<Long, Document> documentsMap,
			Collection<PageCrawler> pages, HibernateDAO dao) throws Exception {
		for (PageCrawler pageCrawler : pages) {
			System.gc();
			Document document = documentsMap.get(pageCrawler.getId());
			Document father = documentsMap.get(pageCrawler.getIdPage());
			if (father != null)
				document.addFatherDocument(father);
			dao.update(document);
		}
	}

	public List<?> loadByFields(Class<?> klass, String fieldName1,
			Object value1, String fieldName2, Object value2) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = ss.beginTransaction();

		Criteria criteria = ss.createCriteria(klass).add(
				Restrictions.eq(fieldName1, value1)).add(
				Restrictions.eq(fieldName2, value2));
		List<?> listResult = criteria.list();

		ss.flush();
		tx.commit();
		HibernateSessionFactory.closeSession();
		return listResult;

	}

	public Collection<?> loadByField(Class<?> klass, String fieldName,
			Object value) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = ss.beginTransaction();

		Criteria criteria = ss.createCriteria(klass).add(
				Restrictions.eq(fieldName, value));
		Collection<?> listResult = criteria.list();

		ss.flush();
		tx.commit();
		HibernateSessionFactory.closeSession();
		return listResult;
	}

	public Collection<?> listAllOrderBy(Class<?> klass, String orderPropertyName) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = ss.beginTransaction();

		Criteria criteria = ss.createCriteria(klass).addOrder(
				Order.asc(orderPropertyName));
		Collection<?> listResult = criteria.list();

		ss.flush();
		tx.commit();
		HibernateSessionFactory.closeSession();
		return listResult;
	}

	public Collection<?> listAll(Class<?> klass) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = ss.beginTransaction();

		Criteria criteria = ss.createCriteria(klass);
		Collection<?> listResult = criteria.list();

		ss.flush();
		tx.commit();
		HibernateSessionFactory.closeSession();
		return listResult;
	}

	public int countAll(String tableName) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = ss.beginTransaction();

		String queryString = String.format("SELECT count(*) FROM %s;",
				tableName);
		SQLQuery sqlQuery = ss.createSQLQuery(queryString);
		return ((BigInteger) sqlQuery.uniqueResult()).intValue();
	}

	public long maxPropertyValue(String tableName, String propertyName) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = ss.beginTransaction();

		String queryString = String.format("SELECT max(%s) FROM %s;",
				propertyName, tableName);
		SQLQuery sqlQuery = ss.createSQLQuery(queryString);
		return ((BigInteger) sqlQuery.uniqueResult()).longValue();
	}

	public static List<OutputLinkCrawler> loadFathers(OutputLinkCrawler link) {
		Session ss = HibernateSessionFactory.currentSession();
		Transaction tx = null;
		List<OutputLinkCrawler> links = new ArrayList<OutputLinkCrawler>();
		try {
			tx = ss.beginTransaction();
			Criteria criteria = ss.createCriteria(OutputLinkCrawler.class).add(
					Restrictions.eq("idTest", link.getIdPage())).setMaxResults(
					1);
			links = criteria.list();
		} catch (HibernateException he) {
			if (tx != null)
				tx.rollback();
			throw he;
		}
		return links;
	}

}
