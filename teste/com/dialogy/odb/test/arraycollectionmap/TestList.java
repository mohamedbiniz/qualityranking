package com.dialogy.odb.test.arraycollectionmap;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.session.Context;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.arraycollectionmap.PlayerWithList;
import com.dialogy.odb.tool.IOUtil;

public class TestList extends ODBTestCase {
	
	public void testList1() throws Exception{
		Configuration.setDebugEnabled(false);
		ODB odb = ODB.open("list1.odb");
		
		long nb = odb.getNumberOfObjectsOf(PlayerWithList.class);
		PlayerWithList player = new PlayerWithList("kiko");
		player.addGame("volley-ball");
		player.addGame("squash");
		player.addGame("tennis");
		player.addGame("ping-pong");
		
		odb.store(player);
        System.out.println("Session before closing - " + Context.existSession());
		odb.commitAndClose();
        System.out.println("Session after closing - "+ Context.existSession());
		
		ODB odb2 = ODB.open("list1.odb");
		List l = odb2.getObjectsOf(PlayerWithList.class,true);
		
		assertEquals(nb+1,l.size());
		
		// gets last player
		PlayerWithList player2 = (PlayerWithList) l.get(l.size()-1);
		assertEquals(player.toString(),player2.toString());
        odb2.close();
        IOUtil.deleteFile("list1.odb");
        System.out.println("Thread = " + Thread.currentThread().hashCode());
        
        
        System.out.println("Session after all - "+ Context.existSession());
        
	}
    
}










