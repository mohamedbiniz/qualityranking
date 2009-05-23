/* 
 * $RCSfile: TestUpdate.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:56 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.update;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class TestUpdate extends ODBTestCase {
	public static int NB_OBJECTS = 50;
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ODB odb = ODB.open("update.odb");
        for(int i=0;i<NB_OBJECTS;i++){
            odb.store(new Function("function " + (i+i)));
            odb.store(new User("olivier " + i,"olivier@dialogy.com "+i,new Profile("profile "+i,new Function("inner function "+i))));
        }
        odb.commitAndClose();
    }

    public void test1() throws Exception{
        ODB odb = ODB.open("update.odb");
        
        List l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(Function function){
        		return function.getName().equals("function 10");
        	}
        });
        assertFalse(l.isEmpty());
        Function f = (Function) l.get(0);

        assertEquals("function 10",f.getName());
        f.setName(f.getName() + " | " + f.getName());
        odb.store(f);
        odb.commitAndClose();
        
        odb = ODB.open("update.odb");
        
        l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(Function function){
        		return function.getName().equals("function 10");
        	}
        });
        
        assertTrue(l.isEmpty());
        
        l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(Function function){
        		return function.getName().equals("function 10 | function 10");
        	}
        });
        
        assertFalse(l.isEmpty());
        odb.close();
        
        
    }
    
    public void test2() throws Exception{
        ODB odb = ODB.open("update.odb");
        
        List l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(User user){
        		return user.getProfile().getName().equals("profile 10");
        	}
        });
        assertFalse(l.isEmpty());
        User u = (User) l.get(0);

        assertEquals("profile 10",u.getProfile().getName());
        Profile p2 =u.getProfile();
        p2.setName(p2.getName() + " | " + p2.getName());
        odb.store(p2);
        odb.commitAndClose();
        
        odb = ODB.open("update.odb");
        
        l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(User user){
        		return user.getProfile().getName().equals("profile 10");
        	}        });
        
        assertTrue(l.isEmpty());
        
        l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(User user){
        		return user.getProfile().getName().equals("profile 10 | profile 10");
        	}
        });
        
        assertFalse(l.isEmpty());
        l = odb.getObjectsOf(Profile.class,false);
        assertEquals(NB_OBJECTS,l.size());
        System.out.println("Profiles =" + l);
        odb.close();
        
        
    }

    public void test3() throws Exception{
    	
    	Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("update.odb");
        
        List l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(User user){
        		return user.getProfile().getName().equals("profile 10");
        	}
        });
        assertFalse(l.isEmpty());
        User u = (User) l.get(0);

        assertEquals("profile 10",u.getProfile().getName());
        Profile p2 =u.getProfile();
        p2.setName(p2.getName() + " | " + p2.getName());
        odb.store(u);
        odb.commitAndClose();
        
        odb = ODB.open("update.odb");
        
        l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(User user){
        		return user.getProfile().getName().equals("profile 10");
        	}        });
        
        assertTrue(l.isEmpty());
        
        l = odb.getObjects(new ISimpleNativeQuery(){
        	public boolean match(User user){
        		return user.getProfile().getName().equals("profile 10 | profile 10");
        	}
        });
        
        assertFalse(l.isEmpty());
        l = odb.getObjectsOf(Profile.class,false);
        assertEquals(NB_OBJECTS,l.size());
        System.out.println("Profiles =" + l);
        odb.close();
        
        
    }

    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        IOUtil.deleteFile("update.odb");
        
    }
    
    

}
