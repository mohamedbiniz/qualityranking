/**
 * 
 */
package br.ufrj.cos.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.GraphInstance;
import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.Metadata;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.HibernateDAO;
import br.ufrj.cos.enume.MetadataType;
import br.ufrj.cos.matlab.JobSend;
import br.ufrj.cos.matlab.client.MatClient;
import br.ufrj.cos.matlab.exception.MatLabException;
import br.ufrj.cos.services.process.MetadataExtract;
import edu.uci.ics.jung.algorithms.importance.AbstractRanker;
import edu.uci.ics.jung.algorithms.importance.HITS;
import edu.uci.ics.jung.algorithms.importance.NodeRanking;
import edu.uci.ics.jung.algorithms.importance.PageRank;
import edu.uci.ics.jung.algorithms.transformation.DirectionTransformer;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;

/**
 * @author Fabricio
 * 
 */
public abstract class Service extends Thread {

	private static final String PAJEK_FILE_NAME_FORMAT = "pajek%s.txt";

	private static final char CHAR_SPACE = ' ';

	private static HibernateDAO dao;

	private long pauseTime;

	private char dataSetInitStatus;

	private char dataSetEndStatus;

	private char dataSetMethod;

	private Date now = null;

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
		String query = String.format("from DataSet " + "where status='%c' "
				+ restricionMethod + "order by creation_datetime",
				getDataSetInitStatus());
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

	protected void derivacaoMetadados(DataSet dataSet) throws Exception {
		DocumentQualityDimension documentQualityDimension = null;
		long diff = generatePajekFormat(dataSet);
		HashMap<String, HashMap<Long, Double>> scoresHubAutority = generateJungScores(
				dataSet, diff);
		Collection<Document> documents = HelperAcessDB.loadDocuments(dataSet);
		Collection<QualityDimension> qualityDimensions = HelperAcessDB
				.loadQualityDimensions(dataSet);
		setNow(new Date());
		for (Document document : documents) {
			System.gc();
			for (QualityDimension qualityDimension : qualityDimensions) {
				documentQualityDimension = new DocumentQualityDimension();
				documentQualityDimension.setDocument(document);
				documentQualityDimension.setQualityDimension(qualityDimension);

				double score = 0;
				String code = qualityDimension.getCodeStr();
				if (code.equals(QualityDimension.COM)) {
					score = getCompleteness(document, scoresHubAutority);
				} else if (code.equals(QualityDimension.REP)) {
					score = getReputation(document, scoresHubAutority);
				} else if (code.equals(QualityDimension.TIM)) {
					score = getTimeliness(document);
				}
				documentQualityDimension.setScore(new BigDecimal(score));
				getDao().create(documentQualityDimension);
			}

		}

	}

	/**
	 * @param document
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws Exception
	 */
	protected void extractMetadatas(Document document)
			throws MalformedURLException, IOException, Exception {
		MetadataExtract metadataExtract = new MetadataExtract(document.getUrl());
		HashMap<MetadataType, byte[]> listMetadatas = metadataExtract.extract();
		for (MetadataType metadataType : listMetadatas.keySet()) {
			System.gc();
			byte[] valueMetadata = listMetadatas.get(metadataType);
			Metadata metadata = new Metadata();
			metadata.setDocument(document);
			metadata.setType(metadataType);
			metadata.setValue(valueMetadata);
			getDao().create(metadata);
		}
	}

	private long generatePajekFormat(DataSet dataSet) throws SQLException,
			IOException {
		long diffIdOfDocuments = getMinIdDocument(dataSet) - 1;
		List<Document> documents = HelperAcessDB.loadDocuments(dataSet);
		PrintWriter writer = new PrintWriter(new FileWriter(String.format(
				PAJEK_FILE_NAME_FORMAT, dataSet.getId())));
		writer.println(String.format("*Vertices %d", documents.size()));
		for (Document document : documents) {
			writer.println(String.format("%d \"%s\"", document.getId()
					- diffIdOfDocuments, document.getUrl()));
		}
		writer.println("*Arcs");
		goTree(dataSet, writer, diffIdOfDocuments);
		writer.close();
		return diffIdOfDocuments;
	}

	private void goTree(DataSet dataSet, PrintWriter writer,
			long diffIdOfDocuments) {
		List<Document> rootDocuments = HelperAcessDB.findRootDocuments(dataSet);
		for (Document rootDocument : rootDocuments) {
			toVisitDocument(rootDocument, writer, diffIdOfDocuments);
		}
	}

	private void toVisitDocument(Document fatherDocument, PrintWriter writer,
			long diffIdOfDocuments) {
		int pesoArco = 1;
		List<Document> childDocuments = HelperAcessDB
				.loadDocumentsByFather(fatherDocument);
		for (Document childDocument : childDocuments) {
			System.gc();
			writer.println(String.format("%d %d %d", fatherDocument.getId()
					- diffIdOfDocuments, childDocument.getId()
					- diffIdOfDocuments, pesoArco));
			toVisitDocument(childDocument, writer, diffIdOfDocuments);
		}

	}

	private long getMinIdDocument(DataSet dataSet) {
		List<Document> result = null;
		try {
			Query q = getDao().openSession().createQuery(
					String.format(
							"from Document where dataSet_id=? order by id",
							getDataSetInitStatus()));
			q.setParameter(0, dataSet.getId());
			q.setMaxResults(1);
			result = q.list();
		} catch (HibernateException he) {
			throw he;
		}
		if (result == null || result.isEmpty())
			return 0;
		return result.get(0).getId();
	}

	private HashMap<String, HashMap<Long, Double>> generateJungScores(
			DataSet dataSet, long diffIdOfDocuments) {
		GraphInstance graphInstance = new GraphInstance();
		Graph graph = graphInstance.load(String.format(PAJEK_FILE_NAME_FORMAT,
				dataSet.getId()));
		// graphInstance.displayGraph(graph);
		// graphInstance.save(graph,"C:/eclipse/workspace/jung/graphTestOut.txt");
		System.out.println(" Ranking usando Authorities ");
		HITS rankerAuthorities = new HITS(graph, true);
		rankerAuthorities.evaluate();
		rankerAuthorities.printRankings(true, true);
		System.out.println(" Ranking usando Hubs ");
		HITS rankerHubs = new HITS(graph, false);
		rankerHubs.evaluate();
		rankerHubs.printRankings(true, true);
		System.out.println(" Ranking usando Pagerank ");
		DirectedGraph directedGraph = new DirectedSparseGraph();
		directedGraph = DirectionTransformer.toDirected(graph);
		// directedGraph= (DirectedSparseGraph)graph;
		PageRank rankerPageRank = new PageRank(directedGraph, 0.15);
		rankerPageRank.evaluate();
		rankerPageRank.printRankings(true, true);
		// try {
		// Thread.sleep(200000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		HashMap<String, HashMap<Long, Double>> result = new HashMap<String, HashMap<Long, Double>>();

		result.put(rankerAuthorities.getRankScoreKey(), getRankingByDocumentId(
				diffIdOfDocuments, rankerAuthorities));

		result.put(rankerHubs.getRankScoreKey(), getRankingByDocumentId(
				diffIdOfDocuments, rankerHubs));

		return result;

	}

	/**
	 * @param diffIdOfDocuments
	 * @param rankerAuthorities
	 * @return
	 */
	private HashMap<Long, Double> getRankingByDocumentId(
			long diffIdOfDocuments, AbstractRanker ranker) {
		List<NodeRanking> listRankings = null;
		HashMap<Long, Double> rankingMap = null;
		rankingMap = new HashMap<Long, Double>();
		listRankings = ranker.getRankings();
		for (NodeRanking nodeRanking : listRankings) {
			rankingMap.put(nodeRanking.originalPos + diffIdOfDocuments,
					nodeRanking.rankScore);

		}
		return rankingMap;
	}

	private double getCompleteness(Document document,
			HashMap<String, HashMap<Long, Double>> scoresHubAutority)
			throws SQLException, IOException {
		String name_ranking = "AUTHORITY";
		return getRankingScore(document, scoresHubAutority, name_ranking);
	}

	private double getReputation(Document document,
			HashMap<String, HashMap<Long, Double>> scoresHubAutority) {
		String name_ranking = "HUB";
		return getRankingScore(document, scoresHubAutority, name_ranking);
	}

	/**
	 * @param document
	 * @param scoresHubAutority
	 * @param name_ranking
	 * @return
	 */
	private double getRankingScore(Document document,
			HashMap<String, HashMap<Long, Double>> scoresHubAutority,
			String name_ranking) {
		double score = 0;
		HashMap<Long, Double> mapRanking = null;
		for (String key : scoresHubAutority.keySet()) {
			if (key.contains(name_ranking)) {
				mapRanking = scoresHubAutority.get(key);
			}
		}

		if ((mapRanking != null)
				&& (!mapRanking.get(document.getId()).equals(Double.NaN)))
			score = mapRanking.get(document.getId());

		return score;
	}

	private double getTimeliness(Document document) {
		double score = 0;
		byte[] metadata = getMetadata(document, MetadataType.DATE);
		if (metadata != null) {
			DateFormat dateFormat = new SimpleDateFormat(
					MetadataExtract.DATE_FORMAT);
			Date lastModified = null;
			try {
				lastModified = dateFormat.parse(new String(metadata));
			} catch (ParseException e) {
				System.err.println(String.format(
						"Este documento (%s) não possui o metadado, "
								+ "ou o mesmo não está no formato correto, "
								+ "para calcular o timeliness", document
								.toString()));
			}
			if (lastModified != null) {
				double diffDates = calcDiffDays(getNow(), lastModified);
				// atualização trimestral
				double quo = (diffDates / (3 * 30)) + 1;
				score = 1 / (quo > 1 ? quo : 1);
			}
		}
		return score;
	}

	private byte[] getMetadata(Document document, MetadataType metadataType) {
		byte[] value = null;
		Metadata metadata = null;
		metadata = HelperAcessDB.loadMetadata(document, metadataType);
		if (metadata != null)
			value = metadata.getValue();
		return value;
	}

	private double calcDiffDays(Date dateEnd, Date dateInit) {
		// Creates two calendars instances
		Calendar calInit = new GregorianCalendar();
		Calendar calEnd = new GregorianCalendar();

		// Set the date for both of the calendar instance
		calInit.setTime(dateInit);
		calEnd.setTime(dateEnd);

		// Get the represented date in milliseconds
		long milisInit = calInit.getTimeInMillis();
		long milisEnd = calEnd.getTimeInMillis();

		// Calculate difference in milliseconds
		double diff = milisEnd - milisInit;

		//
		// // Calculate difference in seconds
		// double diffSeconds = diff / 1000;
		//
		// // Calculate difference in minutes
		// double diffMinutes = diff / (60 * 1000);
		//
		// // Calculate difference in hours
		// double diffHours = diff / (60 * 60 * 1000);

		// Calculate difference in days
		double diffDays = diff / (24 * 60 * 60 * 1000);
		return diffDays;
	}

	public static void fuzzyDataSet(DataSet dataSet) throws Exception {
		Collection<ContextQualityDimensionWeight> listCQDWeights = HelperAcessDB
				.loadContextQualityDimensionWeights(dataSet);

		int qtdQualityDimensions = HelperAcessDB.loadQualityDimensions(dataSet)
				.size();
		double contextWeights[] = HelperAcessDB.getWeights(listCQDWeights);

		Collection<Document> documents = HelperAcessDB.loadDocuments(dataSet);
		for (Document document : documents) {

			double[] qds = HelperAcessDB
					.loadDocumentQualityDimensionScores(document);

			Double documentScore = null;
			while (documentScore == null) {
				try {
					documentScore = fuzzy(qtdQualityDimensions, qds,
							contextWeights);
				} catch (MatLabException mle) {
					System.err.println(mle.getMessage());
					JOptionPane
							.showMessageDialog(
									null,
									"Possivelmente o serviço do MatLab não está iniciado ou houve erro no mesmo!\n"
											+ "É necessário (re)iniciar o serviço do MatLab antes de continuar!",
									"Erro no MatLab",
									JOptionPane.WARNING_MESSAGE);
				}
			}

			document.setScore(new BigDecimal(documentScore.doubleValue()));
			HibernateDAO.getInstance().update(document);
		}

	}

	public static Double fuzzy(int qtdQualityDimensions, double[] qds,
			double[] contextWeights) throws MatLabException {
		JobSend jobSend = new JobSend("fuzzyDocument", qtdQualityDimensions,
				contextWeights, qds);
		MatClient c = null;
		Double documentScore = null;

		try {
			c = MatClient.getInstance();
			documentScore = c.createJob(jobSend);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (documentScore == null) {
				throw new MatLabException(
						"ERRO NO SERVICO DO MATLAB! É PRECISO REINICIAR O SERVICO DO MATLAB!");
			}

		}
		return documentScore;
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

	/**
	 * @return the now
	 */
	private Date getNow() {
		return now;
	}

	/**
	 * @param now
	 *            the now to set
	 */
	private void setNow(Date now) {
		this.now = now;
	}

}
