/**
 * 
 */
package beans.words;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import beans.Artigo;

/**
 * @author Fabricio
 * 
 */
@Table(name = "Words_ArtigosAll")
@Entity
public class Word_ArtigosAll extends Word_Artigos {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String word;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@PrimaryKeyJoinColumn
	@JoinColumn(nullable = false)
	private Artigo artigo;

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word
	 *            the word to set
	 */
	public void setWord(String word) {
		this.word = word;
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
}
