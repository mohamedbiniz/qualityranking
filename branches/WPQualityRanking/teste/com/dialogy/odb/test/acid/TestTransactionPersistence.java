package com.dialogy.odb.test.acid;

import java.io.IOException;
import java.math.BigDecimal;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.core.acid.Transaction;
import com.dialogy.odb.core.acid.WriteAction;
import com.dialogy.odb.core.io.ByteArrayConverter;
import com.dialogy.odb.core.io.FileSystemInterface;
import com.dialogy.odb.core.io.IOFileParameter;
import com.dialogy.odb.core.meta.ODBType;
import com.dialogy.odb.core.session.Context;
import com.dialogy.odb.core.session.Session;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.tool.DisplayUtility;
import com.dialogy.odb.tool.IOUtil;

public class TestTransactionPersistence extends ODBTestCase {

	public void test4() throws IOException, ClassNotFoundException {

		Configuration.setDebugEnabled(false);
		WriteAction wa1 = new WriteAction(1, ByteArrayConverter.intToByteArray(1), ODBType.NATIVE_INT_ID, "size");
		assertEquals(wa1.getBytes().length, 4);

		WriteAction wa2 = new WriteAction(1, ByteArrayConverter.stringToByteArray("ola chico", true), ODBType.STRING_ID, "size");

		WriteAction wa3 = new WriteAction(1, ByteArrayConverter.bigDecimalToByteArray(new BigDecimal("1.123456789"), true), ODBType.BIG_DECIMAL_ID, "size");
	}

	public void test2B() throws IOException, ClassNotFoundException {

		Configuration.setDebugEnabled(false);
        Session session = Context.startNewSession();
		WriteAction wa1 = new WriteAction(1, ByteArrayConverter.intToByteArray(1), ODBType.NATIVE_INT_ID, "size");
		WriteAction wa2 = new WriteAction(5, ByteArrayConverter.stringToByteArray(" 1 - ola chico! - 1", true), ODBType.STRING_ID, "name");

		WriteAction wa3 = new WriteAction(1, ByteArrayConverter.intToByteArray(2), ODBType.NATIVE_INT_ID, "size");
		WriteAction wa4 = new WriteAction(5, ByteArrayConverter.stringToByteArray(" 2 - ola chico! - 2", true), ODBType.STRING_ID, "name");

		FileSystemInterface fsi = new FileSystemInterface(session,new IOFileParameter("test.odb",true));
		Transaction transaction = new Transaction(session,fsi);
        transaction.setArchiveLog(true);
        
		transaction.addWriteAction(wa1);
		transaction.addWriteAction(wa2);
		transaction.addWriteAction(wa3);
		transaction.addWriteAction(wa4);


        transaction.commit();
		Transaction transaction2 = Transaction.read(transaction.getName());

		System.out.println("t1 = " + transaction.toString());
		System.out.println("t2 = " + transaction2.toString());

		WriteAction wat1 = (WriteAction) transaction.getWriteActions().get(transaction.getWriteActions().size() - 1);
		WriteAction wat2 = (WriteAction) transaction2.getWriteActions().get(transaction2.getWriteActions().size() - 1);
		assertEquals(DisplayUtility.byteArrayToString(wat1.getBytes()), DisplayUtility.byteArrayToString(wat2.getBytes()));
		assertEquals(wat1.getPosition(), wat2.getPosition());

		transaction2.commit();
		fsi.close();
		IOUtil.deleteFile("test.odb");
		IOUtil.deleteFile(transaction.getName());
        Context.endSession(session);
    }

	public void test3() throws IOException, ClassNotFoundException {
		Configuration.setDebugEnabled(false);
        Session session = Context.startNewSession();
        FileSystemInterface fsi = new FileSystemInterface(session,new IOFileParameter("test2.odb",true));
        Transaction transaction = new Transaction(session,fsi);
        transaction.setArchiveLog(true);
		for (int i = 0; i < 1000; i++) {
			transaction.addWriteAction(new WriteAction(i, ByteArrayConverter.intToByteArray(i), ODBType.NATIVE_INT_ID, "attr" + i));
		}

		transaction.commit();

		long start = System.currentTimeMillis();
		Transaction transaction2 = Transaction.read(transaction.getName());
		long t = System.currentTimeMillis() - start;
		// System.out.println("t1 = " + transaction.toString());
		// System.out.println("t2 = " + transaction2.toString());

		WriteAction wa1 = (WriteAction) transaction.getWriteActions().get(99);
		WriteAction wa2 = (WriteAction) transaction2.getWriteActions().get(99);
        assertEquals(DisplayUtility.byteArrayToString(wa1.getBytes()), DisplayUtility.byteArrayToString(wa2.getBytes()));
		assertEquals(wa1.getPosition(), wa2.getPosition());

		transaction2.commit();
		System.out.println("t=" + t);
		fsi.close();
		IOUtil.deleteFile("test2.odb");
		IOUtil.deleteFile(transaction.getName());
        Context.endSession(session);
    }
}
