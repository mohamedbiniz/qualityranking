/**
 * 
 */
package br.ufrj.cos.bri.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;

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
import br.ufrj.htmlbase.db.hibernate.PageHibernateImpl;
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

	private static final long PAUSA_CRAWLER = 30000;

	Capture crawler;

	private boolean pausado;

	private int qtdSubThread = 1;

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
		// TODO Auto-generated method stub
		// long minIdDocument = getMinIdDocument(dataSet);
		DocumentQualityDimension documentQualityDimension = null;
		Collection<Document> documents = loadDocuments(dataSet);
		// generatePajekFormat();
		// generateJungScores();
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

	private void generateJungScores() {
		GraphInstance graphInstance = new GraphInstance();
		Graph graph = graphInstance.load("pajek.txt");
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
		try {
			Thread.sleep(200000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private double getTimeliness(Document document) {
		double score = document.getUrl().length() / 255.0;
		// TODO Auto-generated method stub
		return score;
	}

	private double getReputation(Document document) {
		double score = document.getUrl().length() / 255.0;
		// TODO Auto-generated method stub
		return score;
	}

	private double getCompleteness(Document document) throws SQLException,
			IOException {
		double score = 0;
		// TODO Auto-generated method stub

		getCompletenessFromPajek(document);

		score = document.getUrl().length() / 255.0;

		return score;
	}

	private void getCompletenessFromPajek(Document document) {
		// TODO Auto-generated method stub

	}

	private void generatePajekFormat() throws SQLException, IOException {
		PageHibernateImpl pageDao = new PageHibernateImpl();

		pageDao.generatePajek();

	}

	private long getMinIdDocument(DataSet dataSet) {
		Transaction tx = null;
		List<Document> result = null;
		try {
			tx = getDao().openSession().beginTransaction();
			Query q = getDao().getSession().createQuery(
					String.format("from Document order by id",
							getDataSetInitStatus()));
			q.setMaxResults(1);
			result = q.list();
			tx.commit();
		} catch (HibernateException he) {
			if (tx != null)
				tx.rollback();
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

	private Collection<Document> loadDocuments(DataSet dataSet) {
		Collection<Document> list = (Collection<Document>) getDao().listAll(
				Document.class);

		for (Iterator<Document> iterator = list.iterator(); iterator.hasNext();) {
			Document document = (Document) iterator.next();
			if (document.getDataSet().getId() != dataSet.getId()) {
				iterator.remove();
			}
		}
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
}
