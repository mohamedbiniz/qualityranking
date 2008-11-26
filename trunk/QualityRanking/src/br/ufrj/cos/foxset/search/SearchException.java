/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrj.cos.foxset.search;

/**
 * 
 * @author Heraldo
 */
public class SearchException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchException(String message) {
		super(message);
	}

	public SearchException(String message, Throwable cause) {
		super(message, cause);
	}
}
