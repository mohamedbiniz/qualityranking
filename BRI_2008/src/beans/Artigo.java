package beans;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Fabricio
 * @author Oliverio
 * 
 */
@Table(name = "Artigos")
@Entity
public class Artigo implements Serializable, Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true)
	private int numero;

	@Column(length = 7000)
	private String title;

	@Column(length = 7000)
	private String abstracT;

	@Column(length = 7000)
	private String corpo;

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
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numero
	 *            the numero to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the abstracT
	 */
	public String getAbstracT() {
		return abstracT;
	}

	/**
	 * @param abstracT
	 *            the abstracT to set
	 */
	public void setAbstracT(String abstracT) {
		this.abstracT = abstracT;
	}

	/**
	 * @return the corpo
	 */
	public String getCorpo() {
		return corpo;
	}

	/**
	 * @param corpo
	 *            the corpo to set
	 */
	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Artigo) {
				Artigo artigo = (Artigo) obj;
				if ((getId() == 0) || (artigo.getId() == 0)) {
					if (getNumero() == artigo.getNumero()) {
						return true;
					}
				} else {
					if (getId() == artigo.getId()) {
						return true;
					}
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
				if (o instanceof Artigo) {
					Artigo artigo = (Artigo) o;
					if ((getId() == 0) || (artigo.getId() == 0)) {
						if (getNumero() < artigo.getNumero()) {
							return -1;
						} else {
							return 1;
						}
					} else if (getId() < artigo.getId()) {
						return -1;
					} else {
						return 1;
					}

				}
			}
			return -1;
		}
	}

	public String getAll() {
		return this.toString();
	}

	@Override
	public String toString() {
		// return String.format("%d %s %s %s", getId(), getTitle(),
		// getAbstracT(),
		// getCorpo()).replace('\n', ' ');
		return String.format("%s %s %s", getTitle(), getAbstracT(), getCorpo());
	}

}
