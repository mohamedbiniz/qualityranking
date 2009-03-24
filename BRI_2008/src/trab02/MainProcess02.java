package trab02;

import helper.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.lucene.queryParser.ParseException;

import trab01.MainProcess01;
import trab01.P_R;
import trab01.ploter.Ploter;
import trab02.indexing.ReverseListWordDocsDB;
import trab02.indexing.reverseList.ReverseListAbstracTWordDocsDB;
import trab02.indexing.reverseList.ReverseListAllWordDocsDB;
import trab02.indexing.reverseList.ReverseListTitleWordDocsDB;
import trab02.tfidf.ArtigoPeso;
import trab02.tfidf.QueryRanking;
import beans.Artigo;
import beans.Query;
import beans.words.Word_Artigos;

public class MainProcess02 extends MainProcess01 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		etapa01(false, false);

		etapa02(false, true);

		p.getDao().getSession().close();
	}

	private static void etapa02(boolean preprocessar, boolean popular) {

		if (preprocessar) {
			preprocessamento();
		}

		HashMap<String, HashMap<Artigo, Long>> listTitles = null;
		HashMap<String, HashMap<Artigo, Long>> listAbstracT = null;
		// HashMap<String, HashMap<Artigo, Long>> listAll = null;
		try {
			listTitles = indexacaoListaInvertidaTitle(popular);
			listAbstracT = indexacaoListaInvertidaAbstracT(popular);
			// listAll = indexacaoListaInvertidaAll(popular);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			Collection<Artigo> allArtigos = new ArrayList<Artigo>();
			buscarEAvalia(listTitles, "Title");
			buscarEAvalia(listAbstracT, "Abstract");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // catch (IOException e) {

	}

	private static void buscarEAvalia(
			HashMap<String, HashMap<Artigo, Long>> list, String string)
			throws ParseException {
		buscarEGerarVetores(list, string, false);
	}

	private static void preprocessamento() {

		List<Artigo> artigos = listObjToListArtigo(p.listAll("Artigos"));
		for (Artigo artigo : artigos) {
			artigo.setTitle(padronizar(artigo.getTitle()));
			artigo.setAbstracT(padronizar(artigo.getAbstracT()));
			artigo.setCorpo(padronizar(artigo.getCorpo()));
			p.update(artigo);
		}

		List<Query> queries = listObjToListQuery(p.listAll("Queries"));
		for (Query query : queries) {
			query.setText(padronizar(query.getText()));
			p.update(query);
		}

	}

	private static String padronizar(String str) {
		return PorterStemmer.execute(str);
	}

	private static HashMap<String, HashMap<Artigo, Long>> indexacaoListaInvertidaTitle(
			boolean popular) throws Exception {
		return indexacaoListaInvertida(new ReverseListTitleWordDocsDB(),
				"title", popular);
	}

	private static HashMap<String, HashMap<Artigo, Long>> indexacaoListaInvertidaAbstracT(
			boolean popular) throws Exception {
		return indexacaoListaInvertida(new ReverseListAbstracTWordDocsDB(),
				"abstracT", popular);
	}

	private static HashMap<String, HashMap<Artigo, Long>> indexacaoListaInvertidaAll(
			boolean popular) throws Exception {
		return indexacaoListaInvertida(new ReverseListAllWordDocsDB(), "all",
				popular);
	}

	private static HashMap<String, HashMap<Artigo, Long>> indexacaoListaInvertida(
			ReverseListWordDocsDB<? extends Word_Artigos> instance,
			String artigoNameField, boolean popular) throws Exception {
		ReverseListWordDocsDB<? extends Word_Artigos> reverseList = instance;
		if (!popular) {
			return reverseList.getReverseListOfDB(p.getDao());
		}
		List<Artigo> artigos = listObjToListArtigo(p.listAll("Artigos"));
		for (Artigo artigo : artigos) {
			String[] words = getWordsOfArtigo(artigo, artigoNameField);
			for (String word : words) {
				reverseList.addWordArtigo(word, artigo);
			}
		}

		reverseList.popular(p.getDao());

		return reverseList.getReverseList();
	}

	private static String[] getWordsOfArtigo(Artigo artigo,
			String artigoNameField) {
		Field field = ReflectionHelper.findField(Artigo.class, artigoNameField);
		Object fieldvalue = null;
		if (field != null) {
			fieldvalue = ReflectionHelper.getFieldValue(artigo, field);
		} else {
			fieldvalue = ReflectionHelper.invokeGetMethod(artigo,
					artigoNameField);
		}

		String[] words = ((String) fieldvalue).split(" ");
		return words;
	}

	private static Collection<QueryRanking> buscarEGerarVetores(
			HashMap<String, HashMap<Artigo, Long>> list, String campo,
			boolean calcylarTfIdf) throws ParseException {
		if (list == null) {
			return null;
		}

		HashMap<Query, Collection<Artigo>> results = buscar(list);
		avaliar(results, campo);

		Collection<QueryRanking> vectors = new ArrayList<QueryRanking>();
		// if (calcylarTfIdf) {
		// for (Query query : results.keySet()) {
		// vectors.add(new QueryRanking(query, results, list, allArtigos));
		// }
		// }
		return vectors;
	}

	private static void avaliar(HashMap<Query, Collection<Artigo>> results,
			String campo) throws ParseException {
		Collection<Query> queries = results.keySet();
		P_R p_r = null;

		TreeMap<Integer, Double> resultsConsultas = realizarConsultas(queries,
				null, 3, results);
		p_r = P_R.treeMapToP_R(resultsConsultas);

		imprimirPR(p_r);

		Ploter.print(resultsConsultas, campo, "Search Engine");
	}

	/**
	 * @param list
	 */
	private static HashMap<Query, Collection<Artigo>> buscar(
			HashMap<String, HashMap<Artigo, Long>> list) {
		List<Query> queries = listObjToListQuery(p.listAll("Queries"));
		List<Artigo> allArtigos = listObjToListArtigo(p.listAll("Artigos"));

		// Ir� armazenar o resultados das consultas por cada Query
		HashMap<Query, Collection<Artigo>> queriesResults = new HashMap<Query, Collection<Artigo>>();

		for (Query query : queries) {
			Set<Artigo> artigosResultsSet = new TreeSet<Artigo>();
			String[] queryWords = query.toString().split(" ");
			for (String queryW : queryWords) {
				HashMap<Artigo, Long> artigos_Qts_Results = list.get(queryW);
				if ((artigos_Qts_Results != null)
						&& (!artigos_Qts_Results.isEmpty())) {
					artigosResultsSet.addAll(artigos_Qts_Results.keySet());
				}

			}
			Set<ArtigoPeso> artigosPesosResults = buildPesos(query, list,
					artigosResultsSet, allArtigos);
			Collection<Artigo> artigosResults = getArtigosOrdenados(artigosPesosResults);
			queriesResults.put(query, artigosResults);
		}

		return queriesResults;
	}

	private static Set<ArtigoPeso> buildPesos(Query query,
			HashMap<String, HashMap<Artigo, Long>> term_artigos,
			Set<Artigo> artigosResultsSet, List<Artigo> allArtigos) {
		HashMap<Query, Set<Artigo>> resultsByQuery = new HashMap<Query, Set<Artigo>>();
		resultsByQuery.put(query, artigosResultsSet);
		QueryRanking queryRanking = new QueryRanking(query, resultsByQuery,
				term_artigos, allArtigos);
		return queryRanking.getArtigosPesos();

	}

	private static Collection<Artigo> getArtigosOrdenados(
			Set<ArtigoPeso> artigosPesosResults) {
		Collection<Artigo> artigos = new ArrayList<Artigo>();
		for (ArtigoPeso artigoPeso : artigosPesosResults) {
			artigos.add(artigoPeso.getArtigo());
		}
		return artigos;
	}

}
