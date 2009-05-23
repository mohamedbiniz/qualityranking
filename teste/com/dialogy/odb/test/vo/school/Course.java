/* 
 * $RCSfile: Course.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:55 $
 * 
 * 
 */
package com.dialogy.odb.test.vo.school;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String name;
    private List listOfDiscipline;

    public Course() {
    }
    
    public Course(String name) {
        this.name = name;
        this.listOfDiscipline = new ArrayList();
    }
           
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getListOfDiscipline() {
        return listOfDiscipline;
    }

    public void setListOfDiscipline(List listOfDiscipline) {
        this.listOfDiscipline = listOfDiscipline;
    }
        
    public String toString() {
        
        return "name="+name + " | disciplines=" + listOfDiscipline.toString();
    }
}
