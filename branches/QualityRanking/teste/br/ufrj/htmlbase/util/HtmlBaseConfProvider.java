/*
 * HtmlBaseConfProvider.java
 *
 * Created on March 10, 2006, 5:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.ufrj.htmlbase.util;

import br.ufrj.cos.bean.DataSet;

/**
 * 
 * @author mayworm
 */
public class HtmlBaseConfProvider {

	private static HtmlBaseConfProvider instance = null;

	// private HtmlBaseConf conf = null;

	/** Creates a new instance of HtmlBaseConfProvider */
	private HtmlBaseConfProvider() {

		// try {
		// ODB odb;
		// odb = ODB.open(HtmlBaseConf.ODB_FILE_NAME);
		//            
		// List confAtual = odb.getObjectsOf(HtmlBaseConf.class,true);
		//            
		// if(!confAtual.isEmpty()){
		//
		// HtmlBaseConf conf = (HtmlBaseConf)confAtual.get(0);
		// setHtmlBaseConf(conf);
		//
		// }
		//           
		// odb.commit();
		// odb.close();
		//            
		// }catch(Exception e){
		// e.printStackTrace();
		// }

	}

	public static HtmlBaseConfProvider getInstance() {
		if (instance == null) {
			instance = new HtmlBaseConfProvider();
		}

		return instance;
	}

	// public void setHtmlBaseConf(HtmlBaseConf c){
	// conf = c;
	// }
	// public String getHome() {
	// return conf.getHome(); //To change body of created methods use File |
	// Settings | File Templates.
	// }
	//
	// public int getDataBase() {
	// return conf.getDataBase();
	// }

	public int getTopLinks() {
		return 10;// conf.getTopLinks(); //To change body of created methods
		// use File | Settings | File Templates.
	}

	public int getNumeroMaximoPaginas(DataSet dataSet) {

		return getMinPagDB(dataSet);

		// return 1000;// conf.getNumeroMaximoPaginas(); //To change body of
		// created methods use File | Settings | File Templates.
	}

	private int getMinPagDB(DataSet dataSet) {
		return dataSet.getMinQuantityPages();
	}

	public void renew() {
		instance = null;
	}

}
