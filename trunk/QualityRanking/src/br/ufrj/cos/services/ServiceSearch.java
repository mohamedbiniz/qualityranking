/**
 * 
 */
package br.ufrj.cos.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

		fuzzy(dataSet);
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
		Collection<Document> childDocuments = findChildDocumentsByLinks(childLinks);
		document.addAllChildDocuments(childDocuments);

		Map<String, Integer> mapFatherLinks = wf.getBackLinks();
		Set<String> fatherLinks = mapFatherLinks.keySet();
		Collection<Document> fatherDocuments = findFatherDocumentsByLinks(fatherLinks);
		document.addAllFatherDocuments(fatherDocuments);

		getDao().update(document);
	}

	private Collection<Document> findFatherDocumentsByLinks(
			Set<String> fatherLinks) {
		// TODO Auto-generated method stub
		return new ArrayList<Document>();
	}

	private Collection<Document> findChildDocumentsByLinks(
			Set<String> childLinks) {
		// TODO Auto-generated method stub
		return new ArrayList<Document>();
	}

	private void updateSearchRankingScore(Document document,
			QualityDimension qualityDimension, int sizeResults, int position)
			throws Exception {
		DocumentQualityDimension documentQualityDimension = null;
		documentQualityDimension = new DocumentQualityDimension();
		documentQualityDimension.setDocument(document);
		documentQualityDimension.setQualityDimension(qualityDimension);
		double score = 0;
		score = ((double) position) / ((double) sizeResults);
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
