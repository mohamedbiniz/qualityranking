package com.dialogy.odb.test.update;

import java.util.ArrayList;
import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class TestSimpleUpdateObject extends ODBTestCase {
    
	/* (non-Javadoc)
     * @see com.dialogy.odb.test.ODBTestCase#tearDown()
     */
    protected void tearDown() throws Exception {
    }

    public void test1() throws Exception{
		Configuration.setDebugEnabled(false);

        
        ODB odb = ODB.open("t1u.odb");
        System.out.println(odb.getObjectsOf(Function.class,true));
		
		Function login = new Function("login");
		Function logout = new Function("logout");
		odb.store(login);
        odb.store(login);
		odb.store(logout);
        
		odb.commit();
		
		List l = odb.getObjectsOf(Function.class,true);
		Function f2 = (Function) l.get(0);
		f2.setName("login function");
		odb.store(login);
		odb.commit();	
		odb.close();
		Configuration.setDebugEnabled(false);
		ODB odb2 = ODB.open("t1u.odb");
		Function f = (Function) odb2.getObjectsOf(Function.class,true).get(0);
		assertEquals(f.getName(),"login function");
		odb2.close();
		assertTrue(IOUtil.deleteFile("t1u.odb"));

	}
	
	public void test2() throws Exception{
		Configuration.setDebugEnabled(false);
		ODB odb = ODB.open("t2.odb");
		
		int nbUsers = odb.getObjectsOf(User.class,true).size();
		int nbProfiles = odb.getObjectsOf(Profile.class,true).size();
		int nbFunctions = odb.getObjectsOf(Function.class,true).size();

		Function login = new Function("login");
		Function logout = new Function("logout");
		List list = new ArrayList();
		list.add(login);
		list.add(logout);
		Profile profile = new Profile("operator",list);
		User olivier = new User("olivier smadja","olivier@dialogy.com",profile);
		User aisa = new User("Aísa Galvão Smadja","aisa@dialogy.com",profile);
		
		odb.store(olivier);
		odb.store(aisa);
		odb.commit();
		
		List users = odb.getObjectsOf(User.class,true);
		List profiles = odb.getObjectsOf(Profile.class,true);
		List functions = odb.getObjectsOf(Function.class,true);
		odb.close();		
		System.out.println("Users:"+users);
		System.out.println("Profiles:"+profiles);
		System.out.println("Functions:"+functions);
		
		
		assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.get(users.size()-2);
		
		assertEquals(olivier.toString(),user2.toString());
		assertEquals(nbProfiles + 1 , profiles.size());
		assertEquals(nbFunctions + 2, functions.size());
		
		ODB odb2 = ODB.open("t2.odb");
		List l = odb2.getObjectsOf(Function.class,true);
		Function function = (Function) l.get(0);
		function.setName("login function");
		odb2.store(function);
		odb2.commit();
		odb2.close();
		
		ODB odb3 = ODB.open("t2.odb");
		List l2 = odb3.getObjectsOf(User.class,true);

		for(int i=0;i<Math.min(2,l2.size());i++){
			User user = (User) l2.get(i);
			System.out.println(user.getProfile().getFunctions());
			assertEquals("login function",""+user.getProfile().getFunctions().get(0));
		}
		odb3.close();
		assertTrue(IOUtil.deleteFile("t2.odb"));
		
		
	}
	
	public static void main(String[] args) throws Exception {
		TestSimpleUpdateObject tuo = new TestSimpleUpdateObject();
		//tuo.test1();
		//tuo.tes2t2();
	}
}
