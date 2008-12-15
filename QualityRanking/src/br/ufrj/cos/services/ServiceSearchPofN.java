package br.ufrj.cos.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.QualityDimensionWeight;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.PopulateDB;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.LiveSearch;
import br.ufrj.cos.foxset.search.YahooSearch;

public class ServiceSearchPofN extends ServiceSearch {

	public ServiceSearchPofN() {
		super(DataSet.SEARCH_POFN);
	}

	@Override
	protected void prepareDataSets(DataSet dataSet, DataSet dataSetChild)
			throws Exception {

		QualityDimension qualityDimension = null;
		QualityDimensionWeight qualityDimensionWeight = null;

		HashMap<String, String> variaveisLinguisticas = new HashMap<String, String>();
		GoogleSearch googleSearch = new GoogleSearch();
		variaveisLinguisticas.put(googleSearch.getSearchEngineCode().substring(
				0, 3), googleSearch.getSearchEngineCode());

		YahooSearch yahooSearch = new YahooSearch();
		variaveisLinguisticas.put(yahooSearch.getSearchEngineCode().substring(
				0, 3), yahooSearch.getSearchEngineCode());
		LiveSearch liveSearch = new LiveSearch();
		variaveisLinguisticas.put(liveSearch.getSearchEngineCode().substring(0,
				3), liveSearch.getSearchEngineCode());
		for (Iterator<String> iterator = variaveisLinguisticas.keySet()
				.iterator(); iterator.hasNext();) {
			String code = (String) iterator.next();

			qualityDimension = PopulateDB.createQualityDimension(
					variaveisLinguisticas, code);

			int weight = 1;
			qualityDimensionWeight = HelperAcessDB
					.loadQualityDimensionWeight(weight);
			PopulateDB.createContextQualityDimensionWeight(dataSet,
					qualityDimension, qualityDimensionWeight);
			PopulateDB.createContextQualityDimensionWeight(dataSetChild,
					qualityDimension, qualityDimensionWeight);
		}

	}

	@Override
	protected void exportDocumentsFromDataSetFather(DataSet dataSet)
			throws Exception {
		int i = 0;
		Collection<Document> documents = HelperAcessDB
				.findDocumentsGroupByQualityDimension(dataSet
						.getDataSetFather());
		for (Document documentOfDataSetFather : documents) {
			Document document = documentOfDataSetFather.clone();
			document.setDataSet(dataSet);
			dataSet.addDocument(document);
		}
		getDao().update(dataSet);
	}

	@Override
	protected void applyFinalRanking(DataSet dataSet) throws Exception {
		// TODO Auto-generated method stub

	}

}
