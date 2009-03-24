/**
 * 
 */
package trab02.tfidf;

import java.util.Collection;
import java.util.HashMap;

import beans.Artigo;

/**
 * @author Fabricio
 * 
 */
public class TfIdf implements Comparable<Object> {

	private String term;

	private long tf;

	private double idf;

	public TfIdf(String term, Artigo artigo,
			HashMap<String, HashMap<Artigo, Long>> term_artigos,
			Collection<Artigo> allArtigos) throws Exception {
		HashMap<Artigo, Long> artigos = term_artigos.get(term);
		if (artigos == null)
			throw new Exception("N�o exitem artigos com este termo.");
		setTerm(term);
		Long tf = artigos.get(artigo);
		if (tf == null)
			tf = new Long(0);
		// throw new Exception(
		// "N�o exite frequencia do termo com este artigo.");
		setTf(tf);
		setIdf(Math.log10((double) allArtigos.size() / (double) artigos.size()));
	}

	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * @param term
	 *            the term to set
	 */
	private void setTerm(String term) {
		this.term = term;
	}

	/**
	 * @return the tf
	 */
	public long getTf() {
		return tf;
	}

	/**
	 * @param tf
	 *            the tf to set
	 */
	private void setTf(long tf) {
		this.tf = tf;
	}

	/**
	 * @return the idf
	 */
	public double getIdf() {
		return idf;
	}

	/**
	 * @param idf
	 *            the idf to set
	 */
	private void setIdf(double idf) {
		this.idf = idf;
	}

	/**
	 * 
	 * @return the tfidf
	 */
	public double getTfIdf() {
		return getTf() * getIdf();
	}

	@Override
	public String toString() {
		return String.format("%s\t%f", getTerm(), getTfIdf());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof TfIdf) {
				TfIdf tfIdf = (TfIdf) obj;
				if (getTerm().equalsIgnoreCase(tfIdf.getTerm())) {
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
				if (o instanceof TfIdf) {
					TfIdf tfIdf = (TfIdf) o;
					int r = getTerm().compareToIgnoreCase(tfIdf.getTerm());
					return r;
				}
			}
			return -1;
		}
	}

}
