/* 
 * $RCSfile: TestQuerySchool.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:53 $
 * 
 * 
 */
package com.dialogy.odb.test.school;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.dialogy.odb.core.Configuration;
import com.dialogy.odb.main.ODB;
import com.dialogy.odb.test.ODBTestCase;
import com.dialogy.odb.test.vo.school.Course;
import com.dialogy.odb.test.vo.school.Discipline;
import com.dialogy.odb.test.vo.school.History;
import com.dialogy.odb.test.vo.school.Student;
import com.dialogy.odb.test.vo.school.Teacher;
import com.dialogy.odb.tool.IOUtil;

public class TestQuerySchool extends ODBTestCase{

    //  possíveis consultas
    // Listar todos os alunos de determinado professor
    // Listar alunos com nota abaixo de x
    // Listar disciplinas que um professor ministrou no semestre
    
    public void test1() throws Exception{
        Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("t-school.odb");
   
        //List students by name
        SchoolNativeQueryStudent natQuery = new SchoolNativeQueryStudent("Brenna", 23);
        List students = odb.getObjects(natQuery);
        System.out.println("Students with name equals Brenna: " + students);
        
        SchoolSimpleNativeQueryStudent sNatQuery = new SchoolSimpleNativeQueryStudent("Brenna");
        students = odb.getObjects(sNatQuery);
        System.out.println("Students with name equals Brenna: " + students);
        
        
        //list disciplines of one teacher by semester
        
        SchoolNativeQueryTeacher natQuery2 = new SchoolNativeQueryTeacher("Jeremias");
        List historys = odb.getObjects(natQuery2);
        HashMap listDiscipline = new HashMap();
        for (Iterator iter = historys.iterator(); iter.hasNext();) {
            History h = (History) iter.next();
            listDiscipline.put(h.getDiscipline().getName(), h.getDiscipline());
        }
        System.out.println("Disciplines of teacher Jeremias: " + listDiscipline);
             
        odb.close();
        
    }
    
    protected void setUp() throws Exception {
    	Configuration.setDebugEnabled(false);
        ODB odb = ODB.open("t-school.odb");
               
        List students = odb.getObjectsOf(Student.class,true);
        int numStudents = students.size();
        
        System.out.println("Students before insert)-----------" + students);
        
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

        odb.commit();
        odb.close();
        
        odb = ODB.open("t-school.odb");
        students = odb.getObjectsOf(Student.class,true);
        odb.close();
        System.out.println("Students after insert)-----------" + students);
        assertEquals(numStudents+1, students.size());    
    }

	protected void tearDown() throws Exception {
		IOUtil.deleteFile("t-school.odb");
	}
    
    
}
