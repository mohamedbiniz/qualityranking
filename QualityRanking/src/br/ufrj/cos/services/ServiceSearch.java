/**
 * 
 */
package br.ufrj.cos.services;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.SeedDocument;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.SearchEngine;
import br.ufrj.cos.foxset.search.SearchEngine.Result;

/**
 * @author Fabricio
 * 
 */
public class ServiceSearch extends Service {

	private static final long PAUSA_SEARCH = 30000;

	private SearchEngine se;

	public ServiceSearch() {
		super(DataSet.STATUS_SEARCH, DataSet.STATUS_UNDEFINED, PAUSA_SEARCH);
		se = new GoogleSearch();
		se.setAppID("F4ZdLRNQFHKUvggiU+9+60sA8vc3fohb");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.ufrj.cos.bri.services.Service#execute(br.ufrj.cos.bri.bean.DataSet)
	 */
	@Override
	protected void execute(DataSet dataSet) throws Exception {
		se.setMaxResults(dataSet.getMinQuantityPages());
		String keyWords = getKeywords(dataSet);
		searchAndPersistPages(dataSet, se, keyWords);

	}

	private void searchAndPersistPages(DataSet dataSet, SearchEngine se,
			String keyWords) throws Exception {
		List<Result> results = se.search(keyWords);
		QualityDimension qualityDimension = loadQualityDimensions(dataSet,
				ServiceCrawler.loadContextQualityDimensionWeights(dataSet), se
						.getSearchEngineCode());

		int position = 0;

		for (Result result : results) {
			System.gc();
			Document document = new Document();
			document.setDataSet(dataSet);
			document.setUrl(result.getURL());
			getDao().create(document);
			ServiceCrawler.extractMetadatas(document);
			if (qualityDimension != null) {
				updateSearchRankingScore(document, qualityDimension, results
						.size(), position);
			}
			position++;
		}
	}

	private QualityDimension loadQualityDimensions(
			DataSet dataSet,
			Collection<ContextQualityDimensionWeight> loadContextQualityDimensionWeights,
			String searchEngineCode) {
		Collection<QualityDimension> qualityDimensions = ServiceCrawler
				.loadQualityDimensions(dataSet, ServiceCrawler
						.loadContextQualityDimensionWeights(dataSet));
		for (QualityDimension qualityDimension : qualityDimensions) {
			if (searchEngineCode
					.contains(qualityDimension.getCode().toString())) {
				return qualityDimension;
			}
		}
		return null;
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
		return keyWord;
	}
}
