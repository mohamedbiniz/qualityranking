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
@Table(name = "Context_Quality_Dimension_Weight")
@Entity
public class ContextQualityDimensionWeight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ContextQualityDimensionWeightPk id;

	public ContextQualityDimensionWeight() {
		setId(new ContextQualityDimensionWeightPk());
	}

	/**
	 * @return the id
	 */
	public ContextQualityDimensionWeightPk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(ContextQualityDimensionWeightPk id) {
		this.id = id;
	}

	/**
	 * @return the dataSet
	 */
	public DataSet getDataSet() {
		return getId().getDataSet();
	}

	/**
	 * @param dataSet
	 *            the dataSet to set
	 */
	public void setDataSet(DataSet dataSet) {
		getId().setDataSet(dataSet);
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
	 * @return the qualityDimensionWeight
	 */
	public QualityDimensionWeight getQualityDimensionWeight() {
		return getId().getQualityDimensionWeight();
	}

	/**
	 * @param qualityDimensionWeight
	 *            the qualityDimensionWeight to set
	 */
	public void setQualityDimensionWeight(
			QualityDimensionWeight qualityDimensionWeight) {
		getId().setQualityDimensionWeight(qualityDimensionWeight);
	}
}
