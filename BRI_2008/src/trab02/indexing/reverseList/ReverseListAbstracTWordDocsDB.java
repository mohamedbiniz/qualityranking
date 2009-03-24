/**
 * 
 */
package trab02.indexing.reverseList;

import trab02.indexing.ReverseListWordDocsDB;
import beans.words.Word_ArtigosAbstracT;

/**
 * @author Fabricio
 * 
 */
public class ReverseListAbstracTWordDocsDB extends
		ReverseListWordDocsDB<Word_ArtigosAbstracT> {

	@Override
	protected Word_ArtigosAbstracT newInstanceWord_Artigo() {
		return new Word_ArtigosAbstracT();
	}

}
