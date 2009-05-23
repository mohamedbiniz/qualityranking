/* 
 * $RCSfile: TestNativeQuery.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:54 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test;

import java.util.List;

import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class TestNativeQuery extends ODBTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ODB odb = ODB.open("native-query.odb");
        User user = new User("olivier","olivier@dialogy.com",new Profile("profile 1",new Function("function 1")));
        odb.store(user);
        odb.commit();
        odb.close();
    }

    public void test1() throws Exception{
        ODB odb = ODB.open("native-query.odb");
        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(User user){
                return user.getName().startsWith("oli");
            }
        });
        odb.close();
        assertEquals(1,l.size());
    }

    public void test2() throws Exception{
        ODB odb = ODB.open("native-query.odb");
        List l = odb.getObjects(new ISimpleNativeQuery() {
            boolean match(User user){
                return user.getEmail().endsWith(".com");
            }
        });
        odb.close();
        assertEquals(1,l.size());
    }
    
    public void test3() throws Exception{
        ODB odb = ODB.open("native-query.odb");
        List l = odb.getObjects(new ISimpleNativeQuery() {
            boolean match(User user){
                return user.getProfile().getName().startsWith("pro");
            }
        });
        odb.close();
        assertEquals(1,l.size());
    }

    public void test4() throws Exception{
        ODB odb = ODB.open("native-query.odb");
        List l = odb.getObjects(new ISimpleNativeQuery() {
            boolean match(User user){
                return user.getProfile().getFunctions().get(0).toString().equals("function 1");
            }
        });
        odb.close();
        assertEquals(1,l.size());
    }

    public void test5() throws Exception{
        ODB odb = ODB.open("native-query.odb");
        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(User user){
                return user.getProfile().getFunctions().get(0).toString().equals("function 2");
            }
        });
        odb.close();
        assertEquals(0,l.size());
    }

    public void test6() throws Exception{
        ODB odb = ODB.open("native-query.odb");
        List l = odb.getObjects(new ISimpleNativeQuery() {
            public boolean match(User user){
                return user.getProfile().getName().startsWith("PRO");
            }
        });
        odb.close();
        assertEquals(0,l.size());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        IOUtil.deleteFile("native-query.odb");
    }

}
