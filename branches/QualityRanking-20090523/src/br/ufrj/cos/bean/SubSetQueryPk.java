/**
 * 
 */
package br.ufrj.cos.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Fabricio
 * 
 */

@Embeddable
public class SubSetQueryPk implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private SubSet subSet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private Query query;

	/**
	 * @return the subSet
	 */
	public SubSet getSubSet() {
		return subSet;
	}

	/**
	 * @param subSet
	 *            the subSet to set
	 */
	public void setSubSet(SubSet subSet) {
		this.subSet = subSet;
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
	public void setQuery(Query query) {
		this.query = query;
	}

}
