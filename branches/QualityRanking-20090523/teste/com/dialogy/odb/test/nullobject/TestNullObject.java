/* 
 * $RCSfile: TestNullObject.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:55 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.nullobject;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class TestNullObject extends ODBTestCase {
    
    public void test1() throws Exception{
        Configuration.setDebugEnabled(true);
        ODB odb = ODB.open("null.odb");
        
        List l = odb.getObjectsOf(User.class,true);
        System.out.println(l);

        assertEquals(2,l.size());
        
        User user1 = (User) l.get(0);
        assertEquals("oli",user1.getName());
        assertEquals("oli@sdsadf",user1.getEmail());
        
        assertEquals(null,user1.getProfile());

        odb.close();
        
    }
    
    

    protected void setUp() throws Exception {
        Configuration.setDebugEnabled(true);
        ODB odb = ODB.open("null.odb");
        
        User user1 = new User("oli","oli@sdsadf",null); 
        User user2 = new User("karine","karine@sdsadf",null);
        
        odb.store(user1);
        odb.store(user2);
        odb.commitAndClose();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        IOUtil.deleteFile("null.odb");
    }
    
}
