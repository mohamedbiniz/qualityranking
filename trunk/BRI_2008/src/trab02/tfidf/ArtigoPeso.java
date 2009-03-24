package trab02.tfidf;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import beans.Artigo;

public class ArtigoPeso implements Comparable<Object> {

	private static final double Q = 0;

	private Artigo artigo;

	private double peso;

	private Set<TfIdf> tfIdfs;

	public ArtigoPeso() {
		setTfIdfs(new TreeSet<TfIdf>());
	}

	public ArtigoPeso(Artigo artigo,
			HashMap<String, HashMap<Artigo, Long>> term_artigos,
			Collection<Artigo> allArtigos, TreeMap<String, Long> queryVector)
			throws Exception {
		this();
		setArtigo(artigo);
		for (String term : term_artigos.keySet()) {
			addTfIdf(new TfIdf(term, artigo, term_artigos, allArtigos));
		}
		setPeso(calcularPeso(term_artigos, queryVector));
	}

	/**
	 * @return the artigo
	 */
	public Artigo getArtigo() {
		return artigo;
	}

	/**
	 * @param artigo
	 *            the artigo to set
	 */
	public void setArtigo(Artigo artigo) {
		this.artigo = artigo;
	}

	/**
	 * @return the peso
	 */
	public double getPeso() {
		return peso;
	}

	/**
	 * @param peso
	 *            the peso to set
	 */
	private void setPeso(double peso) {
		this.peso = peso;
	}

	/**
	 * @return the tfIdfs
	 */
	public Set<TfIdf> getTfIdfs() {
		return tfIdfs;
	}

	/**
	 * 
	 * @param term
	 * @return
	 */
	public TfIdf getTfIdfByTerm(String term) {
		Set<TfIdf> tfIdfs = getTfIdfs();
		for (TfIdf tfIdf : tfIdfs) {
			if (term.equalsIgnoreCase(tfIdf.getTerm()))
				return tfIdf;
		}
		return null;
	}

	/**
	 * @param tfIdfs
	 *            the tfIdfs to set
	 */
	public void setTfIdfs(Set<TfIdf> tfIdfs) {
		this.tfIdfs = tfIdfs;
	}

	private void addTfIdf(TfIdf tfIdf) {
		getTfIdfs().add(tfIdf);
	}

	protected double calcularPeso(
			HashMap<String, HashMap<Artigo, Long>> term_artigos,
			TreeMap<String, Long> queryVector) {
		double cosseno;
		double aTq = 0;
		double Q = 0;
		double A = 0;

		for (String term : queryVector.keySet()) {
			double aij = 0;
			double qi = 0;

			TfIdf tfIdf = getTfIdfByTerm(term);
			if (tfIdf != null) {
				aij = tfIdf.getTfIdf();
				qi = 1;
			}

			aTq += aij * qi;
			A += qi * qi;// TODO
			Q += aij * aij;// TODO
		}

		cosseno = aTq / (Math.sqrt(A * Q));
		return Math.acos(cosseno);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof ArtigoPeso) {
				ArtigoPeso artigoPeso = (ArtigoPeso) obj;
				if (getPeso() == artigoPeso.getPeso()
						&& getArtigo().equals(artigoPeso.getArtigo())) {
					return true;
				}

			}
		}
		return false;
	}

	public int compareTo(Object o) {
		if (equals(o)) {
			return 0;
		} else {
			if (o != null) {
				if (o instanceof ArtigoPeso) {
					ArtigoPeso artigoPeso = (ArtigoPeso) o;
					int r = (new Double(getPeso())).compareTo(new Double(
							artigoPeso.getPeso()));
					if (r == 0) {
						r = getArtigo().compareTo(artigoPeso.getArtigo());
					}
					return r;
				}
			}
			return -1;
		}
	}

	@Override
	public String toString() {
		return String.format("%03d\t%f", getArtigo().getId(), getPeso());
	}

}
