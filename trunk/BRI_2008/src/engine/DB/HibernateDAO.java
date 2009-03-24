package engine.DB;

import java.io.Serializable;
import java.util.List;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

/**
 * Faz a persistência dos Beans no banco de dados utilizando Hibernate..
 * 
 * @author Fabricio
 * @version 0.0.1
 * @created 22-out-2007 08:40:09
 */
public class HibernateDAO {

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
	 */
	public Serializable create(Object obj) {
		initTransaction();
		Serializable idGerado = session.save(obj);
		return idGerado;
	}

	/**
	 * Atualiza um objeto no banco. Esse objeto deve ser um {@link BaseBean}
	 * anotado utilizando a JPA.
	 * 
	 * @param obj -
	 *            O objeto a ser atualizado.
	 */
	public void update(final Object obj) {
		initTransaction();
		session.flush();
		session.clear();
		session.update(obj);
	}

	/**
	 * Remove um objeto no banco. Esse objeto deve ser um {@link BaseBean}
	 * anotado utilizando a JPA.
	 * 
	 * @param obj -
	 *            O objeto a ser removido.
	 */
	public void remove(final Object obj) {
		initTransaction();
		session.delete(obj);
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
	/*
	 * @SuppressWarnings("unchecked") public List<Object> findByExample(final
	 * Object example, List<String> excludeParams) { initTransaction();
	 * Criteria criteria = session.createCriteria(example.getClass()); //
	 * criando um exemplo Example exampleObject = Example.create(example); //
	 * habilitando ignore case exampleObject.ignoreCase(); // habilitando
	 * procura por like exampleObject.enableLike(MatchMode.ANYWHERE); //
	 * retirando os zeros e nulos da pesquisa // exampleObject.excludeZeroes(); //
	 * removendo do example os atributos que não estão no formulario // de
	 * pesquisa for (String exclude : excludeParams) {
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

	public List<?> searchBoolean(String tableName, String fieldName,
			String value) {

		String queryString = String.format("SELECT * FROM %s WHERE MATCH (%s) "
				+ "AGAINST (? IN BOOLEAN MODE);", tableName, fieldName);
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setParameter(0, value);

		return sqlQuery.list();

	}

	public List<?> searchFullTextHibernate(Class klass, String fieldName,
			String value) throws ParseException {

		QueryParser parser = new QueryParser(fieldName, new StopAnalyzer());

		FullTextSession LuceneSession = Search.createFullTextSession(session);

		Query LuceneQuery = parser.parse(value);

		FullTextQuery fullTextQuery = LuceneSession.createFullTextQuery(
				LuceneQuery, klass);

		return fullTextQuery.list();

	}

	public List<?> searchFullText(String tableName, String fieldName,
			String value) {

		String queryString = String.format("SELECT * FROM %s WHERE MATCH (%s) "
				+ "AGAINST (?);", tableName, fieldName);
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setParameter(0, value);

		return sqlQuery.list();

	}

	public List<?> searchFullTextExt(String tableName, String fieldName,
			String value) {

		String queryString = String.format("SELECT * FROM %s WHERE MATCH (%s) "
				+ "AGAINST (? WITH QUERY EXPANSION);", tableName, fieldName);
		SQLQuery sqlQuery = session.createSQLQuery(queryString);
		sqlQuery.setParameter(0, value);

		return sqlQuery.list();
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
}