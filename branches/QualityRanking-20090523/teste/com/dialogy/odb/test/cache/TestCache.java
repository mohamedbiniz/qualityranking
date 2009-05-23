/* 
 * $RCSfile: TestCache.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:53 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.cache;

import java.util.List;

import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class TestCache extends ODBTestCase {

    public static int NB_OBJECTS = 50;

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        ODB odb = ODB.open("cache.odb");
        for (int i = 0; i < NB_OBJECTS; i++) {
            odb.store(new Function("function " + (i + i)));
            odb.store(new User("olivier " + i, "olivier@dialogy.com " + i, new Profile("profile " + i, new Function("inner function " + i))));
        }
        odb.commitAndClose();
    }

    public void test1() throws Exception {
        ODB odb = ODB.open("cache.odb");

        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(Function function) {
                return function.getName().equals("function 10");
            }
        });
        assertFalse(l.isEmpty());
        // Cache must have only one object : The function
        assertEquals(1,odb.getSession().getCache().getNumberOfObjects());
        odb.close();
    }
    
    public void test2() throws Exception {
        ODB odb = ODB.open("cache.odb");

        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(User user) {
                return user.getName().equals("olivier 10");
            }
        });
        assertFalse(l.isEmpty());
        // Cache must have 3 objets, one user, 1 profile and 1 function
        assertEquals(3,odb.getSession().getCache().getNumberOfObjects());
        odb.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        IOUtil.deleteFile("cache.odb");

    }

}
