/**
 * 
 */
package br.ufrj.cos.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Fabricio
 * 
 */
@Table(name = "DataSet_Collaborator")
@Entity
public class DataSetCollaborator implements Serializable, Cloneable {

	@Transient
	private static final char ROLE_TYPE_E = 'E';

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private DataSetCollaboratorPk id;

	@Column(nullable = false)
	private char role;

	public DataSetCollaborator() {
		setId(new DataSetCollaboratorPk());
		setRole(ROLE_TYPE_E);
	}

	/**
	 * @return the id
	 */
	public DataSetCollaboratorPk getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(DataSetCollaboratorPk id) {
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
	 * @return the collaborator
	 */
	public Collaborator getCollaborator() {
		return getId().getCollaborator();
	}

	/**
	 * @param collaborator
	 *            the collaborator to set
	 */
	public void setCollaborator(Collaborator collaborator) {
		getId().setCollaborator(collaborator);
	}

	/**
	 * @return the role
	 */
	public char getRole() {
		return role;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	public void setRole(char role) {
		this.role = role;
	}

	@Override
	public DataSetCollaborator clone() throws CloneNotSupportedException {
		DataSetCollaborator dataSetCollaborator = new DataSetCollaborator();
		dataSetCollaborator.setCollaborator(getCollaborator());
		dataSetCollaborator.setDataSet(getDataSet());
		dataSetCollaborator.setRole(getRole());
		return dataSetCollaborator;
	}
}
