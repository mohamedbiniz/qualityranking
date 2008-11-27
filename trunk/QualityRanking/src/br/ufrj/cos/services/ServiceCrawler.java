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

import org.hibernate.HibernateException;
import org.hibernate.Query;

import br.ufrj.cos.GraphInstance;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.Metadata;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.enume.MetadataType;
import br.ufrj.cos.services.process.MetadataExtract;
import br.ufrj.htmlbase.Capture;
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
public class ServiceCrawler extends Service {

	private static final String PAJEK_FILE_NAME_FORMAT = "pajek%s.txt";

	private static final long PAUSA_CRAWLER = 30000;

	Capture crawler;

	private boolean pausado;

	private int qtdSubThread = 10;

	private Date now = null;

	public ServiceCrawler() {
		// super(DataSet.STATUS_CRAWLING, DataSet.STATUS_AUTOMATIC_EVALUATION,
		// PAUSA_CRAWLER);
		super(DataSet.STATUS_CRAWLING, DataSet.STATUS_MANUAL_EVALUATION,
				PAUSA_CRAWLER);
		crawler = new Capture();
	}

	@Override
	protected void execute(DataSet dataSet) throws Exception {

		crawler.execute(dataSet, this, qtdSubThread);
		setPausado(true);
		verificaPausa();
		Collection<Document> documents = crawler.exportPages(dataSet, getDao());
		extractMetadatasOfDocuments(documents);
		derivacaoMetadados(dataSet);
		fuzzy(dataSet);
	}

	private void extractMetadatasOfDocuments(Collection<Document> documents)
			throws Exception {
		for (Document document : documents) {
			extractMetadatas(document);
		}
	}

	/**
	 * @param document
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws Exception
	 */
	static void extractMetadatas(Document document)
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

	private void derivacaoMetadados(DataSet dataSet) throws Exception {
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
				String code = new String(qualityDimension.getCode());
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

		if (mapRanking != null)
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
				double quo = (diffDates + 1);
				score = 1 / (quo > 1 ? quo : 1);
			}
		}
		return score;
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

	private byte[] getMetadata(Document document, MetadataType metadataType) {
		byte[] value = null;
		Metadata metadataExample = new Metadata();
		metadataExample.setDocument(document);
		metadataExample.setType(metadataType);
		List<String> listExcludeParams = new ArrayList<String>();
		listExcludeParams.add("id");
		listExcludeParams.add("value");
		List<Metadata> list = (List<Metadata>) getDao().findByExample(
				metadataExample, listExcludeParams);
		// List<Metadata> list = (List<Metadata>) getDao().loadByField(
		// Metadata.class, "type", metadataType);
		if (!list.isEmpty()) {
			Metadata metadata = list.get(0);
			value = metadata.getValue();
		}
		return value;
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

	/**
	 * Nesse método, verificamos a condição que desejamos. Se a variável pausada
	 * valer true, isso nos indica que a thread deve dormir. Portanto, damos um
	 * wait() nela. Caso contrário, ela deve continuar.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void verificaPausa() throws InterruptedException {
		// Esse while é necessário pois threads estão sujeitas a spurious
		// wakeups, ou seja, elas podem acordar mesmo que nenhum notify tenha
		// sido dado.

		// Whiles diferentes podem ser usados para descrever condições
		// diferentes. Você também pode ter mais de uma condição no while
		// associada com um e. Por exemplo, no caso de um produtor/consumidor,
		// poderia ser while (!pausado && !fila.cheia()).

		// Nesse caso só temos uma condição, que é dormir quando pausado.
		while (pausado) {
			wait();
		}
	}

	/**
	 * Nesse método, permitimos a quem quer que use a impressora que controle
	 * sua thread. Definindo pausado como true, essa thread irá parar e esperar
	 * indefinidamente. Caso pausado seja definido como false, a impressora
	 * volta a imprimir.
	 * 
	 * @param pausado
	 *            True para pausar, false para continuar.
	 */
	public synchronized void setPausado(boolean pausado) {
		// if (pausado == false) {
		// qtdSubThread--;
		// if (qtdSubThread == 0)
		// this.pausado = pausado;
		// } else {
		// qtdSubThread = 4;
		// this.pausado = pausado;
		// }
		this.pausado = pausado;

		// Caso pausado seja definido como false, acordamos a thread e pedimos
		// para ela verificar sua condição. Nesse caso, sabemos que a thread
		// acordará, mas no caso de uma condição com várias alternativas, nem
		// sempre isso seria verdadeiro.
		if (!this.pausado)
			notifyAll();
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
