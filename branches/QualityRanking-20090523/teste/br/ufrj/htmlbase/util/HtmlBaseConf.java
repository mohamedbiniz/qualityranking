package br.ufrj.htmlbase.util;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 12, 2006
 * Time: 9:53:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlBaseConf {

    //TODO ver o NutchConf para ter ideias
    /*
    Definir se usarei properties ou .xml

    */

    public static final String ODB_FILE_NAME = "html_base.odb";

    private String home;
    private int dataBase;
    private int topLinks;
    private int numeroMaximoPaginas;

   
    public String getHome() {
        return home;  //To change body of created methods use File | Settings | File Templates.
    }

    public int getDataBase() {
        return dataBase;
    }

    public int getTopLinks() {
        return topLinks;  //To change body of created methods use File | Settings | File Templates.
    }
    
    public int getNumeroMaximoPaginas() {
        return numeroMaximoPaginas;  //To change body of created methods use File | Settings | File Templates.
    }

    public void setHome(String home) {
        this.home = home;
    }

    public void setDataBase(int dataBase) {
        this.dataBase = dataBase;
    }

    public void setTopLinks(int topLinks) {
        this.topLinks = topLinks;
    }

    public void setNumeroMaximoPaginas(int numeroMaximoPaginas) {
        this.numeroMaximoPaginas = numeroMaximoPaginas;
    }
}
