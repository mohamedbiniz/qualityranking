/**
 * 
 */
package br.ufrj.cos.bri.matlab;

import java.io.Serializable;

/**
 * @author <a href="fabriciorsf@cos.ufrj.br">Fabricio Raphael</a>
 * 
 */
public class JobSend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String function;
	private int tl;
	private int qd;
	private double[] cdq;
	private double[] qds;

	public JobSend(String function, int qd, double[] cdq, double[] qds) {
		setFunction(function);
		setQd(qd);
		setCdq(cdq);
		setQds(qds);
	}

	/**
	 * @return the function
	 */
	public String getFunction() {
		return function;
	}

	/**
	 * @param function
	 *            the function to set
	 */
	public void setFunction(String function) {
		this.function = function;
	}

	/**
	 * @return the tl
	 */
	public int getTl() {
		return tl;
	}

	/**
	 * @param tl
	 *            the tl to set
	 */
	public void setTl(int tl) {
		this.tl = tl;
	}

	/**
	 * @return the qd
	 */
	public int getQd() {
		return qd;
	}

	/**
	 * @param qd
	 *            the qd to set
	 */
	public void setQd(int qd) {
		this.qd = qd;
	}

	/**
	 * @return the cdq
	 */
	public double[] getCdq() {
		return cdq;
	}

	/**
	 * @param cdq
	 *            the cdq to set
	 */
	public void setCdq(double[] cdq) {
		this.cdq = cdq;
	}

	@Override
	public String toString() {

		return (getFunction() == null ? "" : getFunction());
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if ((obj != null) && (obj instanceof JobSend)) {
			JobSend jobSend = (JobSend) obj;
			if (getFunction().equals(jobSend.getFunction())) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * @return the qds
	 */
	public double[] getQds() {
		return qds;
	}

	/**
	 * @param qds
	 *            the qds to set
	 */
	public void setQds(double[] qds) {
		this.qds = qds;
	}
}