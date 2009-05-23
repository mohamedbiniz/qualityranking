package com.dialogy.odb.test.crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import com.dialogy.odb.core.query.IQuery;
import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.core.query.soda.Soda;
import com.dialogy.odb.core.query.soda.SodaQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.vo.crawler.Metadata;
import com.dialogy.odb.test.vo.crawler.OutputLink;
import com.dialogy.odb.test.vo.crawler.Page;


/**
 * Created by IntelliJ IDEA.
 * User: olivier s
 * Date: 04/12/2005
 * Time: 19:21:28
 * To change this template use File | Settings | File Templates.
 */
public class TestCrawlerPerf extends TestCase {


    public void testInsert() throws Exception {
        int pageNumber = 10000;
        int metaDataNumber = 5;
        int linkNumber = 5;
        OutputLink outputLink = null;
        Metadata metadata = null;
        List<Metadata> lmetadata = null;
        List<OutputLink> loutputLinks = null;
        ODB odb = ODB.open("crawler2Perf.odb");
        long idMetadata = 1;
        long idOutputLink = 1;
        long start = System.currentTimeMillis();
        System.out.println("ODB : Opening database with " + odb.getNumberOfObjectsOf(Page.class) + " pages");
        
        for(int i=0;i<pageNumber;i++){
            if(i!=0 && i%10000==0){
                System.out.println(i+" pages");
            }
            
            lmetadata = new ArrayList<Metadata>();
            for(int j=0;j<metaDataNumber;j++){
                metadata = new Metadata();
                metadata.setId(String.valueOf(idMetadata));
                metadata.setProperty("name " + idMetadata);
                metadata.setValue("chabou chaby " + idMetadata);
                lmetadata.add(metadata);
            }

            loutputLinks = new ArrayList<OutputLink>();
            for(int j=0;j<linkNumber;j++){
                outputLink = new OutputLink(idOutputLink,new Date(),"http://www.dialogy.com/"+idOutputLink);
                loutputLinks.add(outputLink);
            }
            
            Page page = new Page();
            page.setId(i);
            page.setContent("hdsfhgdfjhldjhvdjfhgvklfhdglkdhfvkjdfbvkhdgkljhfdlhvkhdfvkhdfgbklhdfglkjhafdlghdaflghdkghlhdglkdfhlkgjhdaflghdlfhgldjhfghdvgkhsfhklhgklhkjhfdkgjdhfkgvhjghlghkhklshlkhgklahdgkjlahfdkhgfdjhghkjhjkhaaklhgkladfhgklahdgkljhdfjkhglfadjhgksalghaklghdfhglkahdgkjhdafvdsjh");
            page.setMetadata(lmetadata);
            page.setOutputLinks(loutputLinks);
            page.setPath("/tmp/page/"+i);
            page.setFirstFetch(1);
            page.setScore(1.45F);
                    
            odb.store(page);
            idMetadata++;
            idOutputLink++;
            
        }

        System.out.println("commiting : " + (System.currentTimeMillis()-start) + " ms");
        odb.commitAndClose();
        System.out.println("end of insert : " + (System.currentTimeMillis()-start) + " ms");
    }

    public void testGetAll() throws Exception {
        ODB odb = ODB.open("crawler2Perf.odb");
        System.out.println(odb.getNumberOfObjectsOf(Page.class) + " pages");
        System.out.println(odb.getNumberOfObjectsOf(Metadata.class) + " MetaData");
        System.out.println(odb.getNumberOfObjectsOf(OutputLink.class) + " Output Links");
        odb.close();
    }


    public void testGetNativeQuery() throws Exception {
        long start = System.currentTimeMillis();
        
        ODB odb = ODB.open("crawler2Perf.odb");

        System.out.println("Start of Native Query over " + odb.getNumberOfObjectsOf(Page.class) +" pages");
        // native query
        IQuery nq = new ISimpleNativeQuery() {
            public boolean match(Page page) {
                return page.getId() < 100;
            }
        };
        List l = odb.getObjects(nq, true);
        
        //assertTrue(l.size()>1);

        odb.close();
        
        System.out.println("end of native : " + (System.currentTimeMillis()-start) + " ms");
    }

    public void testSodaQuery() throws Exception {
        
        long start = System.currentTimeMillis();
        ODB odb = ODB.open("crawler2Perf.odb");
        
        System.out.println("Start of Soda Query over " + odb.getNumberOfObjectsOf(Page.class) +" pages");

        // soda query
        IQuery sodaquery = new SodaQuery(Page.class, Soda.equal("path", "/tmp/page/50000"));

        List l = odb.getObjects(sodaquery, true);
        //assertTrue(l.size()>1);

        //assertSearchElement((SearchElement) l.get(0));
        odb.close();
        
        System.out.println("end of soda query : " + (System.currentTimeMillis()-start) + " ms");
    }

    public void tes2tUpdate() throws Exception {
        ODB odb = ODB.open("crawler2Perf.odb");

        // soda query
        IQuery sodaquery = new SodaQuery(Page.class, Soda.equal("path", "/tmp/page"));

        List l = odb.getObjects(sodaquery, true);
        assertEquals(1, l.size());
        
        Page page = (Page) l.get(0);
        page.setPath("/tmp/page/page1");
        odb.store(page);
        odb.commitAndClose();

        odb = ODB.open("crawler2Perf.odb");

        // Check if searchelement with url equals to google still exist - it must not
        sodaquery = new SodaQuery(Page.class, Soda.equal("path", "/tmp/page"));

        l = odb.getObjects(sodaquery, true);
        assertEquals(0, l.size());

        // Now Check if searchelement with url equals to dialogy.com exist !
        sodaquery = new SodaQuery(Page.class, Soda.equal("path", "/tmp/page/page1"));

        l = odb.getObjects(sodaquery, true);
        assertEquals(1, l.size());
        odb.close();

    }

   protected void tearDown() throws Exception {
        //IOUtil.deleteFile("crawler2.odb");
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
