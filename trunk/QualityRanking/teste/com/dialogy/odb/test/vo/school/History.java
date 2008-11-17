/* 
 * $RCSfile: History.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: cvs $
 * $Date: 2006/01/11 12:33:55 $
 * 
 * 
 */
package com.dialogy.odb.test.vo.school;

import java.util.Date;


public class History {
    private Discipline discipline;
    private Teacher teacher;
    private int score;
    private Date date;
    private Student student;
    
    public History() {
    }
    
    public History(Date data, Discipline discipline, int score, Teacher teacher) {
        this.date = data;
        this.discipline = discipline;
        this.score = score;
        this.teacher = teacher;
    }

    public Date getDate() {
        return date;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public int getScore() {
        return score;
    }

    public void setDate(Date data) {
        this.date = data;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher; 
    }        
    
    public String toString() {
        return "disc.="+discipline.getName() + " | teacher=" + teacher.getName() + " | student=" + student.getName() + " | date=" + date + " | score=" + score;
    }

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}
    
    
}
