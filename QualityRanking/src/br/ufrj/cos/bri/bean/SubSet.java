package br.ufrj.cos.bri.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Table
@Entity
public class SubSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "creation_datetime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "min_score", nullable = false, precision = 6, scale = 5)
	private BigDecimal minimalRelevance;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private DataSet dataSet;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(name = "min_assesment_scale_id", nullable = false)
	private AssessmentScale minAssessmentScale;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "SubSet_Query",
			joinColumns = @JoinColumn(name = "subset_id"),
			inverseJoinColumns = @JoinColumn(name = "query_id"))
	private Collection<Query> queries;

	public SubSet() {
		setMinimalRelevance(new BigDecimal(0));
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
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the minimalRelevance
	 */
	public BigDecimal getMinimalRelevance() {
		return minimalRelevance;
	}

	/**
	 * @param minimalRelevance
	 *            the minimalRelevance to set
	 */
	public void setMinimalRelevance(BigDecimal minimalRelevance) {
		this.minimalRelevance = minimalRelevance;
	}

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
	 * @return the minAssessmentScale
	 */
	public AssessmentScale getMinAssessmentScale() {
		return minAssessmentScale;
	}

	/**
	 * @param minAssessmentScale
	 *            the minAssessmentScale to set
	 */
	public void setMinAssessmentScale(AssessmentScale minAssessmentScale) {
		this.minAssessmentScale = minAssessmentScale;
	}

	/**
	 * @return the queries
	 */
	public Collection<Query> getQueries() {
		return queries;
	}

	/**
	 * @param queries
	 *            the queries to set
	 */
	public void setQueries(Collection<Query> queries) {
		this.queries = queries;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public boolean addQuery(Query query) {
		return getQueries().add(query);
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public boolean removeQuery(Query query) {
		return getQueries().remove(query);
	}

}
