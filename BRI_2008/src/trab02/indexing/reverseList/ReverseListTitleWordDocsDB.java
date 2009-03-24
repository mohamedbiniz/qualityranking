/**
 * 
 */
package trab02.indexing.reverseList;

import trab02.indexing.ReverseListWordDocsDB;
import beans.words.Word_ArtigosTitle;

/**
 * @author Fabricio
 * 
 */
public class ReverseListTitleWordDocsDB extends
		ReverseListWordDocsDB<Word_ArtigosTitle> {

	@Override
	protected Word_ArtigosTitle newInstanceWord_Artigo() {
		return new Word_ArtigosTitle();
	}

}
