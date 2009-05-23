package com.dialogy.odb.test.vo.crawler;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Jan 22, 2006
 * Time: 3:53:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Metadata {
    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String property;

    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
}
