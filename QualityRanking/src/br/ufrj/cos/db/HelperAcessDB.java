/**
 * 
 */
package br.ufrj.cos.db;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.DataSetCollaborator;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentDocument;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.QualityDimensionWeight;
import br.ufrj.cos.bean.SeedDocument;

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

	public static ContextQualityDimensionWeight loadContextQualityDimensionWeight(
			DataSet dataSet, QualityDimension qualityDimension,
			QualityDimensionWeight qualityDimensionWeight) {
		Criteria criteria = getDao().openSession().createCriteria(
				ContextQualityDimensionWeight.class).add(
				Restrictions.eq("id.dataSet", dataSet)).add(
				Restrictions.eq("id.qualityDimension", qualityDimension)).add(
				Restrictions.eq("id.qualityDimensionWeight",
						qualityDimensionWeight));
		ContextQualityDimensionWeight contextQualityDimensionWeight = (ContextQualityDimensionWeight) criteria
				.uniqueResult();
		return contextQualityDimensionWeight;
	}

	public static List<QualityDimensionWeight> loadQualityDimensionWeights(
			DataSet dataSet, QualityDimension qualityDimension) {
		Criteria criteria = getDao().openSession().createCriteria(
				ContextQualityDimensionWeight.class).add(
				Restrictions.eq("id.dataSet", dataSet)).add(
				Restrictions.eq("id.qualityDimension", qualityDimension))
				.setProjection(
						Projections.property("id.qualityDimensionWeight"));
		List<QualityDimensionWeight> list = criteria.list();
		return list;
	}

	public static QualityDimension loadQualityDimension(DataSet dataSet,
			String searchEngineCode) {
		Collection<QualityDimension> qualityDimensions = loadQualityDimensions(dataSet);
		for (QualityDimension qualityDimension : qualityDimensions) {
			if (searchEngineCode.contains(qualityDimension.getCodeStr())) {
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

	public static List<DataSet> loadAllDataSetWhitoutFather() {
		Criteria criteria = getDao().openSession()
				.createCriteria(DataSet.class).add(
						Restrictions.isNull("dataSetFather"));
		List<DataSet> list = (List<DataSet>) criteria.list();
		return list;
	}

	public static List<DataSet> loadAllDataSetWhitFather() {
		Criteria criteria = getDao().openSession()
				.createCriteria(DataSet.class).add(
						Restrictions.isNotNull("dataSetFather"));
		List<DataSet> list = (List<DataSet>) criteria.list();
		return list;
	}

	public static DataSet loadDataSetChild(DataSet dataSet) {
		Criteria criteria = getDao().openSession()
				.createCriteria(DataSet.class).add(
						Restrictions.eq("dataSetFather", dataSet));
		criteria.setMaxResults(1);
		List<DataSet> list = (List<DataSet>) criteria.list();
		DataSet dataSetChild = null;
		if (!list.isEmpty())
			dataSetChild = list.get(0);
		return dataSetChild;
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

	public static Collection<Document> findDocumentsGroupByQualityDimension(
			DataSet dataSet) {

		Criteria criteria = getDao().openSession().createCriteria(
				DocumentQualityDimension.class).add(
				Restrictions.gt("score", new BigDecimal(0)));
		criteria.setProjection(Projections.property("id.document.id"));
		List<Long> idsDocuments = (List<Long>) criteria.list();
		Map<Long, Integer> docCount = new HashMap<Long, Integer>();
		for (Long idDoc : idsDocuments) {
			int value = (docCount.get(idDoc) == null ? 1 : docCount.get(idDoc)
					.intValue() + 1);
			docCount.put(idDoc, new Integer(value));
		}
		idsDocuments = new ArrayList<Long>();
		for (Long idDoc : docCount.keySet()) {
			if (docCount.get(idDoc).intValue() >= dataSet.getPOfN()) {
				idsDocuments.add(idDoc);
			}
		}
		List<Document> list = new ArrayList<Document>();
		if (!idsDocuments.isEmpty()) {
			criteria = getDao().openSession().createCriteria(Document.class)
					.add(Restrictions.eq("dataSet", dataSet)).add(
							Restrictions.in("id", idsDocuments));
			list = (List<Document>) criteria.list();
		}
		return list;
	}

	public static List<QualityDimensionWeight> findQualityDimensionWeights(
			QualityDimension qualityDimension, String description, int weight) {
		Criteria criteria = getDao().getSession().createCriteria(
				QualityDimensionWeight.class).add(
				Restrictions.eq("weight", new Integer(weight)));
		if (qualityDimension != null) {
			// TODO
		}
		if (description != null) {
			criteria.add(Restrictions.eq("description", description));
		}
		return criteria.list();

	}

	public static Collection<SeedDocument> loadSeedDocuments(DataSet dataSet) {
		List<SeedDocument> list = (List<SeedDocument>) getDao().loadByField(
				SeedDocument.class, "dataSet", dataSet);
		return list;
	}

	public static List<DocumentDocument> loadDocumentDocumentByDataSet(
			DataSet dataSetFather) {
		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(Restrictions.eq("dataSet", dataSetFather))
				.setProjection(Projections.property("id"));

		Collection<Long> idDocuments = criteria.list();

		List<DocumentDocument> list = new ArrayList<DocumentDocument>();
		if (!idDocuments.isEmpty()) {
			criteria = getDao().openSession().createCriteria(
					DocumentDocument.class).add(
					Restrictions.or(Restrictions.in("id.childDocument.id",
							idDocuments), Restrictions.in(
							"id.fatherDocument.id", idDocuments)));

			list = (List<DocumentDocument>) criteria.list();
		}

		return list;
	}

	public static Document loadDocumentByDataSetAndUrl(DataSet dataSet,
			String url) {
		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(Restrictions.eq("dataSet", dataSet)).add(
				Restrictions.eq("url", url));
		List<Document> list = criteria.list();
		Document doc = null;
		if (!list.isEmpty()) {
			doc = list.get(0);
		}
		return doc;
	}

	public static Collection<DataSetCollaborator> loadCollaborators(
			DataSet dataSet) {
		Criteria criteria = getDao().openSession().createCriteria(
				DataSetCollaborator.class).add(
				Restrictions.eq("id.dataSet", dataSet));
		List<DataSetCollaborator> dataSetCollaborator = (List<DataSetCollaborator>) criteria
				.list();
		return dataSetCollaborator;
	}
}
