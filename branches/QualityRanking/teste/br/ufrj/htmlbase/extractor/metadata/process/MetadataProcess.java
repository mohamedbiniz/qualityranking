package br.ufrj.htmlbase.extractor.metadata.process;

import br.ufrj.htmlbase.PageCrawler;
import br.ufrj.htmlbase.extractor.metadata.MetadataBuilder;
import br.ufrj.htmlbase.extractor.metadata.MetadataDublinCoreBuilder;
import br.ufrj.htmlbase.extractor.metadata.MetadataHtmlExtractorBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 22, 2006
 * Time: 5:07:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataProcess {

    public void execute(PageCrawler request){


        MetadataBuilder builder1 = new MetadataDublinCoreBuilder();

        MetadataBuilder builder2 = new MetadataHtmlExtractorBuilder();

        builder1.setSucessor(builder2);

        builder1.makeMetadata(request);

    }
}
