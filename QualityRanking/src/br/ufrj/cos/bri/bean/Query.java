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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table
@Entity
public class Query implements Serializable, Comparable<Query> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String query;

	@Column(nullable = false, length = 8000)
	private String description;

	@Column(nullable = false)
	private boolean active;

	@Column(name = "creation_datetime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private DataSet dataSet;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Collaborator collaborator;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "SubSet_Query", joinColumns = @JoinColumn(name = "query_id"), inverseJoinColumns = @JoinColumn(name = "subset_id"))
	private Collection<SubSet> subSets;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Answer", joinColumns = @JoinColumn(name = "query_id"), inverseJoinColumns = @JoinColumn(name = "document_id"))
	private Collection<Document> documents;

	public Query() {
		setActive(true);
		setSubSets(new ArrayList<SubSet>());
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
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
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
	 * @return the subSets
	 */
	public Collection<SubSet> getSubSets() {
		return subSets;
	}

	/**
	 * @param subSets
	 *            the subSets to set
	 */
	public void setSubSets(Collection<SubSet> subSets) {
		this.subSets = subSets;
	}

	/**
	 * 
	 * @param subSet
	 * @return
	 */
	public boolean addSubSet(SubSet subSet) {
		return getSubSets().add(subSet);
	}

	/**
	 * 
	 * @param subSet
	 * @return
	 */
	public boolean removeSubSet(SubSet subSet) {
		return getSubSets().remove(subSet);
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

	public int compareTo(Query arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
