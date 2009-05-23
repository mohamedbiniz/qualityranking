package br.ufrj.htmlbase.filter;

import br.ufrj.htmlbase.OutputLinkCrawler;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 8, 2006
 * Time: 5:31:36 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class ValidFilters {


    void setSucessor(ValidFilters sucessor) {
        this.sucessor = sucessor;
    }

    protected ValidFilters sucessor;

  abstract protected boolean isAcceptable(OutputLinkCrawler link);


  Collection sift( Collection links ){

    Collection result = new ArrayList();
      for (Object link : links) {
          OutputLinkCrawler quote = (OutputLinkCrawler) link;
          if (isAcceptable(quote)) {
              result.add(quote);
          }
      }
    return result;
  }

    public Collection handleRequest(Collection links) {

        links = sift(links);

        if(sucessor != null)
            return sucessor.handleRequest(links);
        else
            return links;


    }
}

