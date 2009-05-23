package br.ufrj.htmlbase.filter;

import br.ufrj.htmlbase.OutputLinkCrawler;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 8, 2006
 * Time: 5:36:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidFilterDomain extends ValidFilters{

    private String[] listValidDomain = {"amazon","bookpool"};

   protected boolean isAcceptable(OutputLinkCrawler link) {

        boolean allOk = false;

       // for(String domain : listValidDomain)
         //   if (link.getDomain().contains(domain))
                allOk = true;

        return allOk;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
