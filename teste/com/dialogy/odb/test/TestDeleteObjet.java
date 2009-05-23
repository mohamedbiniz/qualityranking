package com.dialogy.odb.test;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.tool.IOUtil;

public class TestDeleteObjet extends ODBTestCase {

	public void test1() throws Exception{
		int n = 1000;
		Configuration.setDebugEnabled(false);
		ODB odb = ODB.open("t-delete.odb");
        System.out.println("Number of Function(s) = " + odb.getNumberOfObjectsOf(Function.class));
		
		for(int i=0;i<n;i++){
			Function login = new Function("login - " +(i+1));
			odb.store(login);
			assertEquals(i+1,odb.getNumberOfObjectsOf(Function.class));
		}
		odb.commit();

		List l = odb.getObjectsOf(Function.class,true);
		
		for(int i=0;i<n;i++){
			Function f = (Function) l.get(i);
			odb.delete(f);
			assertEquals(1000-(i+1),odb.getNumberOfObjectsOf(Function.class));
		}
		odb.commit();
		odb.close();
		assertTrue(IOUtil.deleteFile("t-delete.odb"));
		
        
	}
}
