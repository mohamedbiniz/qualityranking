/**
 * 
 */
package br.ufrj.cos.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.cos.matlab.JobSend;
import br.ufrj.cos.matlab.client.MatClient;

/**
 * @author Fabricio
 * 
 */
public abstract class Service extends Thread {

	private static final char CHAR_SPACE = ' ';

	private static HibernateDAO dao;

	private long pauseTime;

	private char dataSetInitStatus;

	private char dataSetEndStatus;

	private char dataSetMethod;

	public Service(char dataSetInitStatus, char dataSetEndStatus, long pauseTime) {

		this(dataSetInitStatus, dataSetEndStatus, CHAR_SPACE, pauseTime);
	}

	public Service(char dataSetInitStatus, char dataSetEndStatus,
			char dataSetMethod, long pauseTime) {

		setDataSetInitStatus(dataSetInitStatus);
		setDataSetEndStatus(dataSetEndStatus);
		setDataSetMethod(dataSetMethod);
		setPauseTime(pauseTime);
		setDao(HibernateDAO.getInstance());
		// setSession(getDao().getSession());
	}

	@Override
	public final void run() {
		// super.run();
		Session session = null;
		try {
			while (true) {
				synchronized (dao) {
					session = getDao().openSession();
					// DataSet dataSet = getDataSetTo();
					DataSet dataSet = getDataSetTo(1);
					if (dataSet != null) {
						// getDao().update(dataSet);
						execute(dataSet);
						dataSet.setStatus(getDataSetEndStatus());
						getDao().update(dataSet);
						getDao().closeSession();
					}
					if (session != null)
						getDao().closeSession();
				}
				System.gc();
				try {
					sleep(getPauseTime());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null)
				getDao().closeSession();
		}
	}

	protected abstract void execute(DataSet dataSet) throws Exception;

	/**
	 * @param i
	 * @param session
	 * @param dataSet
	 * @return
	 */
	private final DataSet getDataSetTo() {
		DataSet dataSet = null;

		Criteria crit = getDao().openSession().createCriteria(DataSet.class);
		crit.add(Restrictions.eq("status", getDataSetInitStatus()));
		if (getDataSetMethod() != CHAR_SPACE) {
			crit.add(Restrictions.eq("method", getDataSetMethod()));
		}
		crit.add(Restrictions.eq("crawler", true));
		crit.addOrder(Order.asc("creationDate"));
		crit.setMaxResults(1);
		List<DataSet> dataSets = crit.list();
		if (!dataSets.isEmpty())
			dataSet = dataSets.get(0);
		System.out.println(dataSets.size());
		return dataSet;
	}

	public DataSet getDataSetTo(int max) {

		Transaction tx = null;
		List<DataSet> result = null;
		try {
			tx = getDao().openSession().beginTransaction();
			String queryStr;
			Query q = getDao().getSession().createQuery(getQuerySql());
			q.setMaxResults(max);
			result = q.list();
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null)
				tx.rollback();
			throw he;
		}
		if (result == null || result.isEmpty())
			return null;
		return result.get(0);

	}

	private String getQuerySql() {
		String restricionMethod = "";
		if (getDataSetMethod() != CHAR_SPACE) {
			restricionMethod = String.format("and method='%c' ",
					getDataSetMethod());
		}
		String query = String.format("from DataSet "
				+ "where status='%c' and crawler=1 " + restricionMethod
				+ "order by creation_datetime", getDataSetInitStatus());
		return query;
	}

	/**
	 * @return the pauseTime
	 */
	public final long getPauseTime() {
		return pauseTime;
	}

	/**
	 * @param pauseTime
	 *            the pauseTime to set
	 */
	public final void setPauseTime(long pauseTime) {
		this.pauseTime = pauseTime;
	}

	/**
	 * @return the dataSetStatus
	 */
	public final char getDataSetInitStatus() {
		return dataSetInitStatus;
	}

	/**
	 * @param dataSetStatus
	 *            the dataSetStatus to set
	 */
	public final void setDataSetInitStatus(char dataSetInitStatus) {
		this.dataSetInitStatus = dataSetInitStatus;
	}

	/**
	 * @return the dao
	 */
	public static HibernateDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(HibernateDAO dao) {
		this.dao = dao;
	}

	/**
	 * @return the dataSetEndStatus
	 */
	public char getDataSetEndStatus() {
		return dataSetEndStatus;
	}

	/**
	 * @param dataSetEndStatus
	 *            the dataSetEndStatus to set
	 */
	public void setDataSetEndStatus(char dataSetEndStatus) {
		this.dataSetEndStatus = dataSetEndStatus;
	}

	protected void fuzzy(DataSet dataSet) throws Exception {
		Collection<ContextQualityDimensionWeight> listCQDWeights = HelperAcessDB
				.loadContextQualityDimensionWeights(dataSet);

		int qtdQualityDimensions = HelperAcessDB.loadQualityDimensions(dataSet,
				listCQDWeights).size();
		double contextWeights[] = HelperAcessDB.getWeights(listCQDWeights);

		Collection<Document> documents = HelperAcessDB.loadDocuments(dataSet);
		for (Document document : documents) {

			double[] qds = HelperAcessDB
					.loadDocumentQualityDimensionScores(document);

			JobSend jobSend = new JobSend("fuzzyDocument",
					qtdQualityDimensions, contextWeights, qds);
			MatClient c = null;
			double documentScore = 0;
			try {
				c = MatClient.getInstance();
				documentScore = c.createJob(jobSend);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

			}
			document.setScore(new BigDecimal(documentScore));
			getDao().update(document);
		}
	}

	/**
	 * @return the dataSetMethod
	 */
	public char getDataSetMethod() {
		return dataSetMethod;
	}

	/**
	 * @param dataSetMethod
	 *            the dataSetMethod to set
	 */
	public void setDataSetMethod(char dataSetMethod) {
		this.dataSetMethod = dataSetMethod;
	}

}
