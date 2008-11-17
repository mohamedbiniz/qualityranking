package br.ufrj.cos.bri.bean;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Table
@Entity
public class DataSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	public static final char STATUS_CREATION = 'C';
	@Transient
	public static final char STATUS_CRAWLING = 'L';
	@Transient
	public static final char STATUS_AUTOMATIC_EVALUATION = 'A';
	@Transient
	public static final char STATUS_RANKING = 'R';
	@Transient
	public static final char STATUS_MANUAL_EVALUATION = 'M';
	@Transient
	public static final char STATUS_FINALIZED = 'F';

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private char status;

	@Column(nullable = false)
	private boolean crawler;

	@Column(nullable = false, unique = true, length = 50)
	private String context;

	@Column(nullable = false, length = 8000)
	private String description;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Language language;

	@Column(name = "creation_datetime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(name = "finalization_datetime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date closingDate;

	@Column(name = "min_pages", nullable = false)
	private int minQuantityPages;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Collaborator collaborator;

	@OneToMany(mappedBy = "dataSet", cascade = CascadeType.ALL)
	private Collection<Document> documents;

	@OneToMany(mappedBy = "dataSet", cascade = CascadeType.ALL)
	private Collection<SeedDocument> seedDocuments;

	@OneToMany(mappedBy = "dataSet", cascade = CascadeType.ALL)
	private Collection<AssessmentScale> assessmentScales;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "DataSet_Collaborator", joinColumns = @JoinColumn(name = "dataSet_id"), inverseJoinColumns = @JoinColumn(name = "collaborator_id"))
	private Collection<Collaborator> collaborators;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Context_Quality_Dimension_Weight", joinColumns = @JoinColumn(name = "dataset_id"), inverseJoinColumns = @JoinColumn(name = "quality_dimension_id"))
	private Collection<QualityDimension> qualityDimensions;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Context_Quality_Dimension_Weight", joinColumns = @JoinColumn(name = "dataset_id"), inverseJoinColumns = @JoinColumn(name = "quality_dimension_weight_id"))
	private Collection<QualityDimensionWeight> qualityDimensionWeights;

	public DataSet() {
		setMinQuantityPages(0);
		setCrawler(false);
		setStatus(STATUS_CREATION);
		
		setDocuments(new ArrayList<Document>());
		setSeedDocuments(new ArrayList<SeedDocument>());
		setAssessmentScales(new ArrayList<AssessmentScale>());
		setCollaborators(new ArrayList<Collaborator>());
		setQualityDimensions(new ArrayList<QualityDimension>());
		setQualityDimensionWeights(new ArrayList<QualityDimensionWeight>());

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
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return the language
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(Language language) {
		this.language = language;
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
	 * @return the closingDate
	 */
	public Date getClosingDate() {
		return closingDate;
	}

	/**
	 * @param closingDate
	 *            the closingDate to set
	 */
	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	/**
	 * @return the minQuantityPages
	 */
	public int getMinQuantityPages() {
		return minQuantityPages;
	}

	/**
	 * @param minQuantityPages
	 *            the minQuantityPages to set
	 */
	public void setMinQuantityPages(int minQuantityPages) {
		this.minQuantityPages = minQuantityPages;
	}

	/**
	 * @return the assessmentScales
	 */
	public Collection<AssessmentScale> getAssessmentScales() {
		return assessmentScales;
	}

	/**
	 * @param assessmentScales
	 *            the assessmentScales to set
	 */
	public void setAssessmentScales(Collection<AssessmentScale> assessmentScales) {
		this.assessmentScales = assessmentScales;
	}

	/**
	 * 
	 * @param assessmentScale
	 * @return
	 */
	public boolean addAssessmentScale(AssessmentScale assessmentScale) {
		return getAssessmentScales().add(assessmentScale);
	}

	/**
	 * 
	 * @param assessmentScale
	 * @return
	 */
	public boolean removeAssessmentScale(AssessmentScale assessmentScale) {
		return getAssessmentScales().remove(assessmentScale);
	}

	/**
	 * @return the status
	 */
	public char getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(char status) {
		this.status = status;
	}

	/**
	 * @return the crawler
	 */
	public boolean isCrawler() {
		return crawler;
	}

	/**
	 * @param crawler
	 *            the crawler to set
	 */
	public void setCrawler(boolean crawler) {
		this.crawler = crawler;
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
	 * @return the collaborator
	 */
	public Collaborator getCollaborator() {
		return collaborator;
	}

	/**
	 * @param collaborator
	 *            the collaborator to set
	 */
	public void setCollaborator(Collaborator collaborator) {
		this.collaborator = collaborator;
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

	/**
	 * @return the seedDocuments
	 */
	public Collection<SeedDocument> getSeedDocuments() {
		return seedDocuments;
	}

	/**
	 * @param seedDocuments
	 *            the seedDocuments to set
	 */
	public void setSeedDocuments(Collection<SeedDocument> seedDocuments) {
		this.seedDocuments = seedDocuments;
	}

	/**
	 * 
	 * @param seedDocument
	 * @return
	 */
	public boolean addSeedDocument(SeedDocument seedDocument) {
		return getSeedDocuments().add(seedDocument);
	}

	/**
	 * 
	 * @param seedDocument
	 * @return
	 */
	public boolean removeSeedDocument(SeedDocument seedDocument) {
		return getSeedDocuments().remove(seedDocument);
	}

	/**
	 * @return the collaborators
	 */
	public Collection<Collaborator> getCollaborators() {
		return collaborators;
	}

	/**
	 * @param collaborators
	 *            the collaborators to set
	 */
	public void setCollaborators(Collection<Collaborator> collaborators) {
		this.collaborators = collaborators;
	}

	/**
	 * 
	 * @param collaborator
	 * @return
	 */
	public boolean addCollaborator(Collaborator collaborator) {
		return getCollaborators().add(collaborator);
	}

	/**
	 * 
	 * @param collaborator
	 * @return
	 */
	public boolean removeCollaborator(Collaborator collaborator) {
		return getCollaborators().remove(collaborator);
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

}
