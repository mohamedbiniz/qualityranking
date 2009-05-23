package br.test.htmlcapture;
import java.net.URL;

import junit.framework.TestCase;

import org.htmlparser.beans.LinkBean;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Jan 22, 2006
 * Time: 2:37:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkExtractorTest extends TestCase {

    public void testLinkExtractor(){

     LinkBean sb = new LinkBean ();
     //sb.setLinks (false);
     //sb.setReplaceNonBreakingSpaces (true);
     //sb.setCollapse (true);
     sb.setURL ("http://www.aprendendoingles.com.br/arquivo.shtml"); // the HTTP is performed here
     URL[] urls = sb.getLinks ();

     for(URL u: urls)
        System.out.println(u.toString());

     assertEquals(0, urls.length);




    }
}
