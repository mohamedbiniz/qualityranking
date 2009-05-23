/* 
 * $RCSfile: Teacher.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:55 $
 * 
 * 
 */
package com.dialogy.odb.test.vo.school;

public class Teacher {
    private String name;
    private String speciality;
        
    public Teacher() {
    }
    
    public Teacher(String name, String speciality) {
        super();
        this.name = name;
        this.speciality = speciality;
    }
    
    public String getName() {
        return name;
    }
    public String getSpeciality() {
        return speciality;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
    
    public String toString() {
        return "name="+name + " | speciality=" + speciality;
    }
}
