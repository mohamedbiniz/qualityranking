/* 
 * $RCSfile: TestMap.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:55 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.arraycollectionmap;

import java.util.List;

import com.dialogy.odb.core.query.soda.Soda;
import com.dialogy.odb.core.query.soda.SodaQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.arraycollectionmap.Dictionnary;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.tool.IOUtil;

public class TestMap extends ODBTestCase {

    protected void setUp() throws Exception {
        ODB odb = ODB.open("map.odb");
        
        Dictionnary dictionnary1 = new Dictionnary("test1");
        dictionnary1.addEntry("olivier","Smadja");
        dictionnary1.addEntry("kiko","vidal");
        dictionnary1.addEntry("karine","galvao");
        
        Dictionnary dictionnary2 = new Dictionnary("test2");
        dictionnary2.addEntry("f1",new Function("function1"));
        dictionnary2.addEntry("f2",new Function("function2"));
        dictionnary2.addEntry("f3",new Function("function3"));
        
        odb.store(dictionnary1);
        odb.store(dictionnary2);
        odb.store(new Function("login"));
        odb.commitAndClose();
    }

    
    public void test1() throws Exception{
        ODB odb = ODB.open("map.odb");
        
        List l = odb.getObjectsOf(Dictionnary.class,true);
        System.out.println(l);
        assertEquals(2,l.size());
        Dictionnary dictionnary = (Dictionnary) l.get(0);
        
        assertEquals("Smadja",dictionnary.get("olivier"));
        odb.close();
        
    }
    
    public void test2() throws Exception{
        ODB odb = ODB.open("map.odb");
        
        SodaQuery aq = new SodaQuery(Dictionnary.class,Soda.equal("name","test2"));
        List l = odb.getObjects(aq);
        System.out.println(l);
        assertEquals(1,l.size());
        Dictionnary dictionnary = (Dictionnary) l.get(0);
        
        assertEquals(new Function("function2").getName(),((Function)dictionnary.get("f2")).getName());
        odb.close();
        
    }

    protected void tearDown() throws Exception {
        IOUtil.deleteFile("map.odb");
    }
    
    

}

