/**
 * 
 */
package br.ufrj.cos.bean;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Fabricio
 * 
 */
@Table(name = "Document_Document")
@Entity
public class DocumentDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DocumentDocumentPk id;

	public DocumentDocument() {
		setId(new DocumentDocumentPk());
	}

	/**
	 * @return the id
	 */
	public DocumentDocumentPk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(DocumentDocumentPk id) {
		this.id = id;
	}

	/**
	 * @return the childDocument
	 */
	public Document getChildDocument() {
		return getId().getChildDocument();
	}

	/**
	 * @param childDocument
	 *            the childDocument to set
	 */
	public void setChildDocument(Document childDocument) {
		getId().setChildDocument(childDocument);
	}

	/**
	 * @return the fatherDocument
	 */
	public Document getFatherDocument() {
		return getId().getFatherDocument();
	}

	/**
	 * @param fatherDocument
	 *            the fatherDocument to set
	 */
	public void setFatherDocument(Document fatherDocument) {
		getId().setFatherDocument(fatherDocument);
	}

}
