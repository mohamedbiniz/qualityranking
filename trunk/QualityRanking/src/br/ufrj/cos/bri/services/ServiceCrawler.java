/**
 * 
 */
package br.ufrj.cos.bri.services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
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
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bri.GraphInstance;
import br.ufrj.cos.bri.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bri.bean.DataSet;
import br.ufrj.cos.bri.bean.Document;
import br.ufrj.cos.bri.bean.DocumentQualityDimension;
import br.ufrj.cos.bri.bean.Metadata;
import br.ufrj.cos.bri.bean.QualityDimension;
import br.ufrj.cos.bri.enume.MetadataType;
import br.ufrj.cos.bri.matlab.JobSend;
import br.ufrj.cos.bri.matlab.client.MatClient;
import br.ufrj.cos.bri.services.process.MetadataExtract;
import br.ufrj.htmlbase.Capture;
import edu.uci.ics.jung.algorithms.importance.HITS;
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

	private int qtdSubThread = 1;

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
		extractMetadatas(documents);
		derivacaoMetadados(dataSet);
		fuzzy(dataSet);
	}

	private void extractMetadatas(Collection<Document> documents)
			throws Exception {
		for (Document document : documents) {
			MetadataExtract metadataExtract = new MetadataExtract(document
					.getUrl());
			HashMap<MetadataType, byte[]> listMetadatas = metadataExtract
					.extract();
			for (MetadataType metadataType : listMetadatas.keySet()) {
				byte[] valueMetadata = listMetadatas.get(metadataType);
				Metadata metadata = new Metadata();
				metadata.setDocument(document);
				metadata.setType(metadataType);
				metadata.setValue(valueMetadata);
				getDao().create(metadata);
			}
		}
	}

	private void derivacaoMetadados(DataSet dataSet) throws Exception {
		DocumentQualityDimension documentQualityDimension = null;
		generateJungScores(dataSet, generatePajekFormat(dataSet));
		Collection<Document> documents = loadDocuments(dataSet);
		setNow(new Date());
		for (Document document : documents) {
			Collection<QualityDimension> qualityDimensions = getQualityDimensions(
					dataSet, loadContextQualityDimensionWeights(dataSet));
			for (QualityDimension qualityDimension : qualityDimensions) {
				documentQualityDimension = new DocumentQualityDimension();
				documentQualityDimension.setDocument(document);
				documentQualityDimension.setQualityDimension(qualityDimension);
				double score = 0;
				String code = new String(qualityDimension.getCode());
				if (code.equals(QualityDimension.COM)) {
					score = getCompleteness(document);
				} else if (code.equals(QualityDimension.REP)) {
					score = getReputation(document);
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
		List<Document> documents = loadDocuments(dataSet);
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
		List<Document> rootDocuments = findRootDocuments(dataSet);
		for (Document rootDocument : rootDocuments) {
			toVisitDocument(rootDocument, writer, diffIdOfDocuments);
		}
	}

	private void toVisitDocument(Document fatherDocument, PrintWriter writer,
			long diffIdOfDocuments) {
		int pesoArco = 1;
		List<Document> childDocuments = loadDocumentsByFather(fatherDocument);
		for (Document childDocument : childDocuments) {
			writer.println(String.format("%d %d %d", fatherDocument.getId()
					- diffIdOfDocuments, childDocument.getId()
					- diffIdOfDocuments, pesoArco));
			toVisitDocument(childDocument, writer, diffIdOfDocuments);
		}

	}

	private List<Document> findRootDocuments(DataSet dataSet) {
		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(Restrictions.eq("dataSet", dataSet)).add(
				Restrictions.isNull("document"));

		List<Document> list = (List<Document>) criteria.list();

		return list;
	}

	private HashMap<String, HashMap<Long, Double>> generateJungScores(
			DataSet dataSet, long diffIdOfDocuments) {
		GraphInstance graphInstance = new GraphInstance();
		Graph graph = graphInstance.load(String.format(PAJEK_FILE_NAME_FORMAT,
				dataSet.getId()));
		graphInstance.displayGraph(graph);
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

		// Set<Vertex> vertices = graph.getVertices();
		// HashMap<Vertex, Double> rankingMap = new HashMap<Vertex, Double>();
		// for (Vertex vertex : vertices) {
		// rankingMap.put(vertex, rankerAuthorities.getRankScore(vertex));
		// }

		return null;

	}

	private double getCompleteness(Document document) throws SQLException,
			IOException {
		double score = 0;
		// TODO Auto-generated method stub

		score = document.getUrl().length() / 255.0;

		return score;
	}

	private double getReputation(Document document) {
		double score = document.getUrl().length() / 255.0;
		// TODO Auto-generated method stub
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
						"Este documento (%s) n�o possui o metadado, "
								+ "ou o mesmo n�o est� no formato correto, "
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

	private void fuzzy(DataSet dataSet) throws Exception {

		Collection<ContextQualityDimensionWeight> listCQDWeights = loadContextQualityDimensionWeights(dataSet);

		int qtdQualityDimensions = getQualityDimensions(dataSet, listCQDWeights)
				.size();
		double contextWeights[] = getWeights(listCQDWeights);

		Collection<Document> documents = loadDocuments(dataSet);
		for (Document document : documents) {

			double[] qds = loadDocumentQualityDimensionScores(document);

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

	private List<Document> loadDocuments(DataSet dataSet) {
		List<Document> list = (List<Document>) getDao().loadByField(
				Document.class, "dataSet", dataSet);

		return list;
	}

	private List<Document> loadDocumentsByFather(Document fatherDocument) {

		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(
				Restrictions.eq("dataSet", fatherDocument.getDataSet())).add(
				Restrictions.eq("document", fatherDocument));
		List<Document> list = criteria.list();

		return list;
	}

	private Collection<QualityDimension> getQualityDimensions(DataSet dataSet,
			Collection<ContextQualityDimensionWeight> listCQDWeights) {
		HashMap<Long, QualityDimension> listMap = new HashMap<Long, QualityDimension>();
		for (ContextQualityDimensionWeight contextQualityDimensionWeight : listCQDWeights) {
			QualityDimension qualityDimension = contextQualityDimensionWeight
					.getQualityDimension();
			if (!listMap.containsKey(qualityDimension.getId())) {
				listMap.put(qualityDimension.getId(), qualityDimension);
			}
		}
		return listMap.values();
	}

	private Collection<ContextQualityDimensionWeight> loadContextQualityDimensionWeights(
			DataSet dataSet) {
		Collection<ContextQualityDimensionWeight> list = (Collection<ContextQualityDimensionWeight>) getDao()
				.listAll(ContextQualityDimensionWeight.class);

		for (Iterator<ContextQualityDimensionWeight> iterator = list.iterator(); iterator
				.hasNext();) {
			ContextQualityDimensionWeight contextQualityDimensionWeight = (ContextQualityDimensionWeight) iterator
					.next();
			if (contextQualityDimensionWeight.getDataSet().getId() != dataSet
					.getId()) {
				iterator.remove();
			}
		}
		return list;
	}

	private double[] loadDocumentQualityDimensionScores(Document document) {
		Collection<DocumentQualityDimension> list = (Collection<DocumentQualityDimension>) getDao()
				.listAll(DocumentQualityDimension.class);

		List<Double> scores = new ArrayList<Double>();
		for (Iterator<DocumentQualityDimension> iterator = list.iterator(); iterator
				.hasNext();) {
			DocumentQualityDimension documentQualityDimension = (DocumentQualityDimension) iterator
					.next();
			if (documentQualityDimension.getDocument().getId() == document
					.getId()) {
				scores.add(documentQualityDimension.getScore().doubleValue());
			}
		}

		double[] scoresVector = new double[scores.size()];
		int i = 0;
		for (Double d : scores) {
			scoresVector[i++] = d;
		}
		return scoresVector;
	}

	private double[] getWeights(
			Collection<ContextQualityDimensionWeight> listCQDWeights) {
		double[] weights = new double[listCQDWeights.size()];
		int i = 0;
		for (ContextQualityDimensionWeight w : listCQDWeights) {
			weights[i++] = w.getQualityDimensionWeight().getWeight();
		}
		return weights;
	}

	/**
	 * Nesse m�todo, verificamos a condi��o que desejamos. Se a vari�vel pausada
	 * valer true, isso nos indica que a thread deve dormir. Portanto, damos um
	 * wait() nela. Caso contr�rio, ela deve continuar.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void verificaPausa() throws InterruptedException {
		// Esse while � necess�rio pois threads est�o sujeitas a spurious
		// wakeups, ou seja, elas podem acordar mesmo que nenhum notify tenha
		// sido dado.

		// Whiles diferentes podem ser usados para descrever condi��es
		// diferentes. Voc� tamb�m pode ter mais de uma condi��o no while
		// associada com um e. Por exemplo, no caso de um produtor/consumidor,
		// poderia ser while (!pausado && !fila.cheia()).

		// Nesse caso s� temos uma condi��o, que � dormir quando pausado.
		while (pausado) {
			wait();
		}
	}

	/**
	 * Nesse m�todo, permitimos a quem quer que use a impressora que controle
	 * sua thread. Definindo pausado como true, essa thread ir� parar e esperar
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
		// para ela verificar sua condi��o. Nesse caso, sabemos que a thread
		// acordar�, mas no caso de uma condi��o com v�rias alternativas, nem
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
