package com.dialogy.odb.test.arraycollectionmap;

import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.arraycollectionmap.PlayerWithArray;
import com.dialogy.odb.tool.IOUtil;

public class TestArray extends ODBTestCase {
	
	public void testArray1() throws Exception{
		Configuration.setDebugEnabled(false);
		ODB odb = null;
        
        
        try{
            odb = ODB.open("array1.odb");
            long nb = odb.getNumberOfObjectsOf(PlayerWithArray.class);
            PlayerWithArray player = new PlayerWithArray("kiko");
            player.addGame("volley-ball");
            player.addGame("squash");
            player.addGame("tennis");
            player.addGame("ping-pong");
            
            odb.store(player);
            odb.commit();
            odb.close();
            
            odb = ODB.open("array1.odb");
            List l = odb.getObjectsOf(PlayerWithArray.class,true);
            
            assertEquals(nb+1,l.size());
            System.out.println(l);
            // gets last player
            PlayerWithArray player2 = (PlayerWithArray) l.get(l.size()-1);
            assertEquals(player.toString(),player2.toString());
            odb.close();
            IOUtil.deleteFile("array1.odb");
        }catch(Exception e){
        	if(odb!=null){
        		odb.rollback();
        	}
            throw e;
        }
        finally{
            if(odb!=null){
                odb.close();
            }
        }
		
	}

}
