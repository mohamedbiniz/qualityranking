/**
 * 
 */
package br.ufrj.cos.bri.bean;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * @author Fabricio
 * 
 */
@Table
@Entity
public class Answer {

	@EmbeddedId
	private AnswerPk id;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Collaborator collaborator;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(name = "assessment_scale_id", nullable = false)
	private AssessmentScale assessmentScale;

	public Answer() {
		setId(new AnswerPk());
	}

	/**
	 * @return the id
	 */
	public AnswerPk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(AnswerPk id) {
		this.id = id;
	}

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return getId().getDocument();
	}

	/**
	 * @param document
	 *            the document to set
	 */
	public void setDocument(Document document) {
		getId().setDocument(document);
	}

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return getId().getQuery();
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(Query query) {
		getId().setQuery(query);
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
	 * @return the assessmentScale
	 */
	public AssessmentScale getAssessmentScale() {
		return assessmentScale;
	}

	/**
	 * @param assessmentScale
	 *            the assessmentScale to set
	 */
	public void setAssessmentScale(AssessmentScale assessmentScale) {
		this.assessmentScale = assessmentScale;
	}

}
