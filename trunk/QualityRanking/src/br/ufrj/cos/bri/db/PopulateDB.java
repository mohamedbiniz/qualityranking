/**
 * 
 */
package br.ufrj.cos.bri.db;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bri.bean.Collaborator;
import br.ufrj.cos.bri.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bri.bean.ContextQualityDimensionWeightPk;
import br.ufrj.cos.bri.bean.DataSet;
import br.ufrj.cos.bri.bean.Document;
import br.ufrj.cos.bri.bean.Language;
import br.ufrj.cos.bri.bean.QualityDimension;
import br.ufrj.cos.bri.bean.QualityDimensionWeight;
import br.ufrj.cos.bri.bean.SeedDocument;

/**
 * @author Fabricio
 * 
 */
public class PopulateDB {

	private HibernateDAO dao;

	public PopulateDB() {

		setDao(HibernateDAO.getInstance()); // TODO
	}

	public final void popular() throws Exception {
		// limparDB();

		Language language = new Language();
		language.setName("português");
		language = (Language) create(language);

		Collaborator collaborator = new Collaborator();
		collaborator.setAdministrator(true);
		collaborator.setCoordinator(true);
		collaborator.setEmail("fulano@fulano.com");
		collaborator.setName("fulano da Silva");
		collaborator.setPassword("12345");
		collaborator.setUsername("fulano");
		collaborator = (Collaborator) create(collaborator);

		DataSet dataSet = new DataSet();
		dataSet.setCollaborator(collaborator);
		dataSet.setContext("economia");
		dataSet.setCreationDate(new Date());
		dataSet.setDescription("economia");
		dataSet.setLanguage(language);
		dataSet.setMinQuantityPages(50);
		dataSet.setStatus(DataSet.STATUS_CRAWLING);
		dataSet.setCrawler(true);
		dataSet = (DataSet) create(dataSet);

		SeedDocument seedDocument = null;
		seedDocument = new SeedDocument();
		seedDocument.setDataSet(dataSet);
		seedDocument.setDomain("www.ufpi.br");
		seedDocument.setUrl("http://www.ufpi.br/");

		seedDocument = (SeedDocument) create(seedDocument);

		seedDocument = new SeedDocument();
		seedDocument.setDataSet(dataSet);
		seedDocument.setDomain("www.ufc.br");
		seedDocument.setUrl("http://www.ufc.br/");

		seedDocument = (SeedDocument) create(seedDocument);

		seedDocument = new SeedDocument();
		seedDocument.setDataSet(dataSet);
		seedDocument.setDomain("www.ufrj.br");
		seedDocument.setUrl("http://www.ufrj.br/");

		seedDocument = (SeedDocument) create(seedDocument);

		seedDocument = new SeedDocument();
		seedDocument.setDataSet(dataSet);
		seedDocument.setDomain("www.ufrgs.br");
		seedDocument.setUrl("http://www.ufrgs.br/");

		seedDocument = (SeedDocument) create(seedDocument);

		seedDocument = new SeedDocument();
		seedDocument.setDataSet(dataSet);
		seedDocument.setDomain("www.cos.ufrj.br");
		seedDocument.setUrl("http://www.cos.ufrj.br/");

		seedDocument = (SeedDocument) create(seedDocument);

		QualityDimension qualityDimension = null;
		QualityDimensionWeight qualityDimensionWeight = null;
		ContextQualityDimensionWeight contextQualityDimensionWeight = null;

		HashMap<String, String> variaveisLinguisticas = new HashMap<String, String>();
		variaveisLinguisticas.put(QualityDimension.COM, "Completeness");
		variaveisLinguisticas.put(QualityDimension.REP, "Reputation");
		variaveisLinguisticas.put(QualityDimension.TIM, "Timeleness");
		int i = 1;
		for (Iterator<String> iterator = variaveisLinguisticas.keySet()
				.iterator(); iterator.hasNext();) {
			String code = (String) iterator.next();

			qualityDimension = new QualityDimension();
			qualityDimension.setName(variaveisLinguisticas.get(code));
			qualityDimension.setCode(code.toCharArray());

			qualityDimension = (QualityDimension) create(qualityDimension);

			qualityDimensionWeight = new QualityDimensionWeight();
			qualityDimensionWeight.setDescription(variaveisLinguisticas
					.get(code));
			qualityDimensionWeight.setWeight(1);
			// qualityDimensionWeight.addDataSet(dataSet);
			// qualityDimensionWeight.addQualityDimension(qualityDimension);

			qualityDimensionWeight = (QualityDimensionWeight) create(qualityDimensionWeight);

			contextQualityDimensionWeight = new ContextQualityDimensionWeight();
			contextQualityDimensionWeight.setDataSet(dataSet);
			contextQualityDimensionWeight.setQualityDimension(qualityDimension);
			contextQualityDimensionWeight
					.setQualityDimensionWeight(qualityDimensionWeight);

			contextQualityDimensionWeight = (ContextQualityDimensionWeight) create(contextQualityDimensionWeight);
		}

	}

	public void limparDB() throws Exception {

		// Collection<DocumentQualityDimension> documentQualityDimensions =
		// (Collection<DocumentQualityDimension>) getDao()
		// .listAll(DocumentQualityDimension.class);
		// for (DocumentQualityDimension documentQualityDimension :
		// documentQualityDimensions) {
		// getDao().remove(documentQualityDimension);
		// }

		Collection<DataSet> dataSets = (Collection<DataSet>) getDao().listAll(
				DataSet.class);
		for (DataSet dataSet : dataSets) {
			Collection<Document> documents = findRootDocuments(dataSet);
			for (Document document : documents) {
				removeDocument(document);
			}
		}

		Collection<SeedDocument> seedDocuments = (Collection<SeedDocument>) getDao()
				.listAll(SeedDocument.class);
		for (SeedDocument seedDocument : seedDocuments) {
			getDao().remove(seedDocument);
		}

		Collection<QualityDimensionWeight> qualityDimensionWeights = (Collection<QualityDimensionWeight>) getDao()
				.listAll(QualityDimensionWeight.class);
		for (QualityDimensionWeight qualityDimensionWeight : qualityDimensionWeights) {
			getDao().remove(qualityDimensionWeight);
		}

		Collection<QualityDimension> qualityDimensions = (Collection<QualityDimension>) getDao()
				.listAll(QualityDimension.class);
		for (QualityDimension qualityDimension : qualityDimensions) {
			getDao().remove(qualityDimension);
		}

		for (DataSet dataSet : dataSets) {
			getDao().remove(dataSet);
		}

		Collection<Language> languages = (Collection<Language>) getDao()
				.listAll(Language.class);
		for (Language language : languages) {
			getDao().remove(language);
		}

		Collection<Collaborator> collaborators = (Collection<Collaborator>) getDao()
				.listAll(Collaborator.class);
		for (Collaborator collaborator : collaborators) {
			getDao().remove(collaborator);
		}

	}

	private void removeDocument(Document fatherDocument) throws Exception {
		List<Document> childDocuments = loadDocumentsByFather(fatherDocument);
		for (Document childDocument : childDocuments) {
			removeDocument(childDocument);
			getDao().remove(childDocument);
		}

	}

	private List<Document> findRootDocuments(DataSet dataSet) {
		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(Restrictions.eq("dataSet", dataSet)).add(
				Restrictions.isNull("document"));

		List<Document> list = (List<Document>) criteria.list();

		return list;
	}

	private List<Document> loadDocumentsByFather(Document fatherDocument) {

		Criteria criteria = getDao().openSession().createCriteria(
				Document.class).add(
				Restrictions.eq("dataSet", fatherDocument.getDataSet())).add(
				Restrictions.eq("document", fatherDocument));
		List<Document> list = criteria.list();

		return list;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public Object create(Object obj) throws Exception {
		// org.hibernate.Transaction t =
		// getDao().getSession().beginTransaction();
		getDao().create(obj);
		// t.commit();
		return obj;
	}

	public Object update(Object obj) throws Exception {
		// org.hibernate.Transaction t =
		// getDao().getSession().beginTransaction();

		getDao().update(obj);
		// t.commit();
		return obj;
	}

	public Object remove(Object obj) throws Exception {
		// org.hibernate.Transaction t =
		// getDao().getSession().beginTransaction();

		getDao().remove(obj);
		// t.commit();
		return obj;
	}

	public List<Object[]> listAll(String tableName) {
		return getDao().listAll(tableName);
	}

	/**
	 * @return the dao
	 */
	public HibernateDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	public void setDao(HibernateDAO dao) {
		this.dao = dao;
	}

	public void teste() {
		DataSet dataSet = (DataSet) getDao().loadById(DataSet.class,
				new Long(29));
		System.out.println(dataSet.getQualityDimensions().size());
		QualityDimension qualityDimension = (QualityDimension) getDao()
				.loadById(QualityDimension.class, new Long(6));
		QualityDimensionWeight qualityDimensionWeight = (QualityDimensionWeight) getDao()
				.loadById(QualityDimensionWeight.class, new Long(6));
		ContextQualityDimensionWeightPk contextQualityDimensionWeightPk = new ContextQualityDimensionWeightPk();
		if (dataSet != null)
			System.out.println(dataSet.getId());
		contextQualityDimensionWeightPk.setDataSet(dataSet);
		contextQualityDimensionWeightPk.setQualityDimension(qualityDimension);
		contextQualityDimensionWeightPk
				.setQualityDimensionWeight(qualityDimensionWeight);
		Collection<ContextQualityDimensionWeight> list = getContextQualityDimensionWeights(contextQualityDimensionWeightPk);
		for (ContextQualityDimensionWeight contextQualityDimensionWeight : list) {
			System.out.println(contextQualityDimensionWeight.getDataSet()
					.getContext());
		}
		System.out.println(list.size());

	}

	private Collection<ContextQualityDimensionWeight> getContextQualityDimensionWeights(
			ContextQualityDimensionWeightPk contextQualityDimensionWeightPk) {
		return (Collection<ContextQualityDimensionWeight>) getDao()
				.loadByFieldPk(ContextQualityDimensionWeight.class, "id",
						contextQualityDimensionWeightPk);
	}
}