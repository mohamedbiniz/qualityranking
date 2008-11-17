package com.dialogy.odb.test.session;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.session.Context;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.arraycollectionmap.PlayerWithList;
import com.dialogy.odb.tool.IOUtil;

public class TestSession extends ODBTestCase {
	
	public void test1() throws Exception{
		Configuration.setDebugEnabled(false);
		ODB odb = ODB.open("session.odb");
		
        System.out.println("Session before closing - " + Context.existSession());
		odb.commitAndClose();
        System.out.println("Session after closing - "+ Context.existSession());
		
		ODB odb2 = ODB.open("session.odb");
		List l = odb2.getObjectsOf(PlayerWithList.class,true);
        odb2.close();
        System.out.println("Thread = " + Thread.currentThread().hashCode());
        
        
        IOUtil.deleteFile("list1.odb");
        
        System.out.println("Session after all - "+ Context.existSession());
	}

}










