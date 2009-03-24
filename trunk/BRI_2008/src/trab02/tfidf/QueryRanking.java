/**
 * 
 */
package trab02.tfidf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import beans.Artigo;
import beans.Query;

/**
 * @author Fabricio
 * 
 */
public class QueryRanking implements Comparable<Object> {

	private Query query;

	private Set<ArtigoPeso> artigosPesos;

	public QueryRanking() {
		setArtigosPesos(new TreeSet<ArtigoPeso>());
	}

	public QueryRanking(Query query,
			HashMap<Query, Set<Artigo>> resultsByQuery,
			HashMap<String, HashMap<Artigo, Long>> term_artigos,
			Collection<Artigo> allArtigos) {
		this();
		setQuery(query);

		Set<Artigo> artigos = resultsByQuery.get(query);

		for (Artigo artigo : artigos) {
			try {
				ArtigoPeso artigoPeso = new ArtigoPeso(artigo, term_artigos,
						allArtigos, getQueryVector());
				addArtigoPeso(artigoPeso);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

	}

	private TreeMap<String, Long> getQueryVector() {
		String[] words = query.getText().split(" ");
		TreeMap<String, Long> queryVector = new TreeMap<String, Long>();
		for (String word : words) {
			if (queryVector.keySet().contains(word)) {
				queryVector.put(word, queryVector.get(word) + 1);
			} else {
				queryVector.put(word, new Long(1));
			}
		}
		return queryVector;
	}

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	private void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * @return the artigosPesos
	 */
	public Set<ArtigoPeso> getArtigosPesos() {
		return artigosPesos;
	}

	/**
	 * @param artigosPesos
	 *            the artigosPesos to set
	 */
	private void setArtigosPesos(Set<ArtigoPeso> artigosPesos) {
		this.artigosPesos = artigosPesos;
	}

	private void addArtigoPeso(ArtigoPeso artigoPeso) {
		getArtigosPesos().add(artigoPeso);
	}

	@Override
	public String toString() {
		String results = "";
		for (ArtigoPeso artigoPeso : getArtigosPesos()) {
			results = results + "\t" + getArtigosPesos() + "\n";
		}
		return String.format("%d\n%s", getQuery().getId(), results);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof QueryRanking) {
				QueryRanking queryRanking = (QueryRanking) obj;
				if (getQuery().equals(queryRanking.getQuery())
						&& getArtigosPesos().equals(
								queryRanking.getArtigosPesos())) {
					return true;
				}

			}
		}
		return false;
	}

	@Override
	public int compareTo(Object o) {
		if (equals(o)) {
			return 0;
		} else {
			if (o != null) {
				if (o instanceof QueryRanking) {
					QueryRanking queryRanking = (QueryRanking) o;
					return getQuery().compareTo(queryRanking.getQuery());
				}
			}
			return -1;
		}
	}

}
