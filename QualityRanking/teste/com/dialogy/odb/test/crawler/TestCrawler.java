package com.dialogy.odb.test.crawler;

import java.util.List;

import junit.framework.TestCase;

import com.dialogy.odb.core.query.IQuery;
import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.core.query.soda.Soda;
import com.dialogy.odb.core.query.soda.SodaQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.vo.crawler.SearchElement;
import com.dialogy.odb.tool.IOUtil;


/**
 * Created by IntelliJ IDEA.
 * User: olivier s
 * Date: 04/12/2005
 * Time: 19:21:28
 * To change this template use File | Settings | File Templates.
 */
public class TestCrawler extends TestCase {


    protected void setUp() throws Exception {
        ODB odb = ODB.open("crawler.odb");

        SearchElement se = new SearchElement();
        se.setFetchInterval((byte) 40);
        se.setId(100);
        se.setNextFetch(45500);
        se.setNumOutlinks(40);
        se.setRetries((byte) 3);
        se.setScore((float) 0.89);
        se.setUrl("http://www.google.com");
        se.setVersion((byte) 1);

        odb.store(se);

        se = new SearchElement();
        se.setFetchInterval((byte) 41);
        se.setId(101);
        se.setNextFetch(45501);
        se.setNumOutlinks(41);
        se.setRetries((byte) 4);
        se.setScore((float) 0.90);
        se.setUrl("http://www.google1.com?sdjkhfsdjkfhsdjkfhsdjkhfjkashfsdjkahfkajsdhfjksdhfkjsdhfkjsdhfksdja");
        se.setVersion((byte) 2);

        odb.store(se);
        odb.commitAndClose();
    }

    public void testGetAll() throws Exception {
        ODB odb = ODB.open("crawler.odb");
        List l = odb.getObjectsOf(SearchElement.class, true);
        assertEquals(2, l.size());

        assertSearchElement((SearchElement) l.get(0));
        odb.close();
    }


    public void testGetNativequery() throws Exception {
        ODB odb = ODB.open("crawler.odb");

        // native query
        IQuery nq = new ISimpleNativeQuery() {
            public boolean match(SearchElement se) {
                return se.getUrl().equals("http://www.google.com");
            }
        };
        List l = odb.getObjects(nq, true);
        assertEquals(1, l.size());

        assertSearchElement((SearchElement) l.get(0));
        odb.close();
    }

    public void testSodaQuery() throws Exception {
        ODB odb = ODB.open("crawler.odb");

        // soda query
        IQuery sodaquery = new SodaQuery(SearchElement.class, Soda.equal("url", "http://www.google.com"));

        List l = odb.getObjects(sodaquery, true);
        assertEquals(1, l.size());

        assertSearchElement((SearchElement) l.get(0));
        odb.close();
    }

    public void testUpdate() throws Exception {
        ODB odb = ODB.open("crawler.odb");

        // soda query
        IQuery sodaquery = new SodaQuery(SearchElement.class, Soda.equal("url", "http://www.google.com"));

        List l = odb.getObjects(sodaquery, true);
        assertEquals(1, l.size());
        SearchElement se = (SearchElement) l.get(0);
        se.setUrl("http://www.dialogy.com");
        odb.store(se);
        odb.commitAndClose();

        odb = ODB.open("crawler.odb");

        // Check if searchelement with url equals to google still exist - it must not
        sodaquery = new SodaQuery(SearchElement.class, Soda.equal("url", "http://www.google.com"));

        l = odb.getObjects(sodaquery, true);
        assertEquals(0, l.size());

        // Now Check if searchelement with url equals to dialogy.com exist !
        sodaquery = new SodaQuery(SearchElement.class, Soda.equal("url", "http://www.dialogy.com"));

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
        IOUtil.deleteFile("crawler.odb");
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
    }

}
