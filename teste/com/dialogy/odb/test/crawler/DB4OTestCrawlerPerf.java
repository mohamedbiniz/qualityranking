package com.dialogy.odb.test.crawler;

import junit.framework.TestCase;


/**
 * Created by IntelliJ IDEA.
 * User: olivier s
 * Date: 04/12/2005
 * Time: 19:21:28
 * To change this template use File | Settings | File Templates.
 */
public class DB4OTestCrawlerPerf extends TestCase {

	/*
    public void testInsert() throws Exception {
        int pageNumber = 10000;
        int metaDataNumber = 5;
        int linkNumber = 5;
        OutputLink outputLink = null;
        Metadata metadata = null;
        List<Metadata> lmetadata = null;
        List<OutputLink> loutputLinks = null;
        
        ObjectContainer oc = Db4o.openFile("crawler2Perf.db4o");
        long idMetadata = 1;
        long idOutputLink = 1;
        long start = System.currentTimeMillis();
        System.out.println("DB4O : Opening database with ?");// +oc.query(Page.class).size() + " pages");
        
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
                    
            //odb.store(page);
            oc.set(page);
            idMetadata++;
            idOutputLink++;
            
        }

        System.out.println("commiting : " + (System.currentTimeMillis()-start) + " ms");
        oc.close();
        System.out.println("end of insert : " + (System.currentTimeMillis()-start) + " ms");
    }

    public void testGetAll() throws Exception {
        //ObjectContainer oc = Db4o.openFile("crawler2Perf.db4o");
        //System.out.println(oc.odb.getNumberOfObjectsOf(Page.class) + " pages");
        //System.out.println(odb.getNumberOfObjectsOf(Metadata.class) + " MetaData");
        //System.out.println(odb.getNumberOfObjectsOf(OutputLink.class) + " Output Links");
        //odb.close();
    }


    public void testGetNativeQuery() throws Exception {
        long start = System.currentTimeMillis();
        
        ObjectContainer oc = Db4o.openFile("crawler2Perf.db4o");

        System.out.println("Start of Native Query over " + oc.query(Page.class).size() +" pages");
        // native query
        Predicate p = new Predicate <Page> ()
            {
            public boolean match(Page page) {
                return page.getId() < 100;
            };   
            };
        List l = oc.query(p);
        
        //assertTrue(l.size()>1);

        oc.close();
        
        System.out.println("end of native : " + (System.currentTimeMillis()-start) + " ms");
    }

    public void testSodaQuery() throws Exception {
        
        long start = System.currentTimeMillis();
        ObjectContainer oc = Db4o.openFile("crawler2Perf.db4o");
        
        System.out.println("Start of Soda Query over " + oc.query(Page.class).size() +" pages");

        // soda query
        IQuery sodaquery = new SodaQuery(Page.class, Soda.equal("path", "/tmp/page/50000"));

        Query q = oc.query();
        q.descend("path").constrain("/tmp/page/50000");
        List l = q.execute();
        //assertTrue(l.size()>1);

        //assertSearchElement((SearchElement) l.get(0));
        oc.close();
        
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
    */
}
