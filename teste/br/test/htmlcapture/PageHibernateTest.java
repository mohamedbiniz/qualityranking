/*
 * PageHibernateTest.java
 *
 * Created on March 21, 2006, 11:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package br.test.htmlcapture;

import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;

import br.ufrj.htmlbase.OutputLinkCrawler;
import br.ufrj.htmlbase.db.hibernate.HibernateSessionFactory;

/**
 *
 * @author mayworm
 */
public class PageHibernateTest  extends TestCase{
    
    /** Creates a new instance of PageHibernateTest */
    public PageHibernateTest() {
    }
  /*
    public void testPage(){
        
        String url = null;

        try {
                url = "http://www.bookpool.com/bs";
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            Page p = null;
            try {
                p = new Page(url);
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            if(p.process()){

                try {
                    PageBD bd = new PageHibernateImpl();

                    bd.save(p);
                    
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } 
            }
            
    }
     */
    public void testLink(){
        Session session = HibernateSessionFactory.currentSession();
        Transaction tx = session.beginTransaction();

     
      long l = -9134976161665755795L;

		// RSN @SuppressWarnings("unchecked")
		@SuppressWarnings("unchecked")
        List<OutputLinkCrawler> result = session.createCriteria(OutputLinkCrawler.class).add(Expression.eq("idTest",l)).list();
        System.out.println(result.size());
        
        for(int i =0;i<result.size();i++){
                
                    OutputLinkCrawler u = result.get(i);
                    u.setVisited(false);
                    session.update(u);
                    session.flush();
                    session.clear();
        }
        
        tx.commit();
        HibernateSessionFactory.closeSession();
    }
     
}
