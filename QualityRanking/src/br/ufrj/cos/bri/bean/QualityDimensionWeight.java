/**
 * 
 */
package br.ufrj.cos.bri.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * @author Fabricio
 * 
 */
@Table(name = "Quality_Dimension_Weight")
@Entity
public class QualityDimensionWeight implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private int weight;

	@Column(nullable = false, length = 100)
	private String description;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Context_Quality_Dimension_Weight", joinColumns = @JoinColumn(name = "quality_dimension_weight_id"), inverseJoinColumns = @JoinColumn(name = "dataset_id"))
	private Collection<DataSet> dataSets;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Context_Quality_Dimension_Weight", joinColumns = @JoinColumn(name = "quality_dimension_weight_id"), inverseJoinColumns = @JoinColumn(name = "quality_dimension_id"))
	private Collection<QualityDimension> qualityDimensions;

	public QualityDimensionWeight() {
		setDataSets(new ArrayList<DataSet>());
		setQualityDimensions(new ArrayList<QualityDimension>());
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the dataSets
	 */
	public Collection<DataSet> getDataSets() {
		return dataSets;
	}

	/**
	 * @param dataSets
	 *            the dataSets to set
	 */
	public void setDataSets(Collection<DataSet> dataSets) {
		this.dataSets = dataSets;
	}

	/**
	 * 
	 * @param dataSet
	 */
	public boolean addDataSet(DataSet dataSet) {
		return getDataSets().add(dataSet);
	}

	/**
	 * 
	 * @param dataSet
	 * @return
	 */
	public boolean removeDataSet(DataSet dataSet) {
		return getDataSets().remove(dataSet);
	}

	/**
	 * @return the qualityDimensions
	 */
	public Collection<QualityDimension> getQualityDimensions() {
		return qualityDimensions;
	}

	/**
	 * @param qualityDimensions
	 *            the qualityDimensions to set
	 */
	public void setQualityDimensions(
			Collection<QualityDimension> qualityDimensions) {
		this.qualityDimensions = qualityDimensions;
	}

	/**
	 * 
	 * @param qualityDimension
	 * @return
	 */
	public boolean addQualityDimension(QualityDimension qualityDimension) {
		return getQualityDimensions().add(qualityDimension);
	}

	/**
	 * 
	 * @param qualityDimension
	 * @return
	 */
	public boolean removeQualityDimension(QualityDimension qualityDimension) {
		return getQualityDimensions().remove(qualityDimension);
	}

}
