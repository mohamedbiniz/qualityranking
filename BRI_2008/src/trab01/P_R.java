/**
 * 
 */
package trab01;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import beans.Artigo;

/**
 * @author Fabricio
 * 
 */
public class P_R {

	static final int QTD_PTS = 11;

	private int pt = 0;

	private HashMap<Integer, Double> precisao;

	private HashMap<Integer, Double> revocacao;

	public P_R(Set<Integer> revocacao, List<Double> precisao) {

	}

	public P_R() {
		setPrecisao(new HashMap<Integer, Double>());
		setRevocacao(new HashMap<Integer, Double>());
	}

	public P_R(Collection<Artigo> artigos, Set<Artigo> allArtigosRelevantes) {
		this();
		calcularPrecisaoAndRevocacao(artigos, allArtigosRelevantes);
	}

	/**
	 * @return the precisao
	 */
	public HashMap<Integer, Double> getPrecisao() {
		return precisao;
	}

	/**
	 * @param precisao
	 *            the precisao to set
	 */
	private void setPrecisao(HashMap<Integer, Double> precisao) {
		this.precisao = precisao;
	}

	private void addPrecisaoRevocacao(double precisao, double revocacao) {

		getPrecisao().put(pt, precisao);
		getRevocacao().put(pt, revocacao);
		pt++;

	}

	/**
	 * @return the revocacao
	 */
	public HashMap<Integer, Double> getRevocacao() {
		return revocacao;
	}

	/**
	 * @param revocacao
	 *            the revocacao to set
	 */
	private void setRevocacao(HashMap<Integer, Double> revocacao) {
		this.revocacao = revocacao;
	}

	public void calcularPrecisaoAndRevocacao(Collection<Artigo> artigos,
			Set<Artigo> allArtigosRelevantes) {
		int RA = 0;
		int R = 0;
		final int N = allArtigosRelevantes.size();
		double precisao = 0;
		double revocacao = 0;
		// addPrecisaoRevocacao(precisao, revocacao);

		for (Artigo artigo : artigos) {
			R++;
			if (allArtigosRelevantes.contains(artigo)) {
				RA++;
				precisao = ((double) RA * 100) / ((double) R);
				revocacao = ((double) RA * 100) / ((double) N);
				System.out.println(precisao);
				System.out.println(revocacao);
				addPrecisaoRevocacao(precisao, revocacao);
			}

		}

		// interpolar();

	}

	@Override
	public String toString() {
		String result = "";

		for (int key = 0; key < QTD_PTS; key++) {
			String elemento = String.format("Q=%03d\tP=%1.2f\tR=%1.2f\n", key,
					getPrecisao().get(key), getRevocacao().get(key));

			result = result + elemento;
		}

		return result;
	}

	public static TreeMap<Integer, Double> getAvaragesByRecallLevel(
			TreeMap<Integer, Double>[] precisionsByRecall, int numQueries) {
		TreeMap<Integer, Double> P_R_interpolada = new TreeMap<Integer, Double>();
		for (Integer recall : precisionsByRecall[0].keySet()) {
			Double sum = new Double(0);
			for (TreeMap<Integer, Double> precisionByRecall : precisionsByRecall) {
				sum = sum + precisionByRecall.get(recall);
			}
			P_R_interpolada.put(recall, sum / numQueries);
		}
		return P_R_interpolada;
	}

	public static P_R treeMapToP_R(TreeMap<Integer, Double> treeMap) {
		P_R p_r = new P_R();
		for (Integer key : treeMap.keySet()) {
			p_r.addPrecisaoRevocacao(treeMap.get(key), (double) key);
		}
		return p_r;
	}

}
