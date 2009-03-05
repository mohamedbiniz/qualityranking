/**
 * 
 */
package br.ufrj.cos.foxset.search;

import java.net.ConnectException;

/**
 * @author Fabricio
 * 
 */
public abstract class ThreadFoxSet extends Thread {

	private boolean pronto = false;

	public abstract void execute() throws ConnectException;

	@Override
	public void run() {
		super.run();
		try {
			execute();
		} catch (ConnectException ce) {
			ce.printStackTrace();
		} finally {
			setPronto(true);
		}
	}

	/**
	 * @return the pronto
	 */
	public final boolean isPronto() {
		return pronto;
	}

	/**
	 * @param pronto
	 *            the pronto to set
	 */
	public final void setPronto(boolean pronto) {
		this.pronto = pronto;
	}
}
