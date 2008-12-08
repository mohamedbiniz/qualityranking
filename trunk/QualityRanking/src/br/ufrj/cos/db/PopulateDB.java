/**
 * 
 */
package br.ufrj.cos.db;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.Collaborator;
import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.ContextQualityDimensionWeightPk;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.Language;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.QualityDimensionWeight;
import br.ufrj.cos.bean.SeedDocument;
import br.ufrj.cos.foxset.search.GoogleSearch;
import br.ufrj.cos.foxset.search.LiveSearch;
import br.ufrj.cos.foxset.search.YahooSearch;

/**
 * @author Fabricio
 * 
 */
public class PopulateDB {

	private HibernateDAO dao;

	public PopulateDB() {

		setDao(HibernateDAO.getInstance()); // TODO
	}

	// http://en.wikipedia.org/wiki/Relational_databases
	//
	// http://computer.howstuffworks.com/framed.htm?parent=relational-database.htm&url=http://www.edm2.com/0612/msql7.html
	//
	// http://education-portal.com/articles/Database_Administrator_Certification%3A_Certificate_Program_Overview.html
	//
	// http://fyi.oreilly.com/2008/11/relational-database-technology.html#normalization
	//
	//
	// http://redbook.cs.berkeley.edu/redbook3/lecs.html

	public final void popularTradicionalBDR() throws Exception {
		// limparDB();

		Language language = createLanguage("english");

		Collaborator collaborator = createCollaborator(true, true,
				"fulano@fulano.com", "fulano da Silva", "12345", "fulano");

		DataSet dataSet = createDataSet(collaborator, "economy", "economy",
				language, 100, DataSet.STATUS_CRAWLING);

		createSeedDocument(dataSet, "redbook.cs.berkeley.edu",
				"http://redbook.cs.berkeley.edu/redbook3/lecs.html");

		createSeedDocument(dataSet, "en.wikipedia.org",
				"http://en.wikipedia.org/wiki/Relational_databases");

		createSeedDocument(
				dataSet,
				"computer.howstuffworks.com",
				"http://computer.howstuffworks.com/framed.htm?parent=relational-database.htm&url=http://www.edm2.com/0612/msql7.html");

		createSeedDocument(
				dataSet,
				"education-portal.com",
				"http://education-portal.com/articles/Database_Administrator_Certification%3A_Certificate_Program_Overview.html");

		createSeedDocument(
				dataSet,
				"fyi.oreilly.com",
				"http://fyi.oreilly.com/2008/11/relational-database-technology.html#normalization");

		QualityDimension qualityDimension = null;
		QualityDimensionWeight qualityDimensionWeight = null;

		HashMap<String, String> variaveisLinguisticas = new HashMap<String, String>();
		variaveisLinguisticas.put(QualityDimension.COM, "Completeness");
		variaveisLinguisticas.put(QualityDimension.REP, "Reputation");
		variaveisLinguisticas.put(QualityDimension.TIM, "Timeleness");
		for (Iterator<String> iterator = variaveisLinguisticas.keySet()
				.iterator(); iterator.hasNext();) {
			String code = (String) iterator.next();

			qualityDimension = createQualityDimension(variaveisLinguisticas,
					code);

			int weight = 1;

			if (code.equals(QualityDimension.COM)) {
				weight = 4;
			} else if (code.equals(QualityDimension.REP)) {
				weight = 3;
			} else if (code.equals(QualityDimension.TIM)) {
				weight = 2;
			}

			qualityDimensionWeight = createQualityDimensionWeight(
					variaveisLinguisticas, code, weight);

			createContextQualityDimensionWeight(dataSet, qualityDimension,
					qualityDimensionWeight);
		}

	}

	public final void popularTradicionalEconomia() throws Exception {
		// limparDB();

		Language language = createLanguage("english");

		Collaborator collaborator = createCollaborator(true, true,
				"fulano@fulano.com", "fulano da Silva", "12345", "fulano");

		DataSet dataSet = createDataSet(collaborator, "economy", "economy",
				language, 600, DataSet.STATUS_CRAWLING);

		// http://www.economist.com
		createSeedDocument(dataSet, "www.economist.com",
				"http://www.economist.com/");

		// http://www.economypedia.com/
		createSeedDocument(dataSet, "www.economypedia.com",
				"http://www.economypedia.com/");

		// http://www.economywatch.com/
		createSeedDocument(dataSet, "www.economywatch.com",
				"http://www.economywatch.com/");

		// http://www.economy.com/
		createSeedDocument(dataSet, "www.economy.com",
				"http://www.economy.com/");

		// createSeedDocument(dataSet, "www.ufpi.br", "http://www.ufpi.br/");
		//
		// createSeedDocument(dataSet, "www.ufc.br", "http://www.ufc.br/");
		//
		// createSeedDocument(dataSet, "www.ufrj.br", "http://www.ufrj.br/");
		//
		// createSeedDocument(dataSet, "www.ufrgs.br", "http://www.ufrgs.br/");
		//
		// createSeedDocument(dataSet, "www.cos.ufrj.br",
		// "http://www.cos.ufrj.br/");

		QualityDimension qualityDimension = null;
		QualityDimensionWeight qualityDimensionWeight = null;

		HashMap<String, String> variaveisLinguisticas = new HashMap<String, String>();
		variaveisLinguisticas.put(QualityDimension.COM, "Completeness");
		variaveisLinguisticas.put(QualityDimension.REP, "Reputation");
		variaveisLinguisticas.put(QualityDimension.TIM, "Timeleness");
		for (Iterator<String> iterator = variaveisLinguisticas.keySet()
				.iterator(); iterator.hasNext();) {
			String code = (String) iterator.next();

			qualityDimension = createQualityDimension(variaveisLinguisticas,
					code);

			int weight = 1;

			if (code.equals(QualityDimension.COM)) {
				weight = 4;
			} else if (code.equals(QualityDimension.REP)) {
				weight = 3;
			} else if (code.equals(QualityDimension.TIM)) {
				weight = 2;
			}

			qualityDimensionWeight = createQualityDimensionWeight(
					variaveisLinguisticas, code, weight);

			createContextQualityDimensionWeight(dataSet, qualityDimension,
					qualityDimensionWeight);
		}

	}

	public final void popularSearch() throws Exception {
		// limparDB();

		Language language = createLanguage("english");

		Collaborator collaborator = createCollaborator(true, true,
				"fulano@fulano.com", "fulano da Silva", "12345", "fulano");

		DataSet dataSet = createDataSet(collaborator, "economy", "economy",
				language, 100, DataSet.STATUS_SEARCH);

		createSeedDocument(dataSet, "economist", "economist");
		createSeedDocument(dataSet, "economy", "economy");

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

			qualityDimension = createQualityDimension(variaveisLinguisticas,
					code);

			int weight = 1;

			qualityDimensionWeight = createQualityDimensionWeight(
					variaveisLinguisticas, code, weight);
			createContextQualityDimensionWeight(dataSet, qualityDimension,
					qualityDimensionWeight);
		}

	}

	/**
	 * @param dataSet
	 * @param qualityDimension
	 * @param qualityDimensionWeight
	 * @throws Exception
	 */
	private ContextQualityDimensionWeight createContextQualityDimensionWeight(
			DataSet dataSet, QualityDimension qualityDimension,
			QualityDimensionWeight qualityDimensionWeight) throws Exception {
		ContextQualityDimensionWeight contextQualityDimensionWeight = null;
		contextQualityDimensionWeight = new ContextQualityDimensionWeight();
		contextQualityDimensionWeight.setDataSet(dataSet);
		contextQualityDimensionWeight.setQualityDimension(qualityDimension);
		contextQualityDimensionWeight
				.setQualityDimensionWeight(qualityDimensionWeight);

		contextQualityDimensionWeight = (ContextQualityDimensionWeight) create(contextQualityDimensionWeight);
		return contextQualityDimensionWeight;
	}

	/**
	 * @param variaveisLinguisticas
	 * @param code
	 * @param weight
	 * @return
	 * @throws Exception
	 */
	private QualityDimensionWeight createQualityDimensionWeight(
			HashMap<String, String> variaveisLinguisticas, String code,
			int weight) throws Exception {
		QualityDimensionWeight qualityDimensionWeight = null;
		qualityDimensionWeight = new QualityDimensionWeight();
		qualityDimensionWeight.setDescription(variaveisLinguisticas.get(code));
		qualityDimensionWeight.setWeight(weight);
		qualityDimensionWeight = (QualityDimensionWeight) create(qualityDimensionWeight);
		return qualityDimensionWeight;
	}

	/**
	 * @param variaveisLinguisticas
	 * @param code
	 * @return
	 * @throws Exception
	 */
	private QualityDimension createQualityDimension(
			HashMap<String, String> variaveisLinguisticas, String code)
			throws Exception {
		QualityDimension qualityDimension = null;
		qualityDimension = new QualityDimension();
		qualityDimension.setName(variaveisLinguisticas.get(code));
		qualityDimension.setCode(code.toCharArray());

		qualityDimension = (QualityDimension) create(qualityDimension);
		return qualityDimension;
	}

	/**
	 * @param dataSet
	 * @param domain
	 * @param url
	 * @throws Exception
	 */
	private SeedDocument createSeedDocument(DataSet dataSet, String domain,
			String url) throws Exception {
		SeedDocument seedDocument = null;
		seedDocument = new SeedDocument();
		seedDocument.setDataSet(dataSet);
		seedDocument.setDomain(domain);
		seedDocument.setUrl(url);

		seedDocument = (SeedDocument) create(seedDocument);

		return seedDocument;
	}

	/**
	 * @param language
	 * @param collaborator
	 * @param status
	 * @return
	 * @throws Exception
	 */
	private DataSet createDataSet(Collaborator collaborator, String context,
			String description, Language language, int minQuantityPages,
			char status) throws Exception {
		DataSet dataSet = new DataSet();
		dataSet.setCollaborator(collaborator);
		dataSet.setContext(context);
		dataSet.setCreationDate(new Date());
		dataSet.setDescription(description);
		dataSet.setLanguage(language);
		dataSet.setMinQuantityPages(minQuantityPages);
		dataSet.setStatus(status);
		dataSet.setCrawler(true);
		dataSet = (DataSet) create(dataSet);
		return dataSet;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private Collaborator createCollaborator(boolean administrator,
			boolean coordinator, String email, String name, String password,
			String username) throws Exception {
		Collaborator collaborator = new Collaborator();
		collaborator.setAdministrator(administrator);
		collaborator.setCoordinator(coordinator);
		collaborator.setEmail(email);
		collaborator.setName(name);
		collaborator.setPassword(password);
		collaborator.setUsername(username);

		collaborator = (Collaborator) create(collaborator);
		return collaborator;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private Language createLanguage(String name) throws Exception {
		Language language = new Language();
		language.setName(name);
		language = (Language) create(language);
		return language;
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