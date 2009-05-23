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
public class DocumentQualityDimensionPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private Document document;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quality_dimension_id")
	private QualityDimension qualityDimension;

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
	 * @return the qualityDimension
	 */
	public QualityDimension getQualityDimension() {
		return qualityDimension;
	}

	/**
	 * @param qualityDimension
	 *            the qualityDimension to set
	 */
	public void setQualityDimension(QualityDimension qualityDimension) {
		this.qualityDimension = qualityDimension;
	}
}
