/* 
 * $RCSfile: Student.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:55 $
 * 
 * 
 */
package com.dialogy.odb.test.vo.school;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Student {
    private String id;
    private String name;
    private int age;
    private Course course;
    private Date firstDate;
    private List<History> listHistory;

    public Student() {
    }
    
    public Student(int age, Course course, Date date, String id, String name) {
        this.age = age;
        this.course = course;
        firstDate = date;
        this.id = id;
        this.name = name;
        listHistory = new ArrayList<History>();
    }

    public int getAge() {
        return age;
    }

    public Course getCourse() {
        return course;
    }

    public Date getFirstDate() {
        return firstDate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setFirstDate(Date firstDate) {
        this.firstDate = firstDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<History> getListHistory() {
        return listHistory;
    }

    public void setListHistory(List<History> listHistory) {
        this.listHistory = listHistory;
    }
    
    public void addHistory(History history){
        history.setStudent(this);
        listHistory.add(history);
    }
    
    public String toString() {
        return "id="+id + " | name=" + name + " | age= " + age + " | date=" + firstDate + " | course=" + course.getName() + " | history=" + listHistory.toString();
    }
}
