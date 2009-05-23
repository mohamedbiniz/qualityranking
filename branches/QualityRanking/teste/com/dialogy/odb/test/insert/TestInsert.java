package com.dialogy.odb.test.insert;

import java.util.ArrayList;
import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class TestInsert extends ODBTestCase {
	public void testCompositeObjects2DifferentObjects() throws Exception{
		Configuration.setDebugEnabled(true);
		ODB odb = ODB.open("t1.odb");
		
		int nbUsers = odb.getObjectsOf(User.class,true).size();
		int nbProfiles = odb.getObjectsOf(Profile.class,true).size();
		int nbFunctions = odb.getObjectsOf(Function.class,true).size();

		Function login = new Function("login");
		Function logout = new Function("logout");
        Function disconnect = new Function("disconnect");
        List list = new ArrayList();
		list.add(login);
		list.add(logout);

        List list2 = new ArrayList();
        list.add(login);
        list.add(logout);
		Profile profile1 = new Profile("operator 1",list);
        Profile profile2 = new Profile("operator 2",list2);
        User user = new User("olivier smadja","olivier@dialogy.com",profile1);
		User userB = new User("Aísa Galvão Smadja","aisa@dialogy.com",profile2);
		
		odb.store(user);
		odb.store(userB);
		odb.commit();
		
		List users = odb.getObjectsOf(User.class,true);
		List profiles = odb.getObjectsOf(Profile.class,true);
		List functions = odb.getObjectsOf(Function.class,true);
		
		//assertEquals(nbUsers+2,users.size());
		User user2 = (User) users.get(0);
		
		assertEquals(user.toString(),user2.toString());
		assertEquals(nbProfiles + 2 , profiles.size());
		assertEquals(nbFunctions + 2, functions.size());
		odb.close();
		assertTrue(IOUtil.deleteFile("t1.odb"));

	}


    public void testCompositeObjects2() throws Exception{
        Configuration.setDebugEnabled(true);
        ODB odb = ODB.open("t3.odb");

        int nbUsers = odb.getObjectsOf(User.class,true).size();
        int nbProfiles = odb.getObjectsOf(Profile.class,true).size();
        int nbFunctions = odb.getObjectsOf(Function.class,true).size();

        Function login = new Function("login");
        Function logout = new Function("logout");

        List list = new ArrayList();
        list.add(login);
        list.add(logout);

        Profile profile1 = new Profile("operator 1",list);
        Profile profile2 = new Profile("operator 2",list);
        User user = new User("olivier smadja","olivier@dialogy.com",profile1);
        User userB = new User("Aísa Galvão Smadja","aisa@dialogy.com",profile2);

        odb.store(user);
        odb.store(userB);
        odb.commitAndClose();

        odb = ODB.open("t3.odb");

        List users = odb.getObjectsOf(User.class,true);
        List profiles = odb.getObjectsOf(Profile.class,true);
        List functions = odb.getObjectsOf(Function.class,true);

        //assertEquals(nbUsers+2,users.size());
        User user2 = (User) users.get(0);

        assertEquals(user.toString(),user2.toString());
        assertEquals(nbProfiles +2 , profiles.size());
        assertEquals(nbFunctions + 2, functions.size());
        odb.close();
        assertTrue(IOUtil.deleteFile("t3.odb"));

    }

    public void testCompositeObjects3() throws Exception{
        Configuration.setDebugEnabled(true);
        ODB odb = ODB.open("t4.odb");

        int nbUsers = odb.getObjectsOf(User.class,true).size();
        int nbProfiles = odb.getObjectsOf(Profile.class,true).size();
        int nbFunctions = odb.getObjectsOf(Function.class,true).size();

        Function login = new Function("login");
        Function logout = new Function("logout");

        List list = new ArrayList();
        list.add(login);
        list.add(logout);

        Profile profile1 = new Profile("operator 1",list);
        User user = new User("olivier smadja","olivier@dialogy.com",profile1);
        User userB = new User("Aísa Galvão Smadja","aisa@dialogy.com",profile1);

        odb.store(user);
        odb.store(userB);
        odb.commitAndClose();

        odb = ODB.open("t4.odb");

        List users = odb.getObjectsOf(User.class,true);
        List profiles = odb.getObjectsOf(Profile.class,true);
        List functions = odb.getObjectsOf(Function.class,true);

        //assertEquals(nbUsers+2,users.size());
        User user2 = (User) users.get(0);

        assertEquals(user.toString(),user2.toString());
        assertEquals(nbProfiles +1 , profiles.size());
        assertEquals(nbFunctions + 2, functions.size());
        odb.close();
        assertTrue(IOUtil.deleteFile("t4.odb"));

    }

    public void testCompositeObjects4() throws Exception{
        Configuration.setDebugEnabled(true);
        ODB odb = ODB.open("t5.odb");

        int nbUsers = odb.getObjectsOf(User.class,true).size();
        int nbProfiles = odb.getObjectsOf(Profile.class,true).size();
        int nbFunctions = odb.getObjectsOf(Function.class,true).size();

        Function login = new Function("login");
        Function logout = new Function("logout");

        List list = new ArrayList();
        list.add(login);
        list.add(logout);

        Profile profile1 = new Profile("operator 1",list);
        User user = new User("olivier smadja","olivier@dialogy.com",profile1);
        User userB = new User("Aísa Galvão Smadja","aisa@dialogy.com",profile1);

        odb.store(user);
        odb.store(userB);
        odb.commit();


        List users = odb.getObjectsOf(User.class,true);
        List profiles = odb.getObjectsOf(Profile.class,true);
        List functions = odb.getObjectsOf(Function.class,true);

        //assertEquals(nbUsers+2,users.size());
        User user2 = (User) users.get(0);

        assertEquals(user.toString(),user2.toString());
        assertEquals(nbProfiles +1 , profiles.size());
        assertEquals(nbFunctions + 2, functions.size());
        odb.close();
        assertTrue(IOUtil.deleteFile("t5.odb"));

    }

    public void testSimple() throws Exception{
        Configuration.setDebugEnabled(true);
        ODB odb = ODB.open("t2.odb");

        int nbFunctions = odb.getObjectsOf(Function.class,true).size();

        Function login = new Function("login");
        Function logout = new Function("logout");

        odb.store(login);
        odb.store(logout);
        odb.commitAndClose();

        odb = ODB.open("t2.odb");
        List functions = odb.getObjectsOf(Function.class,true);
        Function f1 = (Function) functions.get(0);
        f1.setName("login1");

        odb.store(f1);

        odb.commitAndClose();

        odb = ODB.open("t2.odb");

        functions = odb.getObjectsOf(Function.class,true);

        assertEquals(2,functions.size());
        assertEquals("login1",  ((Function)functions.get(0)).getName());
        odb.close();
        assertTrue(IOUtil.deleteFile("t2.odb"));
    }
}
