package br.ufrj.cos.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

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
public class Document implements Serializable, Comparable<Document> {

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

	@Column(precision = 6, scale = 5)
	private BigDecimal score;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private DataSet dataSet;

	// No caso da instância ser um dos filhos
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST,
			CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	private Document document;

	// No caso da instância ser o pai
	@OneToMany(mappedBy = "document", cascade = { CascadeType.MERGE,
			CascadeType.REFRESH })
	private Collection<Document> documents;

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
		setDocuments(new ArrayList<Document>());
		setDocumentDatas(new ArrayList<DocumentData>());
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
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
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

	public int compareTo(Document o) {
		// TODO Auto-generated method stub
		return 0;
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
	public String toString() {
		return String.format("%s", getUrl());
	}

}
