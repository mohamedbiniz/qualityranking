/**
 * 
 */
package br.ufrj.cos.bri.bean;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Fabricio
 * 
 */
@Table(name = "SubSet_Query")
@Entity
public class SubSetQuery implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SubSetQueryPk id;

	public SubSetQuery() {
		setId(new SubSetQueryPk());
	}

	/**
	 * @return the id
	 */
	public SubSetQueryPk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(SubSetQueryPk id) {
		this.id = id;
	}

	/**
	 * @return the subSet
	 */
	public SubSet getSubSet() {
		return getId().getSubSet();
	}

	/**
	 * @param subSet
	 *            the subSet to set
	 */
	public void setSubSet(SubSet subSet) {
		getId().setSubSet(subSet);
	}

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return getId().getQuery();
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(Query query) {
		getId().setQuery(query);
	}

}