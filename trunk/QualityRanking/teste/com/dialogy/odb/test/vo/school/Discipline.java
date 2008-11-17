/* 
 * $RCSfile: Discipline.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:55 $
 * 
 * 
 */
package com.dialogy.odb.test.vo.school;

public class Discipline {
    private String name;
    private int numberOfHour;
   
    public Discipline() {
    }
    
    public Discipline(String name, int hour) {        
        this.name = name;
        numberOfHour = hour;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfHour() {
        return numberOfHour;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfHour(int numberOfHour) {
        this.numberOfHour = numberOfHour;
    }
        
    public String toString() {
        return "name="+name + " | nbhours=" + numberOfHour;
    }
}
