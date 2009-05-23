/* 
 * $RCSfile: TestNQQuery.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:56 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.query.nq;

import java.io.File;
import java.util.List;

import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class TestNQQuery extends ODBTestCase {
    public static int NB_OBJECTS = 10;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
    	System.out.println("TestNQQuery.setUp");
    	ODB odb = ODB.open("get.odb");
        for (int i = 0; i < NB_OBJECTS; i++) {
            odb.store(new Function("function " + i));
            odb.store(new User("olivier " + i, "olivier@dialogy.com " + i, new Profile("profile " + i, new Function("inner function " + i))));
        }
        odb.commitAndClose();
        System.out.println("NbFunctions " + odb.getNumberOfObjectsOf(Function.class));
        System.out.println("NbUsers " + odb.getNumberOfObjectsOf(User.class));
        
    	System.out.println(System.currentTimeMillis());
        //Thread.sleep(5000);
    }

    public void test1() throws Exception {
        
    	System.out.println("****TestNQQuery.test1.existFile : " + new File("get.odb").exists());
    	System.out.println("****TestNQQuery.test1.length : " + new File("get.odb").length());

    	ODB odb = ODB.open("get.odb");
    	odb.getObjectsOf(Function.class,true);
    	odb.close();
    	
    	odb = ODB.open("get.odb");
        System.out.println("TestNQQuery.test1:"+odb.getObjectsOf(Function.class,true));
        
        
        
        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(Function function) {
                return true;
            }
        });
        odb.close();
        assertFalse(l.isEmpty());
        assertEquals(NB_OBJECTS*2, l.size());
    }
    
    public void test2() throws Exception {
    	
    	    	
        System.out.println("****TestNQQuery.test2.existFile : " + new File("get.odb").exists());
        System.out.println("****TestNQQuery.test2.length : " + new File("get.odb").length());
    	ODB odb = ODB.open("get.odb");

        System.out.println("++++TestNQQuery.test2:"+odb.getObjectsOf(Function.class,true));
        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(Function function) {
            	return function.getName().equals("function 5");
            }
        });
        odb.close();
        assertFalse(l.isEmpty());
        assertEquals(1, l.size());
    }

    public void test3() throws Exception {
    	
    	ODB odb = ODB.open("get.odb");

        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(User user) {
                return user.getProfile().getName().equals("profile 5");
            }
        });
        odb.close();
        assertFalse(l.isEmpty());
        assertEquals(1, l.size());
    }

    public void test4() throws Exception {
    	
        ODB odb = ODB.open("get.odb");

        ISimpleNativeQuery query = new ISimpleNativeQuery() {
            public boolean match(User user) {
                return user.getProfile().getName().startsWith("profile");
            }
        }; 
        
        List l = odb.getObjects(query,true,0,5);
        odb.close();
        assertFalse(l.isEmpty());
        assertEquals(6, l.size());
    }

    public void test5() throws Exception {
    	
        ODB odb = ODB.open("get.odb");

        ISimpleNativeQuery query = new ISimpleNativeQuery() {
            public boolean match(User user) {
                return user.getProfile().getName().startsWith("profile");
            }
        }; 
        
        List l = odb.getObjects(query,true,5,6);
        assertEquals(6,odb.getSession().getCache().getNumberOfObjects());
        odb.close();
        System.out.println(l);
        assertFalse(l.isEmpty());
        assertEquals(2, l.size());
        
    }
    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        IOUtil.deleteFile("get.odb");
        System.out.println("TestNQQuery.tearDown");

    }

}
