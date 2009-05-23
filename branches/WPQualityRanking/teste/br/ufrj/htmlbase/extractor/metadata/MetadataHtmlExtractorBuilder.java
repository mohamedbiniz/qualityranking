package br.ufrj.htmlbase.extractor.metadata;

import br.ufrj.htmlbase.PageCrawler;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 22, 2006
 * Time: 1:00:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataHtmlExtractorBuilder extends MetadataBuilder{


    private static Logger logger = Logger.getLogger(MetadataHtmlExtractorBuilder.class);
    public void makeMetadata(PageCrawler page) {


        Parser parser = null;
        String title = null;

        MetadataList list = page.getMetadata();
        try {

           String path = page.getPath();

            parser = new Parser (path);
            NodeList nl = parser.parse (new TagNameFilter ("META")); // here is your two node list

            for(int i =0; i < nl.size(); i++){
                MetaTag t = (MetaTag) nl.elementAt(i);

                String tagName = t.getMetaTagName();
                
                if(tagName==null || tagName.trim().length()==0)
                    tagName = t.getHttpEquiv();
                
                String content = t.getMetaContent();
                
                MetadataCrawler m = new MetadataCrawler(tagName, content);

                list.add(m);


            }



        } catch (ParserException e) {
            logger.fatal("Erro na tentativa de extrair os META's do HTML :: ",e);
        }

        if (sucessor != null){
            sucessor.makeMetadata(page);
       }

    }


}
