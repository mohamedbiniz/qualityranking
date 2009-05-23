package br.test.htmlcapture;
import junit.framework.TestCase;
import org.htmlparser.beans.StringBean;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Jan 22, 2006
 * Time: 3:32:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentExtractorTest extends TestCase {

    public void testContentExtractorTest(){
        StringBean sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (true);
        sb.setURL ("/opt/lixo/foreach.html");
        String s = sb.getStrings ();
        System.out.println(s);

        assertNotNull(s);

    }
}
