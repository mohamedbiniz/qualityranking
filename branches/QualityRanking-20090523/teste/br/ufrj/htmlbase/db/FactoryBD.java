/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 15, 2006
 * Time: 9:55:47 PM
 * To change this template use File | Settings | File Templates.
 */
package br.ufrj.htmlbase.db;

import org.apache.log4j.Logger;

import br.ufrj.htmlbase.db.hibernate.PageHibernateImpl;
import br.ufrj.htmlbase.db.hsqldb.PageBDImpl;
import br.ufrj.htmlbase.util.HtmlBaseConfProvider;

public class FactoryBD {
    
     private static Logger logger = Logger.getLogger(FactoryBD.class);


    HtmlBaseConfProvider provider = HtmlBaseConfProvider.getInstance();
    
    public final int DB = 2;//provider.getDataBase();

    private static FactoryBD ourInstance = new FactoryBD();

    public static FactoryBD getInstance() {
        return ourInstance;
    }

    private FactoryBD() {
    }


     public PageBD createPage() {

         PageBD dao = null;

        switch (DB){
            case 1:{
                try {
                    dao = new PageBDImpl("db_pages");
                    //db.update("CREATE TABLE pages ( id BIGINT, content LONGVARCHAR, url VARCHAR, path_page VARCHAR)");
                } catch (Exception ex1) {
                    logger.fatal(ex1); 
                }

                break;
            }


            case 2:{
                try {
                    dao = new PageHibernateImpl();
                    
                } catch (Exception ex1) {
                    logger.fatal(ex1);
                }
            }




        }

        return dao;
     }


}
