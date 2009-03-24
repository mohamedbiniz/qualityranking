/**
 * 
 */
package beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Fabricio
 * 
 */
@Table(name = "Queries")
@Entity
public class Query implements Serializable, Comparable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true)
	private int numero;

	@Column(nullable = false)
	private String text;

	@OneToMany(mappedBy = "query")
	private Set<Item> itens;

	public Query() {
		setItens(new HashSet<Item>());
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
	 * @return the numero
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * @param numeroQuery
	 *            the numeroQuery to set
	 */
	public void setNumero(int numero) {
		this.numero = numero;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the itens
	 */
	public Set<Item> getItens() {
		return itens;
	}

	/**
	 * @param itens
	 *            the itens to set
	 */
	public void setItens(Set<Item> itens) {
		this.itens = itens;
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean addItem(Item item) {
		return getItens().add(item);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof Query) {
				Query query = (Query) obj;
				if ((getId() == 0) || (query.getId() == 0)) {
					if (getNumero() == query.getNumero()) {
						return true;
					}
				} else {
					if (getId() == query.getId()) {
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
				if (o instanceof Query) {
					Query query = (Query) o;
					if ((getId() == 0) || (query.getId() == 0)) {
						if (getNumero() < query.getNumero()) {
							return -1;
						} else {
							return 1;
						}
					} else {
						if (getId() < query.getId()) {
							return -1;
						} else {
							return 1;
						}
					}
				}
			}
			return -1;
		}
	}

	@Override
	public String toString() {
		return String.format("%s", getText());
	}

}
