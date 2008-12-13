/**
 * 
 */
package br.ufrj.cos.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentDocument;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.QualityDimension;

/**
 * @author Fabricio
 * 
 */
public class HelperAcessDB {

	public static double[] loadDocumentQualityDimensionScores(Document document) {
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

	public static Collection<ContextQualityDimensionWeight> loadContextQualityDimensionWeights(
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

	public static QualityDimension loadQualityDimension(DataSet dataSet,
			String searchEngineCode) {
		Collection<QualityDimension> qualityDimensions = loadQualityDimensions(dataSet);
		for (QualityDimension qualityDimension : qualityDimensions) {
			if (searchEngineCode.contains(String.valueOf(qualityDimension
					.getCode()))) {
				return qualityDimension;
			}
		}
		return null;
	}

	public static Collection<QualityDimension> loadQualityDimensions(
			DataSet dataSet) {
		Collection<ContextQualityDimensionWeight> listCQDWeights = HelperAcessDB
				.loadContextQualityDimensionWeights(dataSet);
		return loadQualityDimensions(dataSet, listCQDWeights);
	}

	public static Collection<QualityDimension> loadQualityDimensions(
			DataSet dataSet,
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

	public static List<Document> loadDocuments(DataSet dataSet) {
		List<Document> list = (List<Document>) getDao().loadByField(
				Document.class, "dataSet", dataSet);

		return list;
	}

	public static List<Document> loadDocumentsByFather(Document fatherDocument) {
		Criteria criteria = getDao().openSession().createCriteria(
				DocumentDocument.class).add(
				Restrictions.eq("id.fatherDocument", fatherDocument))
				.setProjection(Projections.property("id.childDocument.id"));
		Collection<Long> lisIdChilds = criteria.list();
		List<Document> list = new ArrayList<Document>();
		if (!lisIdChilds.isEmpty()) {
			criteria = getDao().openSession().createCriteria(Document.class)
					.add(
							Restrictions.eq("dataSet", fatherDocument
									.getDataSet())).add(
							Restrictions.in("id", lisIdChilds));
			list = criteria.list();
		}

		return list;
	}

	public static List<Document> findRootDocuments(DataSet dataSet) {
		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(Restrictions.eq("dataSet", dataSet)).add(
				Restrictions.isEmpty("fatherDocuments"));

		List<Document> list = (List<Document>) criteria.list();

		return list;
	}

	public static double[] getWeights(
			Collection<ContextQualityDimensionWeight> listCQDWeights) {
		double[] weights = new double[listCQDWeights.size()];
		int i = 0;
		for (ContextQualityDimensionWeight w : listCQDWeights) {
			weights[i++] = w.getQualityDimensionWeight().getWeight();
		}
		return weights;
	}

	/**
	 * @return the dao
	 */
	public static HibernateDAO getDao() {
		return HibernateDAO.getInstance();
	}

	public static Document getPersistedDocument(Document document) {
		Document documentPersisted = null;
		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(
				Restrictions.eq("dataSet", document.getDataSet()));

		List<Document> list = (List<Document>) criteria.list();
		int index = list.indexOf(document);
		if (index != -1)
			documentPersisted = list.get(index);
		return documentPersisted;
	}
}
