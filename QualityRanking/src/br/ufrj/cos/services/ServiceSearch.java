/**
 * 
 */
package br.ufrj.cos.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.SeedDocument;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.LiveSearch;
import br.ufrj.cos.foxset.search.SearchEngine;
import br.ufrj.cos.foxset.search.WebFile;
import br.ufrj.cos.foxset.search.YahooSearch;
import br.ufrj.cos.foxset.search.SearchEngine.Result;

/**
 * @author Fabricio
 * 
 */
public class ServiceSearch extends Service {

	private static final long PAUSA_SEARCH = 30000;

	private SearchEngine[] se;

	public ServiceSearch() {
		super(DataSet.STATUS_SEARCH, DataSet.STATUS_UNDEFINED, PAUSA_SEARCH);
		se = new SearchEngine[3];

		se[0] = new GoogleSearch();
		se[0].setAppID("F4ZdLRNQFHKUvggiU+9+60sA8vc3fohb");

		se[1] = new YahooSearch();
		se[1]
				.setAppID("j3ANBxbV34FKDH_U3kGw0Jwj5Zbc__TDAYAzRopuJMGa8WBt0mtZlj4n1odUtMR8hco-");

		se[2] = new LiveSearch();
		se[2].setAppID("6F4477E8615644FDA81A62E15217B400B121E0A4");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufrj.cos.bri.services.Service#execute(br.ufrj.cos.bri.bean.DataSet)
	 */
	@Override
	protected void execute(DataSet dataSet) throws Exception {
		String keyWords = getKeywords(dataSet);
		for (int i = 0; i < se.length; i++) {
			searchAndPersistPages(dataSet, se[i], keyWords);
		}
		mathDocumentAndQualityDimension(dataSet);

		fuzzy(dataSet);
	}

	private void mathDocumentAndQualityDimension(DataSet dataSet)
			throws Exception {
		Collection<QualityDimension> qualityDimensions = HelperAcessDB
				.loadQualityDimensions(dataSet);
		for (QualityDimension qualityDimension : qualityDimensions) {
			Collection<Document> documents = findDocumentsWithoutQualityDimension(
					dataSet, qualityDimension);
			for (Document document : documents) {
				updateSearchRankingScore(document, qualityDimension, 1, 1);
			}
		}
	}

	private Collection<Document> findDocumentsWithoutQualityDimension(
			DataSet dataSet, QualityDimension qualityDimension) {
		Criteria criteria = getDao().openSession().createCriteria(
				DocumentQualityDimension.class).add(
				Restrictions.ne("id.qualityDimension", qualityDimension))
				.setProjection(Projections.property("id.document.id"));
		Collection<Long> listIdDocumentsNotInQualiyDimension = criteria.list();
		criteria = getDao().openSession().createCriteria(
				DocumentQualityDimension.class).add(
				Restrictions.eq("id.qualityDimension", qualityDimension))
				.setProjection(Projections.property("id.document.id"));
		Collection<Long> listIdDocumentsInQualiyDimension = criteria.list();
		for (Iterator<Long> iterator = listIdDocumentsNotInQualiyDimension
				.iterator(); iterator.hasNext();) {
			Long idDocument = iterator.next();
			if (listIdDocumentsInQualiyDimension.contains(idDocument)) {
				iterator.remove();
			}

		}
		criteria = getDao().openSession().createCriteria(Document.class).add(
				Restrictions.eq("dataSet", dataSet)).add(
				Restrictions.in("id", listIdDocumentsNotInQualiyDimension));
		Collection<Document> list = criteria.list();
		return list;
	}

	private void searchAndPersistPages(DataSet dataSet, SearchEngine se,
			String keyWords) throws Exception {

		se.setMaxResults(dataSet.getMinQuantityPages());
		List<Result> results = se.search(keyWords);
		QualityDimension qualityDimension = HelperAcessDB.loadQualityDimension(
				dataSet, se.getSearchEngineCode());

		int position = 0;

		for (Result result : results) {
			System.gc();
			Document document = new Document();
			document.setDataSet(dataSet);
			document.setUrl(result.getURL());
			Document documentPersisted = HelperAcessDB
					.getPersistedDocument(document);
			if (documentPersisted == null) {
				getDao().create(document);
				ServiceCrawler.extractMetadatas(document);
			} else {
				document = documentPersisted;
			}
			if (qualityDimension != null) {
				updateSearchRankingScore(document, qualityDimension, results
						.size(), position);
			}
			updateDocumentLinks(document);
			position++;
		}
	}

	private void updateDocumentLinks(Document document) throws Exception {
		WebFile wf = new WebFile(document.getUrl());

		Map<String, Integer> mapChildLinks = wf.getForwardLinks();
		Set<String> childLinks = mapChildLinks.keySet();
		Collection<Document> childDocuments = findDocumentsByLinks(childLinks,
				document.getDataSet());
		document.addAllChildDocuments(childDocuments);

		Map<String, Integer> mapFatherLinks = wf.getBackLinks();
		Set<String> fatherLinks = mapFatherLinks.keySet();
		Collection<Document> fatherDocuments = findDocumentsByLinks(
				fatherLinks, document.getDataSet());
		document.addAllFatherDocuments(fatherDocuments);

		getDao().update(document);
	}

	private Collection<Document> findDocumentsByLinks(Set<String> fatherLinks,
			DataSet dataSet) {
		Collection<Document> fatherDocuments = new ArrayList<Document>();
		if (!fatherLinks.isEmpty()) {
			Criteria criteria = getDao().openSession().createCriteria(
					Document.class).add(Restrictions.eq("dataSet", dataSet))
					.add(Restrictions.in("url", fatherLinks));
			fatherDocuments = (List<Document>) criteria.list();
		}
		return fatherDocuments;
	}

	private void updateSearchRankingScore(Document document,
			QualityDimension qualityDimension, int sizeResults, int position)
			throws Exception {
		DocumentQualityDimension documentQualityDimension = null;
		documentQualityDimension = new DocumentQualityDimension();
		documentQualityDimension.setDocument(document);
		documentQualityDimension.setQualityDimension(qualityDimension);
		double score = 0;
		score = ((double) sizeResults - (double) position)
				/ ((double) sizeResults);
		documentQualityDimension.setScore(new BigDecimal(score));
		getDao().create(documentQualityDimension);
	}

	private String getKeywords(DataSet dataSet) {
		String keyWord = "";
		for (SeedDocument seedDocument : dataSet.getSeedDocuments()) {
			keyWord += " " + seedDocument.getUrl();
		}
		return keyWord.substring(1);
	}
}
