package br.ufrj.cos.services;

import br.ufrj.cos.bean.DataSet;

public class ServiceSearchQualityFuzzy extends ServiceSearch {

	public ServiceSearchQualityFuzzy() {
		super(DataSet.SEARCH_QUALITYFUZZY);
	}

	@Override
	protected void exportDocumentsFromDataSetFather(DataSet dataSetChild) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void applyFinalRanking(DataSet dataSet) {
		// TODO Auto-generated method stub
	}

}
