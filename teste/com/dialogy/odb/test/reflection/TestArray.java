package com.dialogy.odb.test.reflection;

import java.lang.reflect.Array;

import com.dialogy.odb.test.ODBTestCase;

public class TestArray extends ODBTestCase {
	public void test1(){
		String [] array = {"Ola","chico"};
		
		System.out.println(array.getClass().isArray());
		System.out.println(array.getClass().getComponentType());
		
		assertEquals(true,array.getClass().isArray());
		assertEquals("[Ljava.lang.String;",array.getClass().getName());
		assertEquals("java.lang.String",array.getClass().getComponentType().getName());
	}
	public void test2(){
		int [] array = {1,2};
		
		System.out.println(array.getClass().isArray());
		System.out.println(array.getClass().getComponentType());
		
		assertEquals(true,array.getClass().isArray());
		assertEquals("[I",array.getClass().getName());
		assertEquals("int",array.getClass().getComponentType().getName());
	}
	public void test3(){
		double [] array = {1,2};
		
		System.out.println(array.getClass().isArray());
		System.out.println(array.getClass().getComponentType());
		
		assertEquals(true,array.getClass().isArray());
		assertEquals("[D",array.getClass().getName());
		assertEquals("double",array.getClass().getComponentType().getName());
	}
	
	public void test4() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		System.out.println("type int : " + Integer.TYPE.getName());
		Object o = Array.newInstance(Integer.TYPE,5);
		Array.setInt(o,0,1);
		Array.set(o,1,new Integer(2));
		
		assertEquals(true,o.getClass().isArray());
		assertEquals("int",o.getClass().getComponentType().getName());
		assertEquals(1,Array.getInt(o,0));
		assertEquals(2,Array.getInt(o,1));
	}


}
