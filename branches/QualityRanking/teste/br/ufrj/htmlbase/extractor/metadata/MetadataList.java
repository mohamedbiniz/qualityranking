package br.ufrj.htmlbase.extractor.metadata;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: mayworm
 * Date: Feb 10, 2006
 * Time: 3:48:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MetadataList {

    private Collection<MetadataCrawler> metadatas = new ArrayList<MetadataCrawler>();

    public boolean add(MetadataCrawler m){
        return metadatas.add(m);

    }

    public boolean addList(Collection<MetadataCrawler> c){
        return metadatas.addAll(c);
    }

    public Collection<MetadataCrawler> getMetadatas(){
        return this.metadatas;

    }

    public long getLong(String metadataName, long defaultValue){

        String valueString = getMetadataValue(metadataName);

        if (valueString == null)
            return defaultValue;
        try {
            return Long.parseLong(valueString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }

    }


    public float getFloat(String metadataName, float defaultValue){

        String valueString = getMetadataValue(metadataName);

        if (valueString == null)
            return defaultValue;
        try {
            return Float.parseFloat(valueString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }

    }



    public String getString(String metadataName){

        return getMetadataValue(metadataName);

    }

    public String getString(String metadataName, String defaultValue){

        String valueString = getMetadataValue(metadataName);

        if (valueString == null)
            return defaultValue;

        return valueString;


    }


    public int getInt(String metadataName, int defaultValue){

        String valueString = getMetadataValue(metadataName);

        if (valueString == null)
            return defaultValue;
        try {
            return Integer.parseInt(valueString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }

    }


    public boolean getBoolean(String metadataName, boolean defaultValue){

        String valueString = getMetadataValue(metadataName);

        if (valueString == null)
            return defaultValue;
        try {
            return Boolean.parseBoolean(valueString);
        } catch (NumberFormatException e) {
            return defaultValue;
        }

    }


    private String getMetadataValue(String metadataName) {
        String valueString = null;
        for(Object m: metadatas){
            MetadataCrawler meta = (MetadataCrawler)m;
            String property = meta.getName();

            if (property.equalsIgnoreCase(metadataName)){
                valueString = meta.getValue();
                break;
            }

        }
        return valueString;
    }


}
