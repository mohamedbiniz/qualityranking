package com.dialogy.odb.test.vo.crawler;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Jan 22, 2006
 * Time: 3:49:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Page {

    //lista de urls existentes na pagina
    protected List<OutputLink> outputLinks;

    //Lista de metadados que foram obtidos, a partir do Dublin core
    protected List<Metadata> metadata;

    //conteudo da pagina sem links e figuras
    protected String content;

    //identificador unico gerado com o MD5 do conteudo
    protected long id;

    //Url da pagina
    protected String url;

    //data do primeiro download
    protected int firstFetch;

    //data do proximo fecth
    protected int nextFetch;

    //pontuacao da pagina a partir de um algotimo de rank
    protected float score;

    //caminho para o arquivo fisicamente armazenado
    protected String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<OutputLink> getOutputLinks() {
        return outputLinks;
    }

    public void setOutputLinks(List<OutputLink> outputLinks) {
        this.outputLinks = outputLinks;
    }

    public List<Metadata> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<Metadata> metadata) {
        this.metadata = metadata;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFirstFetch() {
        return firstFetch;
    }

    public void setFirstFetch(int firstFetch) {
        this.firstFetch = firstFetch;
    }

    public int getNextFetch() {
        return nextFetch;
    }

    public void setNextFetch(int nextFetch) {
        this.nextFetch = nextFetch;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }



}
