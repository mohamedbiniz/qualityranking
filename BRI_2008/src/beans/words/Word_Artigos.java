/**
 * 
 */
package beans.words;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import beans.Artigo;

/**
 * @author Fabricio
 * 
 */
@Table(name = "Words_Artigos")
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Word_Artigos implements Serializable {

	// class listainvertida{
	//		
	// TreeSet<String, Tupla<Art,qtd>> words;
	//		
	// add (String palavra, Artigo a, long qtd){
	// if (words.getKeys.contains(palavra)){
	// if (nao contem o artigo no elemento com chave palavra){
	// words.get(palavra).add(a, qtd)
	// insere do db
	// }
	// }
	// else {
	// words.add (palavra, (a, qtd))
	// insere do db
	// }
	//			
	//			
	// }
	// }

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private long qtdOcorrencias;

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
	 * @return the word
	 */
	public abstract String getWord();

	/**
	 * @param word
	 *            the word to set
	 */
	public abstract void setWord(String word);

	/**
	 * @return the artigo
	 */
	public abstract Artigo getArtigo();

	/**
	 * @param artigo
	 *            the artigo to set
	 */
	public abstract void setArtigo(Artigo artigo);

	/**
	 * @return the qtdOcorrencias
	 */
	public long getQtdOcorrencias() {
		return qtdOcorrencias;
	}

	/**
	 * @param qtdOcorrencias
	 *            the qtdOcorrencias to set
	 */
	public void setQtdOcorrencias(long qtdOcorrencias) {
		this.qtdOcorrencias = qtdOcorrencias;
	}

}
