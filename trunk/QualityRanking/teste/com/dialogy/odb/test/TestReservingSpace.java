package com.dialogy.odb.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.io.FileSystemInterface;
import com.dialogy.odb.core.io.IOFileParameter;
import com.dialogy.odb.core.session.Context;
import com.dialogy.odb.core.session.Session;
import com.dialogy.odb.tool.IOUtil;

public class TestReservingSpace extends ODBTestCase {
	
	
    
    public void testSize() throws IOException{
		Configuration.setDebugEnabled(false);
        Session session = Context.startNewSession();
        FileSystemInterface writingFsi = new FileSystemInterface(session,new IOFileParameter("writing.odb",true));
		FileSystemInterface reservingFsi = new FileSystemInterface(session,new IOFileParameter("reserving.odb",true));
		
		write(writingFsi,false);
		write(reservingFsi,true);

		System.out.println("writing :"+writingFsi.getLength() + " | reserving :" + reservingFsi.getLength());
		assertEquals(writingFsi.getLength(),reservingFsi.getLength());
		writingFsi.close();
		reservingFsi.close();
		assertTrue(IOUtil.deleteFile("writing.odb"));
		assertTrue(IOUtil.deleteFile("reserving.odb"));
        Context.endSession(session);
        
		
	}
	
	public void write(FileSystemInterface fsi,boolean writeInTransaction) throws IOException{
		fsi.writeInt(1,"1", writeInTransaction);
		fsi.writeLong(2,"2", writeInTransaction);
		fsi.writeString("333333","333333", writeInTransaction);
		fsi.writeBigDecimal(new BigDecimal("44444.4444"),"44444.4444", writeInTransaction);
		fsi.writeDate(new Date(),"date", writeInTransaction);
	}
}
