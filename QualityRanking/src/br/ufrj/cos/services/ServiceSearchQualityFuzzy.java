package br.ufrj.cos.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentDocument;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.QualityDimensionWeight;
import br.ufrj.cos.db.HelperAcessDB;
import br.ufrj.cos.db.PopulateDB;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.LiveSearch;
import br.ufrj.cos.foxset.search.YahooSearch;

public class ServiceSearchQualityFuzzy extends ServiceSearch {

	public ServiceSearchQualityFuzzy() {
		super(DataSet.SEARCH_QUALITYFUZZY);
	}

	@Override
	protected void prepareDataSets(DataSet dataSet, DataSet dataSetChild)
			throws Exception {
		Collection<QualityDimension> qualityDimensions = HelperAcessDB
				.loadQualityDimensions(dataSet);

		for (QualityDimension qualityDimension : qualityDimensions) {
			List<QualityDimensionWeight> qualityDimensionWeights = HelperAcessDB
					.loadQualityDimensionWeights(dataSet, qualityDimension);
			for (QualityDimensionWeight qualityDimensionWeight : qualityDimensionWeights) {
				ContextQualityDimensionWeight contextQualityDimensionWeight = HelperAcessDB
						.loadContextQualityDimensionWeight(dataSet,
								qualityDimension, qualityDimensionWeight);
				getDao().remove(contextQualityDimensionWeight);
			}

			getDao().create(qualityDimension);

			for (QualityDimensionWeight qualityDimensionWeight : qualityDimensionWeights) {
				PopulateDB.createContextQualityDimensionWeight(dataSetChild,
						qualityDimension, qualityDimensionWeight);
			}

		}

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

			QualityDimension qualityDimension = PopulateDB
					.createOrUpdateQualityDimension(variaveisLinguisticas, code);

			int weight = 1;
			QualityDimensionWeight qualityDimensionWeight = HelperAcessDB
					.loadQualityDimensionWeight(weight);
			PopulateDB.createContextQualityDimensionWeight(dataSet,
					qualityDimension, qualityDimensionWeight);
		}

	}

	@Override
	protected void exportDocumentsFromDataSetFather(DataSet dataSet)
			throws Exception {
		DataSet dataSetFather = dataSet.getDataSetFather();

		List<Document> documents = HelperAcessDB.loadDocuments(dataSetFather);
		for (Document documentOfDataSetFather : documents) {
			Document document = documentOfDataSetFather.clone();
			document.setDataSet(dataSet);
			document.setScore(null);
			dataSet.addDocument(document);
		}

		getDao().update(dataSet);

		List<DocumentDocument> listDocumentDocument = HelperAcessDB
				.loadDocumentDocumentByDataSet(dataSetFather);
		for (DocumentDocument documentDocument : listDocumentDocument) {
			Document docFatherOld = documentDocument.getFatherDocument();
			Document docChildOld = documentDocument.getChildDocument();
			Document docFather = HelperAcessDB.loadDocumentByDataSetAndUrl(
					dataSet, docFatherOld.getUrl());
			Document docChild = HelperAcessDB.loadDocumentByDataSetAndUrl(
					dataSet, docChildOld.getUrl());
			DocumentDocument docDoc = new DocumentDocument();
			docDoc.setChildDocument(docChild);
			docDoc.setFatherDocument(docFather);
			getDao().create(docDoc);
		}

	}

	@Override
	protected void applyFinalRanking(DataSet dataSet) throws Exception {
		derivacaoMetadados(dataSet);
		fuzzy(dataSet);
	}

}
