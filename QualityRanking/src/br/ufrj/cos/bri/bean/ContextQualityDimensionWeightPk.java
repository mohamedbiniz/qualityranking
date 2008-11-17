/**
 * 
 */
package br.ufrj.cos.bri.bean;

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
public class ContextQualityDimensionWeightPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private DataSet dataSet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quality_dimension_id")
	private QualityDimension qualityDimension;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quality_dimension_weight_id")
	private QualityDimensionWeight qualityDimensionWeight;

	/**
	 * @return the dataSet
	 */
	public DataSet getDataSet() {
		return dataSet;
	}

	/**
	 * @param dataSet
	 *            the dataSet to set
	 */
	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
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
	 * @return the qualityDimensionWeight
	 */
	public QualityDimensionWeight getQualityDimensionWeight() {
		return qualityDimensionWeight;
	}

	/**
	 * @param qualityDimensionWeight
	 *            the qualityDimensionWeight to set
	 */
	public void setQualityDimensionWeight(
			QualityDimensionWeight qualityDimensionWeight) {
		this.qualityDimensionWeight = qualityDimensionWeight;
	}
}
