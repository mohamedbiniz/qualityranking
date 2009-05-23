/**
 * 
 */
package br.ufrj.cos.matlab.exception;

/**
 * @author Fabricio
 * 
 */
public class MatLabException extends Exception {

	/**
	 * 
	 */
	public MatLabException() {
	}

	/**
	 * @param arg0
	 */
	public MatLabException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public MatLabException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public MatLabException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
