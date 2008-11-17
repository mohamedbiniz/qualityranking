/**
 * 
 */
package br.ufrj.cos.bri.bean;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Fabricio
 * 
 */
@Embeddable
public class DataSetCollaboratorPk implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private DataSet dataSet;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn
	private Collaborator collaborator;

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

}
