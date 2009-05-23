/*
 * Created on 10/10/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.dialogy.odb.test;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.dialogy.odb.core.meta.ODBType;

/**
 * @author olivier s
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TestTypeConverter extends ODBTestCase {

	public void test1() {
		assertEquals(ODBType.NATIVE_INT, ODBType.getFromClass(Integer.TYPE));
		assertEquals(ODBType.NATIVE_BOOLEAN, ODBType.getFromClass(Boolean.TYPE));
		assertEquals(ODBType.NATIVE_BYTE, ODBType.getFromClass(Byte.TYPE));
		assertEquals(ODBType.NATIVE_CHAR, ODBType.getFromClass(Character.TYPE));
		assertEquals(ODBType.NATIVE_DOUBLE, ODBType.getFromClass(Double.TYPE));
		assertEquals(ODBType.NATIVE_FLOAT, ODBType.getFromClass(Float.TYPE));
		assertEquals(ODBType.NATIVE_LONG, ODBType.getFromClass(Long.TYPE));
		assertEquals(ODBType.NATIVE_SHORT, ODBType.getFromClass(Short.TYPE));
	}

	public void test2() {
		assertEquals(ODBType.INTEGER, ODBType.getFromClass(Integer.class));
		assertEquals(ODBType.BOOLEAN, ODBType.getFromClass(Boolean.class));
		assertEquals(ODBType.BYTE, ODBType.getFromClass(Byte.class));
		assertEquals(ODBType.CHARACTER, ODBType.getFromClass(Character.class));
		assertEquals(ODBType.DOUBLE, ODBType.getFromClass(Double.class));
		assertEquals(ODBType.FLOAT, ODBType.getFromClass(Float.class));
		assertEquals(ODBType.LONG, ODBType.getFromClass(Long.class));
		assertEquals(ODBType.SHORT, ODBType.getFromClass(Short.class));
		assertEquals(ODBType.STRING, ODBType.getFromClass(String.class));
		assertEquals(ODBType.BIG_DECIMAL, ODBType.getFromClass(BigDecimal.class));
		assertEquals(ODBType.BIG_INTEGER, ODBType.getFromClass(BigInteger.class));
	}

	public void test3() {

		int[] array1 = { 1, 2 };
		assertEquals(ODBType.ARRAY, ODBType.getFromClass(array1.getClass()));
		assertEquals(ODBType.NATIVE_INT, ODBType.getFromClass(array1.getClass()).getSubType());

		String[] array2 = { "1", "2" };
		assertEquals(ODBType.ARRAY, ODBType.getFromClass(array2.getClass()));
		assertEquals(ODBType.STRING, ODBType.getFromClass(array2.getClass()).getSubType());
	}
	
	public void test4() throws ClassNotFoundException{
		System.out.println(int.class.getName());
		assertEquals(Integer.TYPE,int.class);
	}
}
