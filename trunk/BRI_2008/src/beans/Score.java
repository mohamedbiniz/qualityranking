/**
 * 
 */
package beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Fabricio
 * 
 */
@Table(name = "Scores")
@Entity
public class Score implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(length = 4, unique = true)
	private String notas;

	public Score() {

	}

	public Score(String notas) {
		setNotas(notas);
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the notaREW
	 */
	public int getNotaREW() {
		return getNotas().charAt(0);
	}

	/**
	 * @return the notaREWColleagues
	 */
	public int getNotaREWColleagues() {
		return getNotas().charAt(1);
	}

	/**
	 * @return the notaREWPostDoctorates
	 */
	public int getNotaREWPostDoctorates() {
		return getNotas().charAt(2);
	}

	/**
	 * @return the notaJBW
	 */
	public int getNotaJBW() {
		return getNotas().charAt(3);
	}

	/**
	 * @return the notas
	 */
	public String getNotas() {
		return notas;
	}

	/**
	 * @param notas
	 *            the notas to set
	 */
	public void setNotas(String notas) {
		if (notas == null || notas.length() != 4) {
			notas = "0000";
		}
		this.notas = notas;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Score) {
				Score score = (Score) obj;
				if (getNotas().equals(score.getNotas())) {
					return true;
				}
			}
		}
		return false;
	}
}
