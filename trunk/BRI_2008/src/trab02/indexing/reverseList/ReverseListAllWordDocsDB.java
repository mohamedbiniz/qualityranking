/**
 * 
 */
package trab02.indexing.reverseList;

import trab02.indexing.ReverseListWordDocsDB;
import beans.words.Word_ArtigosAll;

/**
 * @author Fabricio
 * 
 */
public class ReverseListAllWordDocsDB extends
		ReverseListWordDocsDB<Word_ArtigosAll> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see trab02.indexing.ReverseListWordDocsDB#newInstanceWord_Artigo()
	 */
	@Override
	protected Word_ArtigosAll newInstanceWord_Artigo() {
		return new Word_ArtigosAll();
	}

}
