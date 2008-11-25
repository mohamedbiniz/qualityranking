package br.ufrj.cos.db;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.ufrj.cos.bean.Answer;
import br.ufrj.cos.bean.AssessmentScale;
import br.ufrj.cos.bean.Collaborator;
import br.ufrj.cos.bean.ContextQualityDimensionWeight;
import br.ufrj.cos.bean.DataSet;
import br.ufrj.cos.bean.DataSetCollaborator;
import br.ufrj.cos.bean.Document;
import br.ufrj.cos.bean.DocumentData;
import br.ufrj.cos.bean.DocumentQualityDimension;
import br.ufrj.cos.bean.Language;
import br.ufrj.cos.bean.Metadata;
import br.ufrj.cos.bean.QualityDimension;
import br.ufrj.cos.bean.QualityDimensionWeight;
import br.ufrj.cos.bean.Query;
import br.ufrj.cos.bean.SeedDocument;
import br.ufrj.cos.bean.SubSet;
import br.ufrj.cos.bean.SubSetQualityDimension;
import br.ufrj.cos.bean.SubSetQuery;
import br.ufrj.cos.util.helper.ReflectionHelper;

/**
 * Faz a persistência dos Beans no banco de dados utilizando Hibernate..
 * 
 * @author Fabricio
 * @version 0.0.1
 * @created 22-out-2007 08:40:09
 */
public class HibernateDAO {

	private SessionFactory sessionFactory;

	public static HibernateDAO dao;

	public static HibernateDAO getInstance() {
		if (dao == null) {
			dao = new HibernateDAO();
		}
		return dao;
	}

	private HibernateDAO() {
		setSession(createSession());
	}

	/**
	 * Cria uma sess�o do Hibernate a aprtir do sessionFactory.
	 * 
	 * @return Uma sess�o do Hibernate aberta.
	 */
	private Session createSession() {

		AnnotationConfiguration annotationCfg = new AnnotationConfiguration();
		annotationCfg.configure("./hibernate_annotation.cfg.xml");

		annotationCfg.addAnnotatedClass(Answer.class);
		annotationCfg.addAnnotatedClass(AssessmentScale.class);
		annotationCfg.addAnnotatedClass(Collaborator.class);
		annotationCfg.addAnnotatedClass(ContextQualityDimensionWeight.class);
		annotationCfg.addAnnotatedClass(DataSet.class);
		annotationCfg.addAnnotatedClass(DataSetCollaborator.class);
		annotationCfg.addAnnotatedClass(Document.class);
		annotationCfg.addAnnotatedClass(DocumentData.class);
		annotationCfg.addAnnotatedClass(DocumentQualityDimension.class);
		annotationCfg.addAnnotatedClass(Language.class);
		annotationCfg.addAnnotatedClass(Metadata.class);
		annotationCfg.addAnnotatedClass(QualityDimension.class);
		annotationCfg.addAnnotatedClass(QualityDimensionWeight.class);
		annotationCfg.addAnnotatedClass(Query.class);
		annotationCfg.addAnnotatedClass(SeedDocument.class);
		annotationCfg.addAnnotatedClass(SubSet.class);
		annotationCfg.addAnnotatedClass(SubSetQualityDimension.class);
		annotationCfg.addAnnotatedClass(SubSetQuery.class);

		// annotationCfg.addAnnotatedClass(Context.class);
		// annotationCfg.addAnnotatedClass(ContextQualityDimensionWeight.class);
		// annotationCfg.addAnnotatedClass(LinguisticTerm.class);
		// annotationCfg.addAnnotatedClass(MembershipDegree.class);

		// annotationCfg.addAnnotatedClass(OutputLink.class);

		sessionFactory = annotationCfg.buildSessionFactory();
		return sessionFactory.openSession();
	}

	/**
	 * Contexto da sessão do Hibernate.
	 */
	private Session session = null;

	/**
	 * Transação para a sessão do hibernate.
	 */
	private Transaction transaction = null;

	/**
	 * Define a sessão a ser utilizada.
	 * 
	 * @param session -
	 *            A sessão a ser utilizada.
	 */
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * Retorna a sessão que está sendo utilizada.
	 * 
	 * @return A sessão que está sendo utilizada.
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * 
	 * @return
	 */
	public Session openSession() {
		if (!getSession().isOpen())
			setSession(getSessionFactory().openSession());
		return getSession();
	}

	/**
	 * 
	 * @return
	 */
	public void closeSession() {
		if (getSession() != null && getSession().isOpen())
			getSession().close();
	}

	/**
	 * Retorna a transação que está sendo utilizada.
	 * 
	 * @return A transação que está sendo utilizada.
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * Salva um objeto no banco. Esse objeto deve ser um {@link BaseBean}
	 * anotado utilizando a JPA.
	 * 
	 * @param obj -
	 *            O objeto a ser salvo.
	 * @return O valor do campo identificador do objeto.
	 * @throws Exception
	 */
	public synchronized Serializable create(Object obj) throws Exception {
		try {
			initTransaction();
			Serializable idGerado = session.save(obj);
			commitTransaction();
			return idGerado;
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * Atualiza um objeto no banco. Esse objeto deve ser um {@link BaseBean}
	 * anotado utilizando a JPA.
	 * 
	 * @param obj -
	 *            O objeto a ser atualizado.
	 * @throws Exception
	 */
	public synchronized void update(final Object obj) throws Exception {
		try {
			initTransaction();
			session.flush();
			session.clear();
			session.update(obj);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * Remove um objeto no banco. Esse objeto deve ser um {@link BaseBean}
	 * anotado utilizando a JPA.
	 * 
	 * @param obj -
	 *            O objeto a ser removido.
	 * @throws Exception
	 */
	public synchronized void remove(final Object obj) throws Exception {
		try {
			initTransaction();
			session.delete(obj);
			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw e;
		}
	}

	/**
	 * Carrega um objeto no banco pelo seu identificador. Esse objeto deve ser
	 * um {@link BaseBean} anotado utilizando a JPA.
	 * 
	 * @param klass -
	 *            A classe do objeto a ser carregado.
	 * 
	 * @param id -
	 *            O identificador do objeto.
	 */
	public Object loadById(Class<?> klass, Serializable id) {
		initTransaction();
		Object obj = null;
		try {
			obj = session.load(klass, id, LockMode.READ);
		} catch (ObjectNotFoundException e) {
			// nothing
		}
		return obj;
	}

	/**
	 * Encontra um objeto no banco baseado em um objeto de exemplo.
	 * 
	 * @param example -
	 *            O objeto de exemplo.
	 * @param excludeParams -
	 *            Os campos do objeto de exemplo que deve ser desconsiderados na
	 *            busca.
	 * @return - Uma lista de objetos que satisfaz os critérios de busca.
	 */

	public List<?> findByExampleClass(Class<?> klass, final Object example,
			List<String> includeParams) {
		List<String> excludeParams = new ArrayList<String>();
		Field[] fields = klass.getFields();
		for (Field field : fields) {
			if (!includeParams.contains(field.getName()))
				excludeParams.add(field.getName());
		}
		return findByExample(example, excludeParams);
	}

	@SuppressWarnings("unchecked")
	public List<?> findByExample(final Object example,
			List<String> excludeParams) {
		initTransaction();
		Criteria criteria = session.createCriteria(example.getClass());
		// criando um exemplo
		Example exampleObject = Example.create(example);
		// habilitando ignore case
		exampleObject.ignoreCase();
		// habilitando procura por like
		exampleObject.enableLike(MatchMode.ANYWHERE);
		// retirando os zeros e nulos da pesquisa
		exampleObject.excludeZeroes();
		// removendo do example os atributos que n�o est�o no formulario de
		// pesquisa
		for (String exclude : excludeParams) {
			exampleObject.excludeProperty(exclude);
		}

		criteria.add(exampleObject);

		Field key = ReflectionHelper.getIdField(example.getClass());
		if (!excludeParams.contains(key.getName())) {
			Object fieldValue = ReflectionHelper.getFieldValue(example, key);
			criteria.add(Restrictions.eq(key.getName(), fieldValue));
		}
		// procurando por composi��o dentro da class
		for (Field field : ReflectionHelper.listAllFields(example.getClass())) {
			if (ReflectionHelper.inheritsFrom(field.getType(), Object.class)) {
				Object fieldValue = ReflectionHelper.getFieldValue(example,
						field);
				if (fieldValue != null) {
					criteria.add(Restrictions.eq(field.getName(), fieldValue));
				}
			}
			// TODO vericar procedimento caso campo seja uma Collection
		}

		final List<?> result = criteria.list();
		return result;
	}

	/**
	 * Encontra um objeto no banco baseado em um objeto de exemplo. Esse método
	 * pode ser utilizado quando se precisa adicionar manualmente outros
	 * critérios na busca.
	 * 
	 * @param example -
	 *            O objeto de exemplo.
	 * @param excludeParams -
	 *            Os campos do objeto de exemplo que deve ser desconsiderados na
	 *            busca.
	 * @param criteria -
	 *            A Criteria que deve ser utilizada para fazer a busca.
	 * @return - Uma lista de objetos que satisfaz os critérios de busca.
	 */
	/*
	 * @SuppressWarnings("unchecked") public List<Object> findByExample(final
	 * Object example, List<String> excludeParams, Criteria criteria) {
	 * initTransaction(); // criando um exemplo Example exampleObject =
	 * Example.create(example); // habilitando ignore case
	 * exampleObject.ignoreCase(); // habilitando procura por like
	 * exampleObject.enableLike(MatchMode.ANYWHERE); // retirando os zeros e
	 * nulos da pesquisa // exampleObject.excludeZeroes(); // removendo do
	 * example os atributos que não estão no formulario // de pesquisa for
	 * (String exclude : excludeParams) {
	 * exampleObject.excludeProperty(exclude); }
	 * 
	 * criteria.add(exampleObject);
	 * 
	 * Field key = ReflectHelper.getIdField(example.getClass()); if
	 * (!excludeParams.contains(key.getName())) { Object fieldValue =
	 * ReflectHelper.getFieldValue(example, key);
	 * criteria.add(Restrictions.eq(key.getName(), fieldValue)); } // procurando
	 * por composição dentro da class for (Field field :
	 * ReflectHelper.listAllFields(example.getClass())) { if
	 * (ReflectHelper.inheritsFrom(field.getType(), BaseBean.class)) { Object
	 * fieldValue = ReflectHelper.getFieldValue(example, field); if (fieldValue !=
	 * null) { criteria.add(Restrictions.eq(field.getName(), fieldValue)); } } //
	 * TODO vericar procedimento caso campo seja uma Collection }
	 * 
	 * final List<Object> result = criteria.list(); return result; }
	 */

	/**
	 * Lista todos os objetos de uma classe.
	 * 
	 * @param klass -
	 *            A classe a ser listada.
	 * @return Uma lista de objetos daquela classe.
	 */
	public List<?> listAll(Class<?> klass) {
		initTransaction();
		session.flush();
		session.clear();
		Criteria criteria = session.createCriteria(klass);
		final List<?> result = criteria.list();
		return result;
	}

	/**
	 * Lista objetos de acordo com algum campo.
	 * 
	 * @param klass -
	 *            A classe do objeto.
	 * @param fieldName -
	 *            O nome do campo a ser buscado.
	 * @param value -
	 *            O valor do campo a ser buscado.
	 * @return - Uma lista de objetos cujo campo fieldName possui o valor value.
	 */
	public List<?> loadByField(Class<?> klass, String fieldName, Object value) {
		Criteria criteria = session.createCriteria(klass).add(
				Restrictions.eq(fieldName, value));
		return criteria.list();
	}

	public List<?> loadByFieldPk(Class<?> klass, String fieldName, Object value) {
		Criteria criteria = session.createCriteria(klass).add(
				Restrictions.like(fieldName, value));
		return criteria.list();
	}

	/**
	 * Busca um objeto de acordo com algum campo único.
	 * 
	 * @param klass -
	 *            A classe do objeto.
	 * @param fieldName -
	 *            O nome do campo a ser buscado.
	 * @param value -
	 *            O valor do campo a ser buscado.
	 * @return - Um objeto cujo campo fieldName possui o valor value.
	 */
	public Object loadByUniqueField(Class<?> klass, String fieldName,
			Object value) {
		Criteria criteria = session.createCriteria(klass).add(
				Restrictions.eq(fieldName, value));
		return criteria.uniqueResult();
	}

	/**
	 * Inicia a transação.
	 */
	public void beginTransation() {
		transaction = session.beginTransaction();
	}

	/**
	 * Envia a transação atual.
	 */
	public void commitTransaction() {
		if (transaction != null) {
			transaction.commit();
		}
	}

	/**
	 * Desfaz a transação atual.
	 */
	public void rollbackTransaction() {
		if (transaction != null) {
			transaction.rollback();
		}
	}

	/**
	 * Inicializa a transação caso ela ainda não esteja iniciada.
	 */
	private void initTransaction() {
		if (!wasStarted()) {
			beginTransation();
		}
	}

	/**
	 * Verifica se uma transação foi enviada.
	 * 
	 * @return true caso ela tenha sido enviada, false caso contrário.
	 */
	public boolean wasCommitted() {
		if (transaction != null) {
			return transaction.wasCommitted();
		} else {
			return false;
		}
	}

	/**
	 * Verifica se uma transação foi desfeita.
	 * 
	 * @return true caso ela tenha sido desfeita, false caso contrário.
	 */
	public boolean wasRolledBack() {
		if (transaction != null) {
			return transaction.wasRolledBack();
		} else {
			return false;
		}
	}

	/**
	 * Verifica se uma transação foi iniciada.
	 * 
	 * @return true caso ela tenha sido iniciada, false caso contrário.
	 */
	public boolean wasStarted() {
		return (transaction != null) && (transaction.isActive());
	}

	public List<Object[]> listAll(String tableName) {
		String queryString = String.format("SELECT * FROM %s;", tableName);
		SQLQuery sqlQuery = session.createSQLQuery(queryString);

		return sqlQuery.list();
	}

	public List<Object[]> loadByField(String tableName, String fieldName,
			Object value) {
		String queryString = String.format("SELECT * FROM %s WHERE %s=?;",
				tableName, fieldName);
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setParameter(0, value);

		return sqlQuery.list();
	}

	public int countAll(String tableName) {
		String queryString = String.format("SELECT count(*) FROM %s;",
				tableName);
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		return ((BigInteger) sqlQuery.uniqueResult()).intValue();
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}