/**
 * 
 */
package trab02.indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import beans.Artigo;
import beans.words.Word_Artigos;

/**
 * @author Fabricio
 * 
 */
public class ReverseListWordDocs<T extends Word_Artigos> {

	private HashMap<String, HashMap<Artigo, Long>> reverseList;

	public ReverseListWordDocs() {
		setReverseList(new HashMap<String, HashMap<Artigo, Long>>());
	}

	public ReverseListWordDocs(List<T> words_artigos) throws Exception {
		this();
		for (T word_Artigos : words_artigos) {
			String word = word_Artigos.getWord();
			Artigo artigo = word_Artigos.getArtigo();
			long qtd = word_Artigos.getQtdOcorrencias();
			putWordArtigoQtd(word, artigo, qtd);
		}
	}

	public List<T> getListWords_Artigos(T classInstance)
			throws InstantiationException, IllegalAccessException {
		List<T> words_artigos = new ArrayList<T>();
		for (String word : getReverseList().keySet()) {
			for (Artigo artigo : getReverseList().get(word).keySet()) {
				Long qtd = getReverseList().get(word).get(artigo);

				T word_artigos = (T) classInstance.getClass().newInstance();
				word_artigos.setWord(word);
				word_artigos.setArtigo(artigo);
				word_artigos.setQtdOcorrencias(qtd);
				words_artigos.add(word_artigos);
			}
		}
		return words_artigos;
	}

	/**
	 * @param word
	 * @param artigo
	 * @param qtd
	 * @throws Exception
	 */
	private void putWordArtigoQtd(String word, Artigo artigo, long qtd)
			throws Exception {
		if (word.equals(""))
			return;
		Set<String> wordsSet = getReverseList().keySet();
		if (wordsSet.contains(word)) {
			Set<Artigo> artigosSet = getReverseList().get(word).keySet();
			if (!artigosSet.contains(artigo)) {
				getReverseList().get(word).put(artigo, qtd);
			} else {
				throw new Exception(
						"Duplicidade de artigo para uma mesma palavra");
			}
		} else {
			HashMap<Artigo, Long> artigo_qtd = new HashMap<Artigo, Long>();
			artigo_qtd.put(artigo, qtd);
			getReverseList().put(word, artigo_qtd);
		}
	}

	public void addWordArtigo(String word, Artigo artigo) {
		if (word.equals(""))
			return;
		long newQtd = new Long(1);
		Set<String> wordsSet = getReverseList().keySet();
		if (wordsSet.contains(word)) {
			Set<Artigo> artigosSet = getReverseList().get(word).keySet();
			if (artigosSet.contains(artigo)) {
				newQtd += getReverseList().get(word).get(artigo);
				getReverseList().get(word).put(artigo, newQtd);
			} else {
				getReverseList().get(word).put(artigo, newQtd);
			}
		} else {
			HashMap<Artigo, Long> artigo_qtd = new HashMap<Artigo, Long>();
			artigo_qtd.put(artigo, newQtd);
			getReverseList().put(word, artigo_qtd);
		}
	}

	/**
	 * @return the invertList
	 */
	public HashMap<String, HashMap<Artigo, Long>> getReverseList() {
		return reverseList;
	}

	/**
	 * @param reverseList
	 *            the reverseList to set
	 */
	private void setReverseList(
			HashMap<String, HashMap<Artigo, Long>> reverseList) {
		this.reverseList = reverseList;
	}

	public long getQtd(String word, Artigo artigo) {
		Long qtd = getReverseList().get(word).get(artigo);
		if (qtd == null) {
			qtd = new Long(0);
		}
		return qtd;
	}

}
