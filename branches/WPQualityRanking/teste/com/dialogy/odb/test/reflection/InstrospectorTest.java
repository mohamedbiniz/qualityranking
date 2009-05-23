package com.dialogy.odb.test.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.dialogy.odb.core.meta.ClassInfo;
import com.dialogy.odb.core.meta.MetaModel;
import com.dialogy.odb.core.meta.NonNativeObjectInfo;
import com.dialogy.odb.core.meta.ObjectInfoComparator;
import com.dialogy.odb.core.reflection.ClassIntrospector;
import com.dialogy.odb.core.reflection.ObjectIntrospector;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.inheritance.FootballPlayer;
import com.dialogy.odb.test.vo.inheritance.OutdoorPlayer;
import com.dialogy.odb.test.vo.inheritance.Player;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;

public class InstrospectorTest extends ODBTestCase{

	public void testClassInfo(){
		User user = new User("olivier smadja","olivier@dialogy.com",new Profile("operator",new Function("login")));
		
		ClassInfo classInfo = ClassIntrospector.getClassInfo(user);
		
		System.out.println(classInfo);
		assertEquals(classInfo.getFullClassname(),user.getClass().getName());
		assertEquals(classInfo.getAttributes().size(),3);
		
	}
	public void testInstanceInfo() throws Exception{
		User user = new User("olivier smadja","olivier@dialogy.com",new Profile("operator",new Function("login")));
		
		ClassInfo ci = ClassIntrospector.getClassInfo(user);
		NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
		
		System.out.println(instanceInfo);
		assertEquals(instanceInfo.getClassInfo().getFullClassname(),user.getClass().getName());
		assertEquals(instanceInfo.getAttributeValues().get(0).toString(),"olivier smadja");
		
	}
	
	public void testInstanceInfo2() throws Exception{
		User user = new User("olivier smadja","olivier@dialogy.com",new Profile("operator",new Function("login")));
		
		ClassInfo ci = ClassIntrospector.getClassInfo(user);
		NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,false,null);
		
		System.out.println(instanceInfo);
		assertEquals(instanceInfo.getClassInfo().getFullClassname(),user.getClass().getName());
		assertEquals(instanceInfo.getAttributeValues().get(0).toString(),"olivier smadja");
		
	}
	
	public void testSuperClass(){
		Class collection = Collection.class;
		Class arrayList = ArrayList.class;
		Class string = String.class;
		
		boolean b1 = collection.isAssignableFrom(arrayList);
		boolean b2 = arrayList.isAssignableFrom(collection);
		boolean b3 = collection.isAssignableFrom(string);
		System.out.println(b1 + " - " + b2+ " - " + b3);
	}
    
    public void testCompareObjects1() throws Exception{
        User user = new User("olivier smadja","olivier@dialogy.com",new Profile("operator",new Function("login")));
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        user.setName("Olivier Smadja");
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        assertEquals(user,comparator.getChangedObjects().get(0));
        assertEquals(1,comparator.getChangedObjects().size());
    }
    public void testCompareObjects2() throws Exception{
        User user = new User("olivier smadja","olivier@dialogy.com",new Profile("operator",new Function("login")));
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        user.setName(null);
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        assertEquals(user,comparator.getChangedObjects().get(0));
        assertEquals(1,comparator.getChangedObjects().size());

    }
    public void testCompareObjects3CollectionContentChange() throws Exception{
        Function function = new Function("login");
        User user = new User("olivier smadja","olivier@dialogy.com",new Profile("operator",function));
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        function.setName("login function");
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        
        Object object = comparator.getChangedObjects().get(0);
        System.out.println("test3:changed objects are " + comparator.getChangedObjects());
        assertEquals(object,function);
        assertEquals(1,comparator.getChangedObjects().size());
        

    }
    public void testCompareObjects4CollectionContentChange() throws Exception{
        Function function = new Function("login");
        User user = new User("olivier smadja","olivier@dialogy.com",new Profile("operator",function));
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        function.setName(null);
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        
        Object object = comparator.getChangedObjects().get(0);
        System.out.println("test4:changed objects are " + comparator.getChangedObjects());
        assertEquals(object,function);
        assertEquals(1,comparator.getChangedObjects().size());

    }
    public void testCompareObjects5() throws Exception{
        Function function = new Function("login");
        Profile profile = new Profile("operator",function);
        User user = new User("olivier smadja","olivier@dialogy.com",profile);
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        profile.getFunctions().add(new Function("logout"));
        
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        
        Object object = comparator.getChangedObjects().get(0);
        System.out.println("test5:changed objects are " + comparator.getChangedObjects());
        assertEquals(profile,object);
        assertEquals(1,comparator.getChangedObjects().size());

    }
    public void testCompareObjects6() throws Exception{
        Function function = new Function("login");
        Profile profile = new Profile("operator",function);
        User user = new User("olivier smadja","olivier@dialogy.com",profile);
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        profile.setName("ope");
        
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        
        Object object = comparator.getChangedObjects().get(0);
        System.out.println("test6:changed objects are " + comparator.getChangedObjects());
        assertEquals(profile,object);
        assertEquals(1,comparator.getChangedObjects().size());

    }
    public void testCompareObjects7() throws Exception{
        Function function = new Function("login");
        Profile profile = new Profile("operator",function);
        User user = new User("olivier smadja","olivier@dialogy.com",profile);
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        /// Set the same name
        profile.setName("operator");
        
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertFalse(comparator.hasChanged(instanceInfo,instanceInfo3));
        
        System.out.println("test7:changed objects are " + comparator.getChangedObjects());
        assertEquals(0,comparator.getChangedObjects().size());

    }
    public void testCompareObjects8() throws Exception{
        Function function = new Function("login");
        Profile profile = new Profile("operator",function);
        User user = new User("olivier smadja","olivier@dialogy.com",profile);
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        user.setProfile(null);
        
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        
        Object object = comparator.getChangedObjects().get(0);
        System.out.println("test8:changed objects are " + comparator.getChangedObjects());
        assertEquals(user,object);
        assertEquals(1,comparator.getChangedObjects().size());

    }
    public void testCompareObjects9() throws Exception{
        Function function = new Function("login");
        Profile profile = new Profile("operator",function);
        User user = new User("olivier smadja","olivier@dialogy.com",profile);
        ObjectInfoComparator comparator = new ObjectInfoComparator();
        
        ClassInfo ci = ClassIntrospector.getClassInfo(user);
        NonNativeObjectInfo instanceInfo = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        user.setName("Kiko");
        
        
        System.out.println("1:"+instanceInfo);
        NonNativeObjectInfo instanceInfo3 = (NonNativeObjectInfo) new ObjectIntrospector(new MetaModel()).getObjectInfo(user,ci,true,null);
        
        System.out.println("2:"+instanceInfo3);
        
        assertTrue(comparator.hasChanged(instanceInfo,instanceInfo3));
        
        Object object = comparator.getChangedObjects().get(0);
        System.out.println("test9:changed objects are " + comparator.getChangedObjects());
        assertEquals(user,object);
        assertEquals(1,comparator.getChangedObjects().size());

    }
    
    public void testGetSuperClasses(){
        List superclasses = ClassIntrospector.getSuperClasses(FootballPlayer.class,true);
        assertEquals(3,superclasses.size());
        assertEquals(FootballPlayer.class,superclasses.get(0));
        assertEquals(OutdoorPlayer.class,superclasses.get(1));
        assertEquals(Player.class,superclasses.get(2));
    }
    
    public void testGetAllFields(){
        List allFields = ClassIntrospector.getAllFields(FootballPlayer.class);
        assertEquals(3,allFields.size());
        assertEquals("role" , ((Field)allFields.get(0)).getName());
        assertEquals("groundName" , ((Field)allFields.get(1)).getName());
        assertEquals("name" , ((Field)allFields.get(2)).getName());
    }
}
