/* 
 * $RCSfile: SchoolNativeQueryStudent.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:53 $
 * 
 * 
 */
package com.dialogy.odb.test.school;

import com.dialogy.odb.core.query.nq.INativeQuery;
import com.dialogy.odb.test.vo.school.Student;

public class SchoolNativeQueryStudent implements INativeQuery {
    private String name;
    private int age;
    
    public SchoolNativeQueryStudent(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public boolean match(Object object) {
        Student s = (Student)object;
        return s.getName().equals(name);
    }

    public Class getObjectType() {        
        return Student.class;
    }

    public Class[] getObjectsType() {
        // TODO Auto-generated method stub
        return null;
    }

    public String[] getIndexFields() {
        // TODO Auto-generated method stub
        return null;
    }

}
