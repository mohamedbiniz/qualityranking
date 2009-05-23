package com.dialogy.odb.test.io;

import java.io.IOException;

import com.dialogy.odb.core.io.BufferedIO;
import com.dialogy.odb.core.io.DefaultFileIO;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.tool.IOUtil;

public class TestDefaultFileIO extends ODBTestCase {
	
	public void test1() throws IOException{
		byte b = 1;
		BufferedIO braf = new DefaultFileIO("testWBuffer.odb", true);

		long t0 = System.currentTimeMillis();

		for (int i = 0; i < 1024 * 100; i++) {
			braf.writeByte(b);
		}
		braf.flush();
        braf.close();
        
        BufferedIO braf2 = new DefaultFileIO("testWBuffer.odb", false);
        assertEquals(1024*100 , braf2.getLength());
		System.out.println("File size=" + braf2.getLength());
		System.out.println("Number of Flush=" + braf.getNumberOfFlush());
        braf2.close();
        assertTrue(IOUtil.deleteFile("testWBuffer.odb"));		
	}
}
