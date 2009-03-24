package beans;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author Oliverio
 * 
 */
@Table(name = "Itens")
@Entity
public class Item implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Transient
	private int numeroQuery;

	@Transient
	private int numeroArtigo;

	@Transient
	private String notas;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Query query;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Artigo artigo;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Score score;

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
	 * @return the numeroQuery
	 */
	public int getNumeroQuery() {
		return numeroQuery;
	}

	/**
	 * @param numeroQuery
	 *            the numeroQuery to set
	 */
	public void setNumeroQuery(int numeroQuery) {
		this.numeroQuery = numeroQuery;
	}

	/**
	 * @return the numeroArtigo
	 */
	public int getNumeroArtigo() {
		return numeroArtigo;
	}

	/**
	 * @param numeroArtigo
	 *            the numeroArtigo to set
	 */
	public void setNumeroArtigo(int numeroArtigo) {
		this.numeroArtigo = numeroArtigo;
	}

	/**
	 * @return the notas
	 */
	public String getNotas() {
		return notas;
	}

	/**
	 * @param notas
	 *            the notas to set
	 */
	public void setNotas(String notas) {
		this.notas = notas;
	}

	/**
	 * @return the query
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * @return the artigo
	 */
	public Artigo getArtigo() {
		return artigo;
	}

	/**
	 * @param artigo
	 *            the artigo to set
	 */
	public void setArtigo(Artigo artigo) {
		this.artigo = artigo;
	}

	/**
	 * @return the score
	 */
	public Score getScore() {
		return score;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(Score score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s", getNumeroQuery(), getNumeroArtigo(),
				getNotas());

	}

}
