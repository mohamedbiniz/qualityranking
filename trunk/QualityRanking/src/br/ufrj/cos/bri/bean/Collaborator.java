package br.ufrj.cos.bri.bean;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.Email;

@Table
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Collaborator implements Serializable, Comparable<Collaborator> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false, unique = true, length = 50)
	private String username;

	@Column(nullable = false, length = 50)
	private String password;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	@Email
	private String email;

	@Column(nullable = false)
	private boolean active;

	@Column(nullable = false)
	private boolean administrator;

	@Column(nullable = false)
	private boolean coordinator;

	@OneToMany(mappedBy = "collaborator", cascade = CascadeType.ALL)
	private Collection<DataSet> dataSets;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "DataSet_Collaborator", joinColumns = @JoinColumn(name = "collaborator_id"), inverseJoinColumns = @JoinColumn(name = "dataSet_id"))
	private Collection<DataSet> dataSetsRole;

	public Collaborator() {
		setActive(true);
		setAdministrator(false);
		setCoordinator(false);
		setDataSets(new ArrayList<DataSet>());
		setDataSetsRole(new ArrayList<DataSet>());
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = md5(password);
	}

	// Função para criar hash da senha informada
	public static String md5(String senha) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
			return hash.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * @return the administrator
	 */
	public boolean isAdministrator() {
		return administrator;
	}

	/**
	 * @param administrator
	 *            the administrator to set
	 */
	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	/**
	 * @return the coordinator
	 */
	public boolean isCoordinator() {
		return coordinator;
	}

	/**
	 * @param coordinator
	 *            the coordinator to set
	 */
	public void setCoordinator(boolean coordinator) {
		this.coordinator = coordinator;
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
	 * @return the dataSetsRole
	 */
	public Collection<DataSet> getDataSetsRole() {
		return dataSetsRole;
	}

	/**
	 * @param dataSetsRole
	 *            the dataSetsRole to set
	 */
	public void setDataSetsRole(Collection<DataSet> dataSetsRole) {
		this.dataSetsRole = dataSetsRole;
	}

	/**
	 * 
	 * @param dataSetRole
	 */
	public boolean addDataSetRole(DataSet dataSetRole) {
		return getDataSetsRole().add(dataSetRole);
	}

	/**
	 * 
	 * @param dataSetRole
	 * @return
	 */
	public boolean removeDataSetRole(DataSet dataSetRole) {
		return getDataSetsRole().remove(dataSetRole);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof Collaborator) {
			Collaborator collaborator = (Collaborator) obj;
			if (getId() != 0 && collaborator.getId() != 0) {
				return getId() == collaborator.getId();
			} else {
				return getUsername().equalsIgnoreCase(
						collaborator.getUsername());
			}

		}
		return false;
	}

	public int compareTo(Collaborator collaborator) {
		if (collaborator != null) {
			if (getId() != 0 && collaborator.getId() != 0) {
				return getUsername().compareToIgnoreCase(
						collaborator.getUsername());
			} else {
				return (new Long(getId())).compareTo(new Long(collaborator
						.getId()));
			}
		}
		return 1;
	}

}
