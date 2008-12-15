package br.ufrj.cos.services;

import java.util.Collection;

import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.db.HelperAcessDB;

public class ServiceSearchPofN extends ServiceSearch {

	public ServiceSearchPofN() {
		super(DataSet.SEARCH_POFN);
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

		// associateQualityDimensions(dataSet);

		// fuzzy(dataSet);

	}

}
