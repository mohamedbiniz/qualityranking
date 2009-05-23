package br.ufrj.htmlbase.extractor.metadata;

import br.ufrj.htmlbase.PageCrawler;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 1, 2006
 * Time: 11:28:17 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MetadataBuilder {

    protected MetadataBuilder sucessor;

    public void setSucessor(MetadataBuilder m){
        this.sucessor = m;
    }

    abstract public void makeMetadata(PageCrawler page);
}
