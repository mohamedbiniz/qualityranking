package br.ufrj.htmlbase.filter;

import br.ufrj.htmlbase.OutputLinkCrawler;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 8, 2006
 * Time: 11:20:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ValidFilterImage extends ValidFilters{
    private String[] listImages = {".gif", ".png", ".bmp", ".jpeg", ".jpg"};

    /**
      * Filtra url's que contenham qualquer umas das extensoes acima
    */
    protected boolean isAcceptable(OutputLinkCrawler link) {

        boolean allOk = true;

        for(String img : listImages)
            if (link.getUrl().contains(img))
                allOk = false;

        return allOk;  //To change body of implemented methods use File | Settings | File Templates.
    }



}
