/* 
 * $RCSfile: SchoolSimpleNativeQueryStudent.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:53 $
 * 
 * 
 */
package com.dialogy.odb.test.school;

import com.dialogy.odb.core.query.nq.ISimpleNativeQuery;
import com.dialogy.odb.test.vo.school.Student;

public class SchoolSimpleNativeQueryStudent implements ISimpleNativeQuery {
    private String name;
    
    public SchoolSimpleNativeQueryStudent(String name) {
        this.name = name;
    }
    
    public boolean match(Student object) {
        return object.getName().equals(name);
    }

}
