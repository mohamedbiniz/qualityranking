/**
 * 
 */
package trab02.indexing;

import java.util.HashMap;
import java.util.List;

import beans.Artigo;
import beans.words.Word_Artigos;
import engine.DB.HibernateDAO;

/**
 * @author Fabricio
 * 
 */
public abstract class ReverseListWordDocsDB<T extends Word_Artigos> extends
		ReverseListWordDocs<T> {

	protected abstract T newInstanceWord_Artigo();

	public final void popular(HibernateDAO dao) throws InstantiationException,
			IllegalAccessException {
		T classInstance = newInstanceWord_Artigo();
		List<T> listWords_Artigos = getListWords_Artigos(classInstance);

		for (T word_Artigos : listWords_Artigos) {
			dao.create(word_Artigos);
		}
	}

	public HashMap<String, HashMap<Artigo, Long>> getReverseListOfDB(
			HibernateDAO dao) throws Exception {
		List<T> words_artigos = (List<T>) dao.listAll(newInstanceWord_Artigo()
				.getClass());
		return (new ReverseListWordDocs<T>(words_artigos)).getReverseList();
	}
}
