/**
 * 
 */
package engine.DB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import beans.Artigo;
import beans.Item;
import beans.Query;
import beans.Score;
import beans.words.Word_Artigos;
import beans.words.Word_ArtigosAbstracT;
import beans.words.Word_ArtigosAll;
import beans.words.Word_ArtigosTitle;

/**
 * @author Fabricio
 * 
 */
public class PopulateDB {

	private SessionFactory sessionFactory;

	private HibernateDAO dao;

	public PopulateDB() {

		setDao(createDao()); // TODO
	}

	public final void popular(List<Artigo> artigos, List<Item> itens,
			List<Query> queries) {

		artigos = persistirArtigos(artigos);
		List<Score> scores = persistirScores(itens);
		queries = persistirQueries(queries);
		itens = persistirItens(itens, queries, scores);

	}

	private List<Artigo> persistirArtigos(List<Artigo> artigos) {
		List<Artigo> new_artigos = new ArrayList<Artigo>();
		for (Iterator<Artigo> iterator = artigos.iterator(); iterator.hasNext();) {
			Artigo artigo = iterator.next();
			// List<Artigo> artigosPer = (List<Artigo>) getDao().loadByField(
			// Artigo.class, "numero", artigo);
			// if (artigosPer.size() == 0) {
			artigo = (Artigo) create(artigo);
			// } else {
			// artigo = artigosPer.get(0);
			// }
			new_artigos.add(artigo);
		}
		return new_artigos;
	}

	private List<Score> persistirScores(List<Item> itens) {
		List<Score> scores = new ArrayList<Score>();
		for (Iterator<Item> iterator = itens.iterator(); iterator.hasNext();) {
			Item item = iterator.next();
			Score score = new Score(item.getNotas());
			if (!scores.contains(score)) {
				scores.add(score);
			}
		}

		List<Score> new_scores = new ArrayList<Score>();
		for (Iterator<Score> iterator = scores.iterator(); iterator.hasNext();) {
			Score score = iterator.next();
			// List<Score> scoresPer = (List<Score>) getDao().loadByField(
			// Score.class, "notas", score);
			// if (scoresPer.size() == 0) {
			score = (Score) create(score);
			// } else {
			// score = scoresPer.get(0);
			// }
			new_scores.add(score);
		}
		return new_scores;
	}

	private List<Query> persistirQueries(List<Query> queries) {
		List<Query> new_queries = new ArrayList<Query>();
		for (Iterator<Query> iterator = queries.iterator(); iterator.hasNext();) {
			Query query = iterator.next();
			// List<Query> queriesPer = (List<Query>) getDao().loadByField(
			// Query.class, "numero", query);
			// if (queriesPer.size() == 0) {
			query = (Query) create(query);
			// } else {
			// query = queriesPer.get(0);
			// }
			new_queries.add(query);
		}
		return new_queries;
	}

	private List<Item> persistirItens(List<Item> itens, List<Query> queries,
			List<Score> scores) {
		List<Item> temp_itens = new ArrayList<Item>();
		for (Iterator<Item> iterator = itens.iterator(); iterator.hasNext();) {
			Item item = iterator.next();

			List<?> score = getDao().loadByField(Score.class, "notas",
					item.getNotas());
			System.out.println(score.size());
			item.setScore((Score) score.get(0));

			List<?> artigo = getDao().loadByField(Artigo.class, "numero",
					item.getNumeroArtigo());
			System.out.println(artigo.size());
			item.setArtigo((Artigo) artigo.get(0));

			List<?> querys = getDao().loadByField(Query.class, "numero",
					item.getNumeroQuery());
			System.out.println(querys.size());
			Query query = (Query) querys.get(0);
			item.setQuery(query);

			temp_itens.add(item);
		}

		List<Item> new_itens = new ArrayList<Item>();
		for (Iterator<Item> iterator = temp_itens.iterator(); iterator
				.hasNext();) {
			Item item = iterator.next();
			item = (Item) create(item);
			/*
			 * Query q = item.getQuery(); q.addItem(item); update(q);
			 */
			new_itens.add(item);
		}
		return new_itens;
	}

	/**
	 * Cria um HibernateDAO com uma sessão criada a partir do sessionFactory.
	 * 
	 * @return Um HibernateDAO instanciado e com sessão.
	 */
	private HibernateDAO createDao() {
		HibernateDAO hibernateDAO = new HibernateDAO();
		hibernateDAO.setSession(createSession());
		return hibernateDAO;
	}

	/**
	 * Cria uma sessão do Hibernate a aprtir do sessionFactory.
	 * 
	 * @return Uma sessão do Hibernate aberta.
	 */
	private Session createSession() {

		AnnotationConfiguration annotationCfg = new AnnotationConfiguration();
		annotationCfg.configure("./hibernate.cfg.xml");

		annotationCfg.addAnnotatedClass(Artigo.class);
		annotationCfg.addAnnotatedClass(Item.class);
		annotationCfg.addAnnotatedClass(Score.class);
		annotationCfg.addAnnotatedClass(Query.class);
		annotationCfg.addAnnotatedClass(Word_Artigos.class);
		annotationCfg.addAnnotatedClass(Word_ArtigosTitle.class);
		annotationCfg.addAnnotatedClass(Word_ArtigosAbstracT.class);
		annotationCfg.addAnnotatedClass(Word_ArtigosAll.class);

		sessionFactory = annotationCfg.buildSessionFactory();
		return sessionFactory.openSession();
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public Object create(Object obj) {
		org.hibernate.Transaction t = getDao().getSession().beginTransaction();
		getDao().create(obj);
		t.commit();
		return obj;
	}

	public Object update(Object obj) {
		org.hibernate.Transaction t = getDao().getSession().beginTransaction();

		getDao().update(obj);
		t.commit();
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
}