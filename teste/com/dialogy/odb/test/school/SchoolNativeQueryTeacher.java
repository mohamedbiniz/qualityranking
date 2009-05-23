/* 
 * $RCSfile: SchoolNativeQueryTeacher.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:53 $
 * 
 * 
 */
package com.dialogy.odb.test.school;

import java.util.Calendar;

import com.dialogy.odb.core.query.nq.INativeQuery;
import com.dialogy.odb.test.vo.school.History;

public class SchoolNativeQueryTeacher implements INativeQuery {
    private String name;
        
    public SchoolNativeQueryTeacher(String name) {
        this.name = name;
    }
    
    public boolean match(Object object) {
        History s = (History)object;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, 6);
        c.set(Calendar.YEAR, 2005);
        
        return s.getTeacher().getName().equals(name) && s.getDate().getTime() > (c.getTime().getTime());
    }

    public Class getObjectType() {        
        return History.class;
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
