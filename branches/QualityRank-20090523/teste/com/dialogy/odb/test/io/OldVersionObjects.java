package com.dialogy.odb.test.io;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.tool.IOUtil;

public class OldVersionObjects extends ODBTestCase {
	
	public void setUp() throws Exception {
        Configuration.setDebugEnabled(true);
        System.out.println("Starting OldVersionObjects");
        ODB odb = ODB.open("old-version-object.odb");
        Function function = new Function("function 1");
        odb.store(function);
        odb.commitAndClose();
    }
	
	public void test1() throws Exception{
		ODB odb = ODB.open("old-version-object.odb");
		Configuration.setDebugEnabled(false);
		Function function = (Function) odb.getObjectsOf(Function.class,true).get(0);
		
		function.setName("f2");
		odb.store(function);
		
		Function oldFunction = (Function) odb.getPreviousVersionObject(function);
		
		assertEquals(oldFunction.getName(),"function 1");
        odb.commitAndClose();
		
	}

	protected void tearDown() throws Exception {
		IOUtil.deleteFile("old-version-object.odb");
        System.out.println("Starting OldVersionObjects");
        super.tearDown();
	}
	
	


}
