/**
 * 
 */
package br.ufrj.cos.bri.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author Fabricio
 * 
 */
@Table(name = "SubSet_Quality_Dimension")
@Entity
public class SubSetQualityDimension {

	@EmbeddedId
	private SubSetQualityDimensionPk id;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(name = "quality_dimension_id", nullable = false)
	private QualityDimension qualityDimension;

	@Column(name = "min_score", nullable = false, precision = 6, scale = 5)
	private BigDecimal minScore;

	/**
	 * @return the id
	 */
	protected SubSetQualityDimensionPk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	protected void setId(SubSetQualityDimensionPk id) {
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

	/**
	 * @return the minScore
	 */
	public BigDecimal getMinScore() {
		return minScore;
	}

	/**
	 * @param minScore
	 *            the minScore to set
	 */
	public void setMinScore(BigDecimal minScore) {
		this.minScore = minScore;
	}

}

@Embeddable
class SubSetQualityDimensionPk implements Serializable {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private SubSet subSet;

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
}
