package com.dialogy.odb.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.meta.ClassInfo;
import com.dialogy.odb.core.reflection.ClassIntrospector;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.vo.attribute.TestClass;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.tool.IOUtil;

public class StorageEngineTest extends ODBTestCase {

	public void testNonNativeAttributes() throws Exception {
		TestClass tc = new TestClass();
		ClassInfo classInfo = ClassIntrospector.getClassInfo(tc);

		List l = classInfo.getAllNonNativeAttributes();
		System.out.println(l);
		assertEquals(0, l.size());
	}

	public void testSimpleInstance() throws Exception {
		Configuration.setDebugEnabled(true);
		
		
		ODB odb = ODB.open("t-simple-instance.odb");

		TestClass tc1 = new TestClass();
		tc1.setBigDecimal1(new BigDecimal("1.123456"));
		tc1.setBoolean1(true);
		tc1.setChar1('d');
		tc1.setDouble1(new Double(154.78998989));
		tc1.setInt1(78964);
		tc1.setString1("Ola chico como vc está ???");
		tc1.setDate1(new Date());

		TestClass tc2 = new TestClass();
		tc2.setBigDecimal1(new BigDecimal("1.1234565454"));
		tc2.setBoolean1(false);
		tc2.setChar1('c');
		tc2.setDouble1(new Double(78454.8779));
		tc2.setInt1(1254);
		tc2.setString1("Ola chico como ca va ???");
		tc2.setDate1(new Date());

		odb.store(tc1);
		odb.store(tc2);
		
		odb.commit();
        odb.close();
        
        odb = ODB.open("t-simple-instance.odb");

		List l = odb.getObjectsOf(TestClass.class,true);
		TestClass tc12 = (TestClass) l.get(0);
		System.out.println("#### " + l.size() + " : " + l);
		assertEquals(tc1.getBigDecimal1(), tc12.getBigDecimal1());
		assertEquals(tc1.getString1(), tc12.getString1());
		assertEquals(tc1.getChar1(), tc12.getChar1());
		assertEquals(tc1.getDouble1(), tc12.getDouble1());
		assertEquals(tc1.getInt1(), tc12.getInt1());
		assertEquals(tc1.isBoolean1(), tc12.isBoolean1());
		if(l.size()<3){
			assertEquals(tc1.getDate1(), tc12.getDate1());
		}

		TestClass tc22 = (TestClass) l.get(1);
		assertEquals(tc2.getBigDecimal1(), tc22.getBigDecimal1());
		assertEquals(tc2.getString1(), tc22.getString1());
		assertEquals(tc2.getChar1(), tc22.getChar1());
		assertEquals(tc2.getDouble1(), tc22.getDouble1());
		assertEquals(tc2.getInt1(), tc22.getInt1());
		assertEquals(tc2.isBoolean1(), tc22.isBoolean1());
		if(l.size()<3){
			assertEquals(tc2.getDate1(), tc22.getDate1());
		}
		
		odb.close();
		assertTrue(IOUtil.deleteFile("t-simple-instance.odb"));
	}

	public void testComplexInstance() throws Exception{
        Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("t-complex-instance.odb");

        Function login = new Function("login");
        Function logout= new Function("logout");
        List functions = new ArrayList();
        functions.add(login);
        functions.add(logout);
        Profile profile = new Profile("profile1",functions);
        User user = new User("oliver","olivier@dialogy.com",profile);
        

        odb.store(user);
        odb.commit();
        odb.close();
        
        odb = ODB.open("t-complex-instance.odb");

        List l = odb.getObjectsOf(User.class,true);
        
        User user2 = (User) l.get(0);
        System.out.println("#### " + l.size() + " : " + l);
        assertEquals(user.getName(), user2.getName());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getProfile().getName(), user2.getProfile().getName());
        assertEquals(user.getProfile().getFunctions().get(0).toString(), user2.getProfile().getFunctions().get(0).toString());
        
        odb.close();
        assertTrue(IOUtil.deleteFile("t-complex-instance.odb"));
        
    }
}
