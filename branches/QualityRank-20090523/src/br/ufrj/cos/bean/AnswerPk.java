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
public class AnswerPk implements Serializable, Comparable<AnswerPk> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private Document document;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private Query query;

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
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

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof AnswerPk) {
			AnswerPk answerPk = (AnswerPk) obj;
			if (getDocument().equals(answerPk.getDocument())
					&& getQuery().equals(answerPk.getQuery())) {
				return true;

			}
		}
		return false;
	}

	public int compareTo(AnswerPk answerPk) {
		if (answerPk != null) {
			if ((getQuery().compareTo(answerPk.getQuery()) < 0)
					|| (getQuery().compareTo(answerPk.getQuery()) == 0)
					&& getDocument().compareTo(answerPk.getDocument()) < 0) {
				return -1;
			} else if (equals(answerPk)) {
				return 0;
			}

		}
		return 1;
	}
}
