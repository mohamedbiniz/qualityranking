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
import com.dialogy.odb.test.vo.crawler.SearchElement;
import com.dialogy.odb.tool.IOUtil;


/**
 * Created by IntelliJ IDEA.
 * User: olivier s
 * Date: 04/12/2005
 * Time: 19:21:28
 * To change this template use File | Settings | File Templates.
 */
public class TestCrawler2 extends TestCase {


    protected void setUp() throws Exception {
        ODB odb = ODB.open("crawler2.odb");

        Metadata metadata = new Metadata();
        metadata.setId("1");
        metadata.setProperty("name");
        metadata.setValue("olivier");
        
        List<Metadata> lmetadata = new ArrayList<Metadata>();
        lmetadata.add(metadata);
        
        OutputLink outputLink = new OutputLink(1,new Date(),"http://www.dialogy.com");
        List<OutputLink> loutlinks = new ArrayList<OutputLink>();
        loutlinks.add(outputLink);
        
        Page page = new Page();
        page.setId(101);
        page.setContent("fjsdfkljsdfsdfjksçdlfjksdkljfkdlsjfkls");
        page.setMetadata(lmetadata);
        page.setOutputLinks(loutlinks);
        page.setPath("/tmp/page");
        page.setFirstFetch(1);
        page.setScore(1.45F);
                
        odb.store(page);

        odb.commitAndClose();
    }

    public void testGetAll() throws Exception {
        ODB odb = ODB.open("crawler2.odb");
        List l = odb.getObjectsOf(Page.class, true);
        assertEquals(1, l.size());

        //assertSearchElement((SearchElement) l.get(0));
        odb.close();
    }


    public void testGetNativeQuery() throws Exception {
        ODB odb = ODB.open("crawler2.odb");

        // native query
        IQuery nq = new ISimpleNativeQuery() {
            public boolean match(Page page) {
                return page.getId() == 101;
            }
        };
        List l = odb.getObjects(nq, true);
        System.out.println(l);
        assertEquals(1, l.size());

        //assertSearchElement((SearchElement) l.get(0));
        odb.close();
    }

    public void testSodaQuery() throws Exception {
        ODB odb = ODB.open("crawler2.odb");

        // soda query
        IQuery sodaquery = new SodaQuery(Page.class, Soda.equal("path", "/tmp/page"));

        List l = odb.getObjects(sodaquery, true);
        assertEquals(1, l.size());

        //assertSearchElement((SearchElement) l.get(0));
        odb.close();
    }

    public void testUpdate() throws Exception {
        ODB odb = ODB.open("crawler2.odb");

        // soda query
        IQuery sodaquery = new SodaQuery(Page.class, Soda.equal("path", "/tmp/page"));

        List l = odb.getObjects(sodaquery, true);
        assertEquals(1, l.size());
        
        Page page = (Page) l.get(0);
        page.setPath("/tmp/page/page1");
        odb.store(page);
        odb.commitAndClose();

        odb = ODB.open("crawler2.odb");

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

    private void assertSearchElement(SearchElement se) {
        assertEquals(40, se.getFetchInterval());
        assertEquals(100, se.getId());
        assertEquals(45500, se.getNextFetch());
        assertEquals(40, se.getNumOutlinks());
        assertEquals(3, se.getRetries());
        assertEquals("" + 0.89, "" + se.getScore());
        assertEquals("http://www.google.com", se.getUrl());
        assertEquals(1, se.getVersion());
    }

    protected void tearDown() throws Exception {
        IOUtil.deleteFile("crawler2.odb");
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
