/* 
 * $RCSfile: TestGetObjectInfo.java,v $
 * Tag : $Name:  $
 * $Revision: 1.2 $
 * $Author: cvs $
 * $Date: 2006/01/13 12:48:27 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.gui;

import java.util.Date;
import java.util.List;

import com.dialogy.odb.core.io.IOFileParameter;
import com.dialogy.odb.core.io.IOParameters;
import com.dialogy.odb.core.io.StorageEngine;
import com.dialogy.odb.core.meta.ClassInfo;
import com.dialogy.odb.core.meta.ObjectInfoUtil;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.country.City;
import com.dialogy.odb.test.vo.country.Country;
import com.dialogy.odb.test.vo.login.Function;
import com.dialogy.odb.test.vo.login.Profile;
import com.dialogy.odb.test.vo.login.User;
import com.dialogy.odb.test.vo.school.Course;
import com.dialogy.odb.test.vo.school.Discipline;
import com.dialogy.odb.test.vo.school.History;
import com.dialogy.odb.test.vo.school.Student;
import com.dialogy.odb.test.vo.school.Teacher;
import com.dialogy.odb.tool.IOUtil;

public class TestGetObjectInfo extends ODBTestCase {

    protected void set1Up() throws Exception {
        ODB odb = ODB.open("t1.odb");
        Function f1 = new Function("login");
        Function f2 = new Function("logout");
        Profile profile = new Profile("profile 1",f1);
        profile.addFunction(f2);
        User user = new User("André","andre@dialogy.com",profile);

        Profile profile2 = new Profile("profile 2",f1);
        profile2.addFunction(f2);
        User user2 = new User("Olivier","olivier@dialogy.com",profile2);

        odb.store(user);
        odb.store(user2);
        
        Course computerScience = new Course("Computer Science");
        Teacher teacher = new Teacher("Jeremias", "Java");
        Discipline dw1 = new Discipline("Des. Web 1", 3);
        Discipline is = new Discipline("Intranet/Segurança", 4);
        
        Student std1 = new Student(20, computerScience, new Date(), "1cs", "Brenna");
        
        History h1 = new History(new Date(), dw1, 0, teacher);
        History h2 = new History(new Date(), is, 0, teacher);
        
        std1.addHistory(h1);
        std1.addHistory(h2);
        
        odb.store(std1);
        
        Country france = new Country("France");
        City paris = new City("paris");
        paris.setCountry(france);
        france.setCapital(paris);
        odb.store(france);
        
        odb.commitAndClose();
    }

    public void test1() throws Exception{
        
        //IOParameters parameters = new IOFileParameter("t1.odb",true);
        IOParameters parameters = new IOFileParameter("knowledger.odb",true);
        StorageEngine storageEngine = new StorageEngine(parameters);
        List objectInfos = storageEngine.getObjectInfos(User.class.getName(),null,true,-1,-1,false);
//        List objectInfos = storageEngine.getObjectInfos(KnowledgeBaseDescription.class,null,true,-1,-1,false);

        ClassInfo ci = storageEngine.getMetaModel().getClassInfo(User.class.getName()); 
        List attributeList = ObjectInfoUtil.buildAttributeNameList(ci);
        List valueList = ObjectInfoUtil.buildValueList(ci,objectInfos);

        System.out.println(attributeList);
        System.out.println(valueList);
        
        List line1 = (List) valueList.get(0);
        assertEquals(attributeList.size(),line1.size());
        storageEngine.close();
    }
    
    protected void tearDown() throws Exception {
        IOUtil.deleteFile("t1.odb");
        super.tearDown();
    }

}
