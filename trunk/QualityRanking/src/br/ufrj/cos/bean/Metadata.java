/**
 * 
 */
package br.ufrj.cos.bean;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.ufrj.cos.enume.MetadataType;

/**
 * @author Fabricio
 * 
 */
@Table
@Entity
public class Metadata implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MetadataType type;

	@Column(nullable = false)
	@Lob
	private byte[] value;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Document document;

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
	 * @return the type
	 */
	public MetadataType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(MetadataType type) {
		this.type = type;
	}

	/**
	 * @return the value
	 */
	public byte[] getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(byte[] value) {
		this.value = value;
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

}
