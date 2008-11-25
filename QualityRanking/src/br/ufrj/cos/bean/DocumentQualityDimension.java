package br.ufrj.cos.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "Document_Quality_Dimension")
@Entity
public class DocumentQualityDimension implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DocumentQualityDimensionPk id;

	@Column(nullable = false, precision = 6, scale = 5)
	private BigDecimal score;

	public DocumentQualityDimension() {
		setId(new DocumentQualityDimensionPk());
	}

	/**
	 * @return the id
	 */
	public DocumentQualityDimensionPk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(DocumentQualityDimensionPk id) {
		this.id = id;
	}

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return getId().getDocument();
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(Document document) {
		getId().setDocument(document);
	}

	/**
	 * @return the qualityDimension
	 */
	public QualityDimension getQualityDimension() {
		return getId().getQualityDimension();
	}

	/**
	 * @param qualityDimension
	 *            the qualityDimension to set
	 */
	public void setQualityDimension(QualityDimension qualityDimension) {
		getId().setQualityDimension(qualityDimension);
	}

	/**
	 * @return the score
	 */
	public BigDecimal getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(BigDecimal score) {
		this.score = score;
	}
}
