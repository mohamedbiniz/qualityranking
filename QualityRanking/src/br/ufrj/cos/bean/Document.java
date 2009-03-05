package br.ufrj.cos.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

@Table
//(name = "PAGE"/*, uniqueConstraints = { @UniqueConstraint(columnNames = {
//"URL", "IdDataSet" }) }*/)
@Entity
public class Document implements Serializable, Comparable<Document>, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private long id;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private boolean active;

	@Column
	private double score;
	
	@Column
	private double fuzzy;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private DataSet dataSet;

	// Quando a atual instância é o filho
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Document_Document", joinColumns = @JoinColumn(name = "child_document_id"), inverseJoinColumns = @JoinColumn(name = "father_document_id"))
	private Set<Document> fatherDocuments;

	// Quando a atual instância é o pai
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Document_Document", joinColumns = @JoinColumn(name = "father_document_id"), inverseJoinColumns = @JoinColumn(name = "child_document_id"))
	private Set<Document> childDocuments;

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
	private Collection<DocumentData> documentDatas;

	@OneToMany(mappedBy = "document", cascade = CascadeType.ALL)
	private Collection<Metadata> metadatas;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Document_Quality_Dimension", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "quality_dimension_id"))
	private Collection<QualityDimension> qualityDimensions;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Answer", joinColumns = @JoinColumn(name = "document_id"), inverseJoinColumns = @JoinColumn(name = "query_id"))
	private Collection<Query> queries;

	public Document() {
		setActive(true);
		setFatherDocuments(new TreeSet<Document>());
		setChildDocuments(new TreeSet<Document>());
		setDocumentDatas(new ArrayList<DocumentData>());
		setMetadatas(new ArrayList<Metadata>());
		setQualityDimensions(new ArrayList<QualityDimension>());
		setQueries(new ArrayList<Query>());
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
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the fatherDocuments
	 */
	public Set<Document> getFatherDocuments() {
		return fatherDocuments;
	}

	/**
	 * @param fatherDocuments
	 *            the fatherDocuments to set
	 */
	public void setFatherDocuments(Set<Document> fatherDocuments) {
		fatherDocuments.remove(this);
		this.fatherDocuments = fatherDocuments;

	}

	/**
	 * 
	 * @param fatherDocument
	 * @return
	 */
	public boolean addFatherDocument(Document fatherDocument) {
		if (!this.equals(fatherDocument))
			return getFatherDocuments().add(fatherDocument);
		return false;

	}

	/**
	 * 
	 * @param fatherDocuments
	 * @return
	 */
	public boolean addAllFatherDocuments(Collection<Document> fatherDocuments) {
		fatherDocuments.remove(this);
		return getFatherDocuments().addAll(fatherDocuments);
	}

	/**
	 * 
	 * @param fatherDocument
	 * @return
	 */
	public boolean removeFatherDocument(Document fatherDocument) {
		return getFatherDocuments().remove(fatherDocument);
	}

	/**
	 * @return the childDocuments
	 */
	public Set<Document> getChildDocuments() {
		return childDocuments;
	}

	/**
	 * @param childDocuments
	 *            the childDocuments to set
	 */
	public void setChildDocuments(Set<Document> childDocuments) {
		childDocuments.remove(this);
		this.childDocuments = childDocuments;

	}

	/**
	 * 
	 * @param childDocument
	 * @return
	 */
	public boolean addChildDocument(Document childDocument) {
		if (!this.equals(childDocument))
			return getChildDocuments().add(childDocument);
		return false;

	}

	/**
	 * 
	 * @param childDocuments
	 * @return
	 */
	public boolean addAllChildDocuments(Collection<Document> childDocuments) {
		childDocuments.remove(this);
		return getChildDocuments().addAll(childDocuments);
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	public boolean removeChildDocument(Document childDocument) {
		return getChildDocuments().remove(childDocument);
	}

	/**
	 * @return the documentDatas
	 */
	public Collection<DocumentData> getDocumentDatas() {
		return documentDatas;
	}

	/**
	 * @param documentDatas
	 *            the documentDatas to set
	 */
	public void setDocumentDatas(Collection<DocumentData> documentDatas) {
		this.documentDatas = documentDatas;
	}

	/**
	 * 
	 * @param documentData
	 * @return
	 */
	public boolean addDocumentData(DocumentData documentData) {
		return getDocumentDatas().add(documentData);
	}

	/**
	 * 
	 * @param documentData
	 * @return
	 */
	public boolean removeDocumentData(DocumentData documentData) {
		return getDocumentDatas().remove(documentData);
	}

	/**
	 * @return the metadatas
	 */
	public Collection<Metadata> getMetadatas() {
		return metadatas;
	}

	/**
	 * @param metadatas
	 *            the metadatas to set
	 */
	public void setMetadatas(Collection<Metadata> metadatas) {
		this.metadatas = metadatas;
	}

	/**
	 * 
	 * @param metadata
	 * @return
	 */
	public boolean addMetadata(Metadata metadata) {
		return getMetadatas().add(metadata);
	}

	/**
	 * 
	 * @param metadata
	 * @return
	 */
	public boolean removeMetadata(Metadata metadata) {
		return getMetadatas().remove(metadata);
	}

	/**
	 * 
	 * 
	 * /**
	 * 
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
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
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

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if ((obj != null) && (obj instanceof Document)) {
			Document document = (Document) obj;
			if (isPersisted(this) && isPersisted(document)) {
				result = (this.getId() == document.getId());
			} else {
				// TODO: comparar URLs, considerar iguais URL que levam à mesma
				// página web
				result = getUrl().equalsIgnoreCase(document.getUrl());
			}
		}
		return result;
	}

	public int compareTo(Document document) {
		int result = 0;
		if (isPersisted(this) && isPersisted(document)) {
			result = (new Long(this.getId())).compareTo(new Long(document
					.getId()));
		} else {
			// TODO: comparar URLs, considerar iguais URL que levam à mesma
			// página web
			result = getUrl().compareToIgnoreCase(document.getUrl());
		}
		return result;
	}

	private boolean isPersisted(Document document) {
		boolean result = false;
		if ((document != null) && (document.getId() != 0))
			result = true;
		return result;
	}

	@Override
	public Document clone() throws CloneNotSupportedException {
		Document document = new Document();
		document.setDataSet(getDataSet());
		document.setUrl(getUrl());
		document.setScore(getScore());
		Collection<Metadata> metadatasOld = getMetadatas();
		for (Metadata metadataOfDataSetFather : metadatasOld) {
			Metadata metadata = metadataOfDataSetFather.clone();
			metadata.setDocument(document);
			document.addMetadata(metadata);
		}
		return document;
	}

	@Override
	public String toString() {
		return String.format("%s", getUrl());
	}

	/**
	 * @return the fuzzy
	 */
	public double getFuzzy() {
		return fuzzy;
	}

	/**
	 * @param fuzzy the fuzzy to set
	 */
	public void setFuzzy(double fuzzy) {
		this.fuzzy = fuzzy;
	}

}
