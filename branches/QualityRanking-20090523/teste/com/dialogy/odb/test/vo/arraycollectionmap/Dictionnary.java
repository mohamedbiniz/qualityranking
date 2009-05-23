/* 
 * $RCSfile: Dictionnary.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:53 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.vo.arraycollectionmap;

import java.util.HashMap;
import java.util.Map;

public class Dictionnary {

    private String name;
    private Map map;
    
    public Dictionnary(){
        this("default");
    }
    public Dictionnary(String name){
        this.name = name;
        map = new HashMap();
    }
    public void addEntry(Object key,Object value){
        map.put(key,value);
    }
    
    public String toString() {
        return name + " | " + map;
    }
    
    public Object get(Object key){
        return map.get(key);
    }
    
}
