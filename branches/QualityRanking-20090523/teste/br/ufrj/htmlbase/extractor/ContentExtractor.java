package br.ufrj.htmlbase.extractor;

import org.apache.log4j.Logger;
import org.htmlparser.beans.StringBean;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 1, 2006
 * Time: 11:28:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentExtractor {

    private static Logger logger = Logger.getLogger(ContentExtractor.class);
    
    public static String getContentExtracted(String path){

        StringBean sb = new StringBean ();
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (true);
        sb.setURL (path);
        String s = sb.getStrings ();
       
        logger.debug("Tamanho em caracteres do conteudo extraido :: "+s.length());
        
        return s;
    }
}
