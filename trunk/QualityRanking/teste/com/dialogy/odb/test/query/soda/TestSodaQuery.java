/* 
 * $RCSfile: TestSodaQuery.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:56 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.query.soda;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.query.soda.Soda;
import com.dialogy.odb.core.query.soda.SodaQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.tool.IOUtil;

public class TestSodaQuery extends ODBTestCase {
    
    public void test1() throws Exception{
        Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("criteria.odb");
        
        SodaQuery aq = new SodaQuery(Function.class,Soda.equal("name","function 2")).or(Soda.equal("name","function 3"));
        
        List l = odb.getObjects(aq,true,-1,-1);
        System.out.println(l);
        assertEquals(2,l.size());
        Function f = (Function) l.get(0);
        assertEquals("function 2",f.getName());
        odb.close();
        
    }
    
    public void test2() throws Exception{
        Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("criteria.odb");
        
        SodaQuery aq = new SodaQuery(Function.class, Soda.equal("name","function 2")).not();
        
        List l = odb.getObjects(aq,true,-1,-1);
        System.out.println(l);
        assertEquals(49,l.size());
        Function f = (Function) l.get(0);
        assertEquals("function 0",f.getName());
        odb.close();
        
    }
    
    public void test3() throws Exception{
        Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("criteria.odb");
        
        SodaQuery aq = new SodaQuery(Function.class, Soda.equal("name","function 2")).or(Soda.equal("name","function 3")).not();
        
        List l = odb.getObjects(aq,true,-1,-1);
        System.out.println(l);
        assertEquals(48,l.size());
        Function f = (Function) l.get(0);
        assertEquals("function 0",f.getName());
        odb.close();
        
    }

    protected void setUp() throws Exception {
        Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("criteria.odb");
        for(int i=0;i<50;i++){
            odb.store(new Function("function "+i));
        }
        odb.commitAndClose();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        IOUtil.deleteFile("criteria.odb");
    }
    
}
