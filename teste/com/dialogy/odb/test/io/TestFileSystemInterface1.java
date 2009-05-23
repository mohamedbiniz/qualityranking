package com.dialogy.odb.test.io;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.dialogy.odb.core.io.FileSystemInterface;
import com.dialogy.odb.test.ODBTestCase;

public class TestFileSystemInterface1 extends ODBTestCase {
	
	
	public void testByte() throws IOException{
		byte b = 127;
		FileSystemInterface fsi = new FileSystemInterface("testByte.teste",true);
		fsi.writeByte(b,"byte",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testByte.teste",false);
		byte b2 = fsi.readByte("byte");
		System.out.println(b2);
		assertEquals(b,b2);
        fsi.close();
        assertEquals(true,fsi.delete());
		
	}
    public void testInt() throws IOException{
        int i = 259998;
        FileSystemInterface fsi = new FileSystemInterface("testInt.teste",true);
        fsi.writeInt(i,"int",false);
        fsi.close();
        
        fsi = new FileSystemInterface("testInt.teste",false);
        int i2 = fsi.readInt("int");
        System.out.println(i2);
        assertEquals(i,i2);
        fsi.close();
        assertEquals(true,fsi.delete());
        
    }

    public void testChar() throws IOException{
		char c = '„';
		FileSystemInterface fsi = new FileSystemInterface("testChar.teste",true);
		fsi.writeChar(c,"char",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testChar.teste",false);
		char c2 = fsi.readChar("char");
		System.out.println(c2);
		assertEquals(c,c2);
        fsi.close();
        assertEquals(true,fsi.delete());
		
	}

	public void testShort() throws IOException{
		short s = 4598;
		FileSystemInterface fsi = new FileSystemInterface("testShort.teste",true);
		fsi.writeShort(s,"short",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testShort.teste",false);
		short s2 = fsi.readShort("short");
		System.out.println(s2);
		assertEquals(s,s2);
        fsi.close();
        assertEquals(true,fsi.delete());
	}

	public void testBoolean() throws IOException{
		boolean b1 = true;
		boolean b2 = false;
		FileSystemInterface fsi = new FileSystemInterface("testBoolean.teste",true);
		fsi.writeBoolean(b1,"bool1",false);
		fsi.writeBoolean(b2,"bool2",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testBoolean.teste",false);
		boolean b11 = fsi.readBoolean("b1");
		boolean b22 = fsi.readBoolean("b2");
		System.out.println(b11 + " - " + b22);
		assertEquals(b1,b11);
		assertEquals(b2,b22);
        fsi.close();
        assertEquals(true,fsi.delete());
	}

	public void testLong() throws IOException{
		long i = 259999865;
		FileSystemInterface fsi = new FileSystemInterface("testLong.teste",true);
		fsi.writeLong(i,"long",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testLong.teste",false);
		long i2 = fsi.readLong("long");
		System.out.println(i2);
		assertEquals(i,i2);
        fsi.close();
        assertEquals(true,fsi.delete());
	}
	public void testString() throws IOException{
		String s = "ola chico, como vocÍ est· ??? eu estou bem atÈ amanh„ de manh„, È»„Û’ıÁ«";
		FileSystemInterface fsi = new FileSystemInterface("testString.teste",true);
		fsi.writeString(s,"string",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testString.teste",false);
		String s2 = fsi.readString("string");
		System.out.println(s2);
		assertEquals(s,s2);
        fsi.close();
        assertEquals(true,fsi.delete());
	}

	public void testBigDecimal() throws IOException{
		BigDecimal bd = new BigDecimal("-128451.1234567899876543210");
		FileSystemInterface fsi = new FileSystemInterface("testBigDecimal.teste",true);
		fsi.writeBigDecimal(bd,"bigd",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testBigDecimal.teste",false);
		BigDecimal bd2 = fsi.readBigDecimal("bigd");
		System.out.println(bd2);
		assertEquals(bd,bd2);
        fsi.close();
        assertEquals(true,fsi.delete());
	}
	public void testBigInteger() throws IOException{
		BigInteger bd = new BigInteger("-128451");
		FileSystemInterface fsi = new FileSystemInterface("testBigDecimal.teste",true);
		fsi.writeBigInteger(bd,"bigi",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testBigDecimal.teste",false);
		BigInteger bd2 = fsi.readBigInteger("bigi");
		System.out.println(bd2);
		assertEquals(bd,bd2);
        fsi.close();
        assertEquals(true,fsi.delete());
	}
	public void testFloat() throws IOException{
		float f = (float)12544548.12454;
		FileSystemInterface fsi = new FileSystemInterface("testFloat.teste",true);
		fsi.writeFloat(f,"float",false);
		fsi.close();
		
		fsi = new FileSystemInterface("testFloat.teste",false);
		float f2 = fsi.readFloat("float");
		System.out.println(f2);
		assertTrue(f==f2);
        fsi.close();
        assertEquals(true,fsi.delete());
	}

}
