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
public class DocumentDocumentPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "child_document_id")
	private Document childDocument;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "father_document_id")
	private Document fatherDocument;

	/**
	 * @return the childDocument
	 */
	public Document getChildDocument() {
		return childDocument;
	}

	/**
	 * @param childDocument
	 *            the childDocument to set
	 */
	public void setChildDocument(Document childDocument) {
		this.childDocument = childDocument;
	}

	/**
	 * @return the fatherDocument
	 */
	public Document getFatherDocument() {
		return fatherDocument;
	}

	/**
	 * @param fatherDocument
	 *            the fatherDocument to set
	 */
	public void setFatherDocument(Document fatherDocument) {
		this.fatherDocument = fatherDocument;
	}

}
