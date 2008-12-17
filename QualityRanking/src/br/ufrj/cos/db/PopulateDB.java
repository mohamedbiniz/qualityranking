/**
 * 
 */
package br.ufrj.cos.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.ufrj.cos.bean.Collaborator;
import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.Language;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.QualityDimensionWeight;
import br.ufrj.cos.bean.SeedDocument;

/**
 * @author Fabricio
 * 
 */
public class PopulateDB {

	private static HibernateDAO dao;

	static {

		setDao(HibernateDAO.getInstance());
	}

	public static void initFoxSet() throws Exception {
		Language languagePt = createLanguage("Português (BR)");
		Language languageEn = createLanguage("English (US)");

		Collaborator coordinatorFoxSet = createCollaboratorFoxSet();

		QualityDimension qualityDimension = null;

		HashMap<String, String> variaveisLinguisticas = new HashMap<String, String>();
		variaveisLinguisticas.put(QualityDimension.COM, "Completeness");
		variaveisLinguisticas.put(QualityDimension.REP, "Reputation");
		variaveisLinguisticas.put(QualityDimension.TIM, "Timeleness");
		variaveisLinguisticas.put(QualityDimension.SEC, "Security");
		for (Iterator<String> iterator = variaveisLinguisticas.keySet()
				.iterator(); iterator.hasNext();) {
			String code = (String) iterator.next();

			qualityDimension = createQualityDimension(variaveisLinguisticas,
					code);

		}

		createQualityDimensionsWeights();

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

	public static final void popularTradicionalBDR() throws Exception {
		// limparDB();

		Language language = createLanguage("english");

		Collaborator collaborator = createCollaboratorFoxSet();

		DataSet dataSet = createDataSet(collaborator, "economy", "economy",
				language, 100, DataSet.STATUS_SEARCH,
				DataSet.CRAWLER_QUALITYFUZZY);

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

		createQualityDimensionsWeights();

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
			qualityDimensionWeight = HelperAcessDB
					.loadQualityDimensionWeight(weight);

			createContextQualityDimensionWeight(dataSet, qualityDimension,
					qualityDimensionWeight);
		}

	}

	public static final void popularTradicionalEconomia() throws Exception {
		// limparDB();

		Language language = createLanguage("english");

		Collaborator collaborator = createCollaboratorFoxSet();

		DataSet dataSet = createDataSet(collaborator, "economy", "economy",
				language, 60, DataSet.STATUS_SEARCH,
				DataSet.CRAWLER_QUALITYFUZZY);

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

		createQualityDimensionsWeights();

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

			qualityDimensionWeight = HelperAcessDB
					.loadQualityDimensionWeight(weight);

			createContextQualityDimensionWeight(dataSet, qualityDimension,
					qualityDimensionWeight);
		}

	}

	public static final void popularSearchPofN(int qtdPag, String keyWords,
			Integer pOfN) throws Exception {
		// limparDB();

		Language language = createLanguage("english");

		Collaborator collaborator = createCollaboratorFoxSet();

		DataSet dataSet = createDataSet(collaborator, keyWords, keyWords,
				language, qtdPag, pOfN, DataSet.STATUS_SEARCH,
				DataSet.SEARCH_POFN);

		createSeedDocument(dataSet, keyWords, keyWords);
		// createSeedDocument(dataSet, "economist", "economist");
		// createSeedDocument(dataSet, "economy", "economy");
		createQualityDimensionsWeights();

	}

	public static void popularSearchQF(int qtdPag, String keyWords)
			throws Exception {
		Language language = createLanguage("english");

		Collaborator collaborator = createCollaboratorFoxSet();

		DataSet dataSet = createDataSet(collaborator, keyWords, keyWords,
				language, qtdPag, DataSet.STATUS_SEARCH,
				DataSet.SEARCH_QUALITYFUZZY);

		createSeedDocument(dataSet, keyWords, keyWords);
		// createSeedDocument(dataSet, "economist", "economist");
		// createSeedDocument(dataSet, "economy", "economy");

		createQualityDimensionsWeights();

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
			qualityDimensionWeight = HelperAcessDB
					.loadQualityDimensionWeight(weight);

			createContextQualityDimensionWeight(dataSet, qualityDimension,
					qualityDimensionWeight);
		}

	}

	private static Collection<QualityDimensionWeight> createQualityDimensionsWeights()
			throws Exception {
		Collection<QualityDimensionWeight> qualityDimensionWeights = new ArrayList<QualityDimensionWeight>();
		QualityDimensionWeight qualityDimensionWeight = null;

		qualityDimensionWeight = createQualityDimensionWeight(0,
				"Indicates that the evaluated quality dimension has no importance.");
		qualityDimensionWeights.add(qualityDimensionWeight);
		qualityDimensionWeight = createQualityDimensionWeight(1,
				"Indicates that the evaluated quality dimension has a small importance.");
		qualityDimensionWeights.add(qualityDimensionWeight);
		qualityDimensionWeight = createQualityDimensionWeight(
				2,
				"Indicates that the evaluated quality dimension is important in some circumstances, but not always.");
		qualityDimensionWeights.add(qualityDimensionWeight);
		qualityDimensionWeight = createQualityDimensionWeight(3,
				"Indicates that the evaluated quality dimension is very important.");
		qualityDimensionWeights.add(qualityDimensionWeight);
		qualityDimensionWeight = createQualityDimensionWeight(4,
				"Indicates that the evaluated quality dimension is essential.");
		qualityDimensionWeights.add(qualityDimensionWeight);

		return qualityDimensionWeights;

	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static Collaborator createCollaboratorFoxSet() throws Exception {
		Collaborator collaborator = createCollaborator("foxset@cos.ufrj.br",
				"FoxSet", "foxset", "foxset", true, true, true);
		return collaborator;
	}

	/**
	 * @param dataSet
	 * @param qualityDimension
	 * @param qualityDimensionWeight
	 * @throws Exception
	 */
	public static ContextQualityDimensionWeight createContextQualityDimensionWeight(
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
	public static QualityDimensionWeight createQualityDimensionWeight(
			int weight, String description) throws Exception {
		QualityDimensionWeight qualityDimensionWeight = null;
		qualityDimensionWeight = new QualityDimensionWeight();
		qualityDimensionWeight.setDescription(description);
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
	public static QualityDimension createQualityDimension(
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
	public static SeedDocument createSeedDocument(DataSet dataSet,
			String domain, String url) throws Exception {
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
	 * @param method
	 * @param dataSetFather
	 * @return
	 * @throws Exception
	 */
	public static DataSet createDataSet(Collaborator collaborator,
			String context, String description, Language language,
			int minQuantityPages, char status, char method) throws Exception {
		return createDataSet(collaborator, context, description, language,
				minQuantityPages, null, status, method);
	}

	/**
	 * 
	 * @param collaborator
	 * @param context
	 * @param description
	 * @param language
	 * @param minQuantityPages
	 * @param status
	 * @param method
	 * @return
	 * @throws Exception
	 */
	public static DataSet createDataSet(Collaborator collaborator,
			String context, String description, Language language,
			int minQuantityPages, Integer pOfN, char status, char method)
			throws Exception {
		return createDataSet(collaborator, context, description, language,
				minQuantityPages, pOfN, status, method, null);
	}

	/**
	 * @param language
	 * @param collaborator
	 * @param status
	 * @param method
	 * @param dataSetFather
	 * @return
	 * @throws Exception
	 */
	public static DataSet createDataSet(Collaborator collaborator,
			String context, String description, Language language,
			int minQuantityPages, Integer pOfN, char status, char method,
			DataSet dataSetFather) throws Exception {
		DataSet dataSet = new DataSet();
		dataSet.setCollaborator(collaborator);
		dataSet.setContext(context);
		dataSet.setCreationDate(new Date());
		dataSet.setDescription(description);
		dataSet.setLanguage(language);
		dataSet.setMinQuantityPages(minQuantityPages);
		dataSet.setPOfN(pOfN);
		dataSet.setStatus(status);
		dataSet.setMethod(method);
		dataSet.setDataSetFather(dataSetFather);
		dataSet = (DataSet) create(dataSet);
		return dataSet;
	}

	/**
	 * 
	 * @param email
	 * @param name
	 * @param password
	 * @param username
	 * @param administrator
	 * @param coordinator
	 * @param active
	 * @return
	 * @throws Exception
	 */
	public static Collaborator createCollaborator(String email, String name,
			String password, String username, boolean administrator,
			boolean coordinator, boolean active) throws Exception {
		Collaborator collaborator = new Collaborator();
		collaborator.setAdministrator(administrator);
		collaborator.setCoordinator(coordinator);
		collaborator.setEmail(email);
		collaborator.setName(name);
		collaborator.setPassword(password);
		collaborator.setUsername(username);
		collaborator.setActive(active);

		collaborator = (Collaborator) create(collaborator);
		return collaborator;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public static Language createLanguage(String name) throws Exception {
		Language language = new Language();
		language.setName(name);
		language = (Language) create(language);
		return language;
	}

	public static void limparDB() throws Exception {

		// Collection<DocumentQualityDimension> documentQualityDimensions =
		// (Collection<DocumentQualityDimension>) getDao()
		// .listAll(DocumentQualityDimension.class);
		// for (DocumentQualityDimension documentQualityDimension :
		// documentQualityDimensions) {
		// getDao().remove(documentQualityDimension);
		// }

		Collection<DataSet> dataSetsWhitoutFather = HelperAcessDB
				.loadAllDataSetWhitoutFather();
		Collection<DataSet> dataSetsWhitFather = HelperAcessDB
				.loadAllDataSetWhitFather();
		Collection<DataSet> dataSets = new ArrayList<DataSet>();
		dataSets.addAll(dataSetsWhitFather);
		dataSets.addAll(dataSetsWhitoutFather);
		for (DataSet dataSet : dataSets) {
			Collection<Document> documents = HelperAcessDB
					.findRootDocuments(dataSet);
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

		for (DataSet dataSet : dataSetsWhitoutFather) {
			removeDataSet(dataSet);
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

	public static void removeDataSet(DataSet dataSet) throws Exception {
		DataSet dataSetChild = HelperAcessDB.loadDataSetChild(dataSet);
		if (dataSetChild != null)
			removeDataSet(dataSetChild);
		getDao().remove(dataSet);
	}

	public static void removeDocument(Document fatherDocument) throws Exception {
		List<Document> childDocuments = HelperAcessDB
				.loadDocumentsByFather(fatherDocument);
		for (Document childDocument : childDocuments) {
			removeDocument(childDocument);
			getDao().remove(childDocument);
		}

	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static Object create(Object obj) throws Exception {
		// org.hibernate.Transaction t =
		// getDao().getSession().beginTransaction();
		getDao().create(obj);
		// t.commit();
		return obj;
	}

	public static Object update(Object obj) throws Exception {
		// org.hibernate.Transaction t =
		// getDao().getSession().beginTransaction();

		getDao().update(obj);
		// t.commit();
		return obj;
	}

	public static Object remove(Object obj) throws Exception {
		// org.hibernate.Transaction t =
		// getDao().getSession().beginTransaction();

		getDao().remove(obj);
		// t.commit();
		return obj;
	}

	public static List<Object[]> listAll(String tableName) {
		return getDao().listAll(tableName);
	}

	/**
	 * @return the dao
	 */
	public static HibernateDAO getDao() {
		return dao;
	}

	/**
	 * @param dao
	 *            the dao to set
	 */
	private static void setDao(HibernateDAO daoInstance) {
		dao = daoInstance;
	}

}