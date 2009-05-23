package com.dialogy.odb.test.io;

import java.io.IOException;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.io.FileSystemInterface;
import com.dialogy.odb.test.ODBTestCase;

public class TestFileSystemInterface2 extends ODBTestCase {

	public void testReadWrite() throws IOException {
        Configuration.setDebugEnabled(false);
		FileSystemInterface fsi = new FileSystemInterface("testReadWrite.teste", true);
		fsi.setWritePosition(fsi.getLength());

		for (int i = 0; i < 10000; i++) {
			fsi.writeInt(i,"int",false);

            long currentPosition = fsi.getPosition();
			if (i == 8000) {

				currentPosition = fsi.getPosition();
				fsi.useBuffer(false);
				fsi.setWritePosition(4);
				assertEquals(1, fsi.readInt("int"));
				fsi.useBuffer(true);
				fsi.setWritePosition(currentPosition);
			}
			if (i == 9000) {

				currentPosition = fsi.getPosition();
				fsi.useBuffer(false);
                fsi.setWritePosition(8);
                fsi.writeInt(12,"int",false);
                fsi.useBuffer(true);
                fsi.setWritePosition(currentPosition);
			}
		}
		fsi.setReadPosition(0);

		for (int i = 0; i < 10000; i++) {
			int j = fsi.readInt("int");
			if(i==2){
				assertEquals(12, j);
			}else{
				assertEquals(i, j);
			}
		}

		fsi.close();
        assertEquals(true,fsi.delete());
	}
}
