package com.dialogy.odb.test.cache;

import java.io.IOException;

import junit.framework.TestCase;

import com.dialogy.odb.core.meta.ClassInfo;
import com.dialogy.odb.core.meta.NonNativeObjectInfo;
import com.dialogy.odb.core.session.Cache;

public class TestInsertingObject extends TestCase {

	public void test1() throws IOException {

		Cache cache = new Cache();
		String s1 = "ola1";
		String s2 = "ola2";
		String s3 = "ola3";
		cache.startInsertingObject(s1);
		cache.startInsertingObject(s2);
		cache.startInsertingObject(s3);

		assertTrue(cache.isInserting(s1));
		assertTrue(cache.isInserting(s2));
		assertTrue(cache.isInserting(s3));

		cache.endInsertingObject(s3);
		cache.endInsertingObject(s2);
		cache.endInsertingObject(s1);

		assertFalse(cache.isInserting(s1));
		assertFalse(cache.isInserting(s2));
		assertFalse(cache.isInserting(s3));
	}

	public void test2() throws IOException {

		Cache cache = new Cache();
		String s1 = "ola1";
		String s2 = "ola2";
		String s3 = "ola3";
		
		for(int i=0;i<1000;i++){
			cache.startInsertingObject(s1);
			cache.startInsertingObject(s2);
			cache.startInsertingObject(s3);
		}
		assertEquals(1000,cache.insertingLevelOf(s1));
		assertEquals(1000,cache.insertingLevelOf(s2));
		assertEquals(1000,cache.insertingLevelOf(s3));
		
		for(int i=0;i<1000;i++){
			cache.endInsertingObject(s1);
			cache.endInsertingObject(s2);
			cache.endInsertingObject(s3);
		}
		assertEquals(0,cache.insertingLevelOf(s1));
		assertEquals(0,cache.insertingLevelOf(s2));
		assertEquals(0,cache.insertingLevelOf(s3));
		
		cache.startInsertingObject(s1);
		cache.startInsertingObject(s1);
		cache.startInsertingObject(s1);
		cache.startInsertingObject(s2);
		cache.startInsertingObject(s3);

		assertTrue(cache.isInserting(s1));
		assertTrue(cache.isInserting(s2));
		assertTrue(cache.isInserting(s3));

		cache.endInsertingObject(s3);
		cache.endInsertingObject(s2);
		cache.endInsertingObject(s1);

		assertTrue(cache.isInserting(s1));
		assertFalse(cache.isInserting(s2));
		assertFalse(cache.isInserting(s3));
	}
	
	public void test3() throws IOException {

		Cache cache = new Cache();
		ClassInfo ci = new ClassInfo();
		ci.setPosition(1);
		NonNativeObjectInfo nnoi1 = new NonNativeObjectInfo(new String("s1"),1, -1,-1,ci,null);
		NonNativeObjectInfo nnoi2 = new NonNativeObjectInfo(new String("s2"),10, -1,-1,ci,null);
		NonNativeObjectInfo nnoi3 = new NonNativeObjectInfo(new String("s3"),100, -1,-1,ci,null);
		
		cache.startReadingObjectInfo(1,nnoi1);
		cache.startReadingObjectInfo(10,nnoi2);
		cache.startReadingObjectInfo(100,nnoi3);

		assertTrue(cache.isReadingObjectInfo(1));
		assertTrue(cache.isReadingObjectInfo(10));
		assertTrue(cache.isReadingObjectInfo(100));

		cache.endReadingObjectInfo(nnoi1);
		cache.endReadingObjectInfo(nnoi2);
		cache.endReadingObjectInfo(nnoi3);

		assertFalse(cache.isReadingObjectInfo(1));
		assertFalse(cache.isReadingObjectInfo(10));
		assertFalse(cache.isReadingObjectInfo(100));
	}

}
