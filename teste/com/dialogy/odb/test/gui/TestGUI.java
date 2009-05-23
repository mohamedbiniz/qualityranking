/* 
 * $RCSfile: TestGUI.java,v $
 * Tag : $Name:  $
 * $Revision: 1.2 $
 * $Author: cvs $
 * $Date: 2006/01/13 12:48:36 $
 * 
 * Copyright 2003 Cetip
 */
package com.dialogy.odb.test.gui;

import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import com.dialogy.odb.core.io.IOFileParameter;
import com.dialogy.odb.core.io.IOParameters;
import com.dialogy.odb.core.io.StorageEngine;
import com.dialogy.odb.gui.objectbrowser.flat.FlatQueryResultPanel;
import com.dialogy.odb.main.ODB;
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

public class TestGUI {


    static void setUp() throws Exception {
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

    public static void main(String[] args) throws Exception {
        setUp();
        JFrame frame = new JFrame("Teste Flat Query Panel");

        IOParameters parameters = new IOFileParameter("t1.odb",true);
        //IOParameters parameters = new IOFileParameter("knowledger.odb",true);
        StorageEngine storageEngine = new StorageEngine(parameters);
        List l = storageEngine.getObjectInfos(User.class.getName(),null,true,-1,-1,false);
        //List l = storageEngine.getObjectInfos(KnowledgeBaseDescription.class,null,true,-1,-1,false);

        FlatQueryResultPanel flatQueryResultPanel = new FlatQueryResultPanel(storageEngine,User.class.getName(),l);
        //FlatQueryResultPanel flatQueryResultPanel = new FlatQueryResultPanel(storageEngine,KnowledgeBaseDescription.class,l);
        frame.getContentPane().add(flatQueryResultPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        
    }


}
