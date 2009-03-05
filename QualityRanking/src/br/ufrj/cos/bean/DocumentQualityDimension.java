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

	@Column
	private double score;

	@Column
	private double fuzzy;

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
	public double getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(double score) {
		this.score = score;
	}

	/**
	 * @return the fuzzy
	 */
	public double getFuzzy() {
		return fuzzy;
	}

	/**
	 * @param fuzzy
	 *            the fuzzy to set
	 */
	public void setFuzzy(double fuzzy) {
		this.fuzzy = fuzzy;
	}
}
