package br.ufrj.htmlbase.filter;

import java.util.ArrayList;
import java.util.Collection;

import br.ufrj.htmlbase.OutputLinkCrawler;

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


  Collection<OutputLinkCrawler> sift( Collection<OutputLinkCrawler> links ){

    Collection<OutputLinkCrawler> result = new ArrayList<OutputLinkCrawler>();
      for (OutputLinkCrawler link : links) {
          if (isAcceptable(link)) {
              result.add(link);
          }
      }
    return result;
  }

    public Collection<OutputLinkCrawler> handleRequest(Collection<OutputLinkCrawler> links) {

        links = sift(links);

        if(sucessor != null)
            return sucessor.handleRequest(links);
        else
            return links;


    }
}

