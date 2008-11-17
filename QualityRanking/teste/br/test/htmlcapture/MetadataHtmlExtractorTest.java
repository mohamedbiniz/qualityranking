package br.test.htmlcapture;

import junit.framework.TestCase;
import br.ufrj.htmlbase.extractor.metadata.MetadataHtmlExtractorBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 22, 2006
 * Time: 1:19:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataHtmlExtractorTest extends TestCase {


    public void testMetadataHtmlExtractor(){

        MetadataHtmlExtractorBuilder t = new MetadataHtmlExtractorBuilder();

        t.makeMetadata(null);


    }
}
