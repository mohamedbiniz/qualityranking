/**
 * 
 */
package trab01;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.queryParser.ParseException;
import org.hibernate.SQLQuery;
import org.jdom.JDOMException;

import trab01.ploter.Ploter;
import beans.Artigo;
import beans.Item;
import beans.Query;
import engine.DB.HibernateDAO;
import engine.DB.PopulateDB;
import engine.XML.Loader;

/**
 * @author Fabricio
 * 
 */
public class MainProcess01 {

	public static final String PATH_DOCS = "C:/workspace/BRI/docs/";
	public static final String PATH_RESULTS = "C:/workspace/BRI/result/";
	// public static final String PATH_DOCS =
	// "/home/fabriciorsf/workspace/BRI/docs/";

	protected static PopulateDB p;

	public static void main(String[] args) {
		etapa01(false, true);
		p.getDao().getSession().close();
	}

	protected static void etapa01(boolean popular, boolean calcular) {
		try {

			List<Query> queries = popularBancoDeDados(popular);

			if (calcular) {
				calcularPR(queries);
			}

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		} catch (ParseException e) {
			e.printStackTrace();

		}
	}

	private static void calcularPR(List<Query> queries) throws ParseException {
		// TreeMap<Integer, Double>[][] r = realizarConsultas(queries);
		TreeMap<Integer, Double>[][] r = new TreeMap[3][3];
		P_R[][] p_r = new P_R[3][3];

		String[] fieldsName = new String[3];
		fieldsName[0] = "title";
		fieldsName[1] = "abstracT";
		fieldsName[2] = "corpo";

		for (int i = 0; i < p_r.length; i++) {// varia o campo
			for (int j = 0; j < p_r[i].length; j++) {// varia a tecnica
				TreeMap<Integer, Double> resultsConsultas = realizarConsultas(
						queries, fieldsName[i], j, null);
				r[i][j] = resultsConsultas;
				p_r[i][j] = P_R.treeMapToP_R(resultsConsultas);
				// p_r[i][j] = P_R.treeMapToP_R(r[i][j]);
			}
		}

		imprimir(p_r);

		Ploter.print(r);
	}

	static int count = 0;

	static TreeMap<Query, Set<Artigo>> listsAllArtigosByQuery = new TreeMap<Query, Set<Artigo>>();

	protected static TreeMap<Integer, Double> realizarConsultas(
			Collection<Query> queries, String fieldName, int j,
			HashMap<Query, Collection<Artigo>> results) throws ParseException {
		HibernateDAO dao = p.getDao();
		String tableName = "Artigos";

		TreeMap<Integer, Double> resultadosSum = new TreeMap<Integer, Double>();
		TreeMap<Integer, Double>[] resultados = new TreeMap[queries.size()];

		int q = 0;
		for (Query query : queries) {

			if (!listsAllArtigosByQuery.keySet().contains(query)) {
				listsAllArtigosByQuery.put(query, listAllArtigosByQuery(dao,
						query));
			}

			P_R resultsQuery = consulta(j, dao, tableName, fieldName, query,
					listsAllArtigosByQuery.get(query), results);

			resultados[q] = interpolar(resultsQuery);
			q++;
			System.out.println(String.format("\n%d", ++count));

		}
		resultadosSum = P_R
				.getAvaragesByRecallLevel(resultados, queries.size());
		// para cada query tem-se os 11 nives de recalll, cada qual com
		// o seu
		// max(precisao)

		// para cada cunjunto de 100 querys pega-se a media de cada
		// nivel de
		// revocacao

		return resultadosSum;

	}

	protected static void imprimir(P_R[][] p_r) {
		for (int i = 0; i < p_r.length; i++) {
			System.out.println("Campo Utilizado: " + i + "\n");
			for (int j = 0; j < p_r[i].length; j++) {
				System.out.println("Tipo de Consulta: " + j + "\n");
				imprimirPR(p_r[i][j]);
			}
			System.out.println("\n");
		}
	}

	protected static void imprimirPR(P_R p_r) {
		System.out.println(p_r + "\n");
	}

	private static TreeMap<Integer, Double> interpolar(P_R p_r) {
		TreeMap<Integer, List<Double>> agregacao = new TreeMap<Integer, List<Double>>();
		final int intervaleLen = 10;
		for (int i = P_R.QTD_PTS - 1; i >= 0; i--) {
			int ri = i * intervaleLen;
			List<Double> precisions = findByIntervaleOfRecall(p_r, ri
					- intervaleLen, ri);
			agregacao.put(ri, precisions);
		}
		TreeMap<Integer, Double> agregacaoMax = new TreeMap<Integer, Double>();
		Object[] keys = agregacao.keySet().toArray();
		double max = 0;
		for (int k = keys.length - 1; k >= 0; k--) {
			Integer recallLevel = (Integer) keys[k];
			max = getMax(max, agregacao.get(recallLevel));
			System.out.println(String.format(
					"Revocacao: %03d\t MaxPrecisao: %03.5f", recallLevel, max));
			agregacaoMax.put(recallLevel, max);
		}

		return agregacaoMax;
	}

	private static Double getMax(double maxOld, List<Double> list) {
		Double max = new Double(maxOld);
		for (Double d : list) {
			if (max < d) {
				max = d;
			}
		}
		return max;
	}

	private static List<Double> findByIntervaleOfRecall(P_R p_r, int rInf,
			int rSup) {
		if (rInf < 0)
			rInf = 0;
		List<Double> precisions = new ArrayList<Double>();
		for (Iterator<Integer> iterator = p_r.getPrecisao().keySet().iterator(); iterator
				.hasNext();) {
			Integer key = iterator.next();
			if (p_r.getRevocacao().get(key) >= rInf
					&& p_r.getRevocacao().get(key) <= rSup) {
				precisions.add(p_r.getPrecisao().get(key));
			}
		}
		return precisions;
	}

	private static P_R consulta(int tipo, HibernateDAO dao, String tableName,
			String fieldName, Query query, Set<Artigo> allArtigosRelevantes,
			HashMap<Query, Collection<Artigo>> resultsPrev) throws ParseException {
		Collection<?> results = new ArrayList<Object>();
		String text = query.getText().trim().replace('\n', ' ');
		switch (tipo) {
		case 0:
			results = dao.searchBoolean(tableName, fieldName, text);
			break;
		case 1:
			results = dao.searchFullText(tableName, fieldName, text);
			break;
		case 2:
			results = dao.searchFullTextExt(tableName, fieldName, text);
			break;
		case 3:
			return new P_R(resultsPrev.get(query), allArtigosRelevantes);
		}

		List<Artigo> artigos = listObjToListArtigo(results);

		return new P_R(artigos, allArtigosRelevantes);
	}

	private static Set<Artigo> listAllArtigosByQuery(HibernateDAO dao,
			Query query) {
		String stringQuery = String
				.format("select a.id, a.abstracT, a.corpo, a.numero, a.title "
						+ "from artigos as a, queries as q "
						+ "where exists (select 1 from itens as i "
						+ "where i.query_id=? and i.artigo_id=a.id) "
						+ "group by a.id, a.abstracT, a.corpo, a.numero, a.title;");
		SQLQuery sqlQuery = dao.getSession().createSQLQuery(stringQuery);
		sqlQuery.setParameter(0, query.getId());
		List<?> artigosRel = sqlQuery.list();

		Set<Artigo> allArtigosRelevantes = listObjToSetArtigo(artigosRel);
		return allArtigosRelevantes;
	}

	protected static List<Artigo> listObjToListArtigo(Collection<?> results) {
		// String resultStr = "";
		List<Artigo> artigos = new ArrayList<Artigo>();
		for (Object obj : results) {
			Artigo artigo = objToArtigo(obj);
			// resultStr = resultStr + artigo + "\n";
			artigos.add(artigo);
		}
		// imprimir(String.format("artigos %03d", query.getNumero()),
		// resultStr);
		return artigos;
	}

	protected static List<Query> listObjToListQuery(List<?> results) {
		List<Query> queries = new ArrayList<Query>();
		for (Object obj : results) {
			Query query = objToQuery(obj);

			queries.add(query);
		}

		return queries;
	}

	private static Query objToQuery(Object obj) {
		Query query = null;
		if (obj instanceof Object[]) {
			Object[] fields = (Object[]) obj;
			query = new Query();
			query.setId(((BigInteger) fields[0]).longValue());
			query.setNumero((Integer) fields[1]);
			query.setText((String) fields[2]);

			// query.setItens();
		}
		return query;
	}

	protected static Set<Artigo> listObjToSetArtigo(List<?> results) {
		// String resultStr = "";
		Set<Artigo> artigos = new TreeSet<Artigo>();
		for (Object obj : results) {
			Artigo artigo = objToArtigo(obj);
			// resultStr = resultStr + artigo + "\n";
			artigos.add(artigo);
		}
		// imprimir(String.format("artigos %03d", query.getNumero()),
		// resultStr);
		return artigos;
	}

	private static Artigo objToArtigo(Object obj) {
		Artigo artigo = null;
		if (obj instanceof Object[]) {
			Object[] fields = (Object[]) obj;
			artigo = new Artigo();
			artigo.setId(((BigInteger) fields[0]).longValue());
			artigo.setAbstracT((String) fields[1]);
			artigo.setCorpo((String) fields[2]);
			artigo.setNumero((Integer) fields[3]);
			artigo.setTitle((String) fields[4]);
		} else if (obj instanceof Artigo) {
			artigo = (Artigo) obj;
		}
		return artigo;
	}

	private static List<Query> popularBancoDeDados(boolean popular)
			throws JDOMException, IOException {

		Loader l = new Loader(PATH_DOCS);
		// Loader l = new Loader("/home/fabriciorsf/workspace/BRI2/docs/");
		p = new PopulateDB();

		List<Artigo> artigos = l.loadArtigos();
		List<Query> queries = l.loadQueries();
		List<Item> itens = l.loadItens();

		if (popular) {
			p.popular(artigos, itens, queries);
		}

		queries = atualizaQueries();

		return queries;

	}

	private static List<Query> atualizaQueries() {
		return (List<Query>) p.getDao().listAll(Query.class);

	}
}
