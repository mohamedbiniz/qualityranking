package br.ufrj.cos.bean;

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

@Table(name = "Quality_Dimension")
@Entity
public class QualityDimension implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String COM = "COM";
	public static final String REP = "REP";
	public static final String TIM = "TIM";
	public static final String SEC = "SEC";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true, length = 50)
	private String name;

	@Column(nullable = false, unique = true, length = 3)
	private char[] code;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Context_Quality_Dimension_Weight", joinColumns = @JoinColumn(name = "quality_dimension_id"), inverseJoinColumns = @JoinColumn(name = "dataset_id"))
	private Collection<DataSet> dataSets;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Context_Quality_Dimension_Weight", joinColumns = @JoinColumn(name = "quality_dimension_id"), inverseJoinColumns = @JoinColumn(name = "quality_dimension_weight_id"))
	private Collection<QualityDimensionWeight> qualityDimensionWeights;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Document_Quality_Dimension", joinColumns = @JoinColumn(name = "quality_dimension_id"), inverseJoinColumns = @JoinColumn(name = "document_id"))
	private Collection<Document> documents;

	public QualityDimension() {
		setDataSets(new ArrayList<DataSet>());
		setQualityDimensionWeights(new ArrayList<QualityDimensionWeight>());
		setDocuments(new ArrayList<Document>());
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public char[] getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(char[] code) {
		if (code.length == 3)
			this.code = code;
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
	 * @return the qualityDimensionWeights
	 */
	public Collection<QualityDimensionWeight> getQualityDimensionWeights() {
		return qualityDimensionWeights;
	}

	/**
	 * @param qualityDimensionWeights
	 *            the qualityDimensionWeights to set
	 */
	public void setQualityDimensionWeights(
			Collection<QualityDimensionWeight> qualityDimensionWeights) {
		this.qualityDimensionWeights = qualityDimensionWeights;
	}

	/**
	 * 
	 * @param qualityDimensionWeight
	 * @return
	 */
	public boolean addQualityDimensionWeight(
			QualityDimensionWeight qualityDimensionWeight) {
		return getQualityDimensionWeights().add(qualityDimensionWeight);
	}

	/**
	 * 
	 * @param qualityDimensionWeight
	 * @return
	 */
	public boolean removeQualityDimensionWeight(
			QualityDimensionWeight qualityDimensionWeight) {
		return getQualityDimensionWeights().remove(qualityDimensionWeight);
	}

	/**
	 * @return the documents
	 */
	public Collection<Document> getDocuments() {
		return documents;
	}

	/**
	 * @param documents
	 *            the documents to set
	 */
	public void setDocuments(Collection<Document> documents) {
		this.documents = documents;
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	public boolean addDocument(Document document) {
		return getDocuments().add(document);
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	public boolean removeDocument(Document document) {
		return getDocuments().remove(document);
	}

	@Override
	public String toString() {
		return String.format("%s", getName());
	}

}
