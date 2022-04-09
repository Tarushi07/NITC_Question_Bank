package com.example.new_firebase;

import java.util.ArrayList;

public class Subject {

    private String subjectName;
    private String subjectCode;
    private String facultyEmail;

    public String getFacultyEmail() {
        return facultyEmail;
    }

    public void setFacultyEmail(String facultyEmail) {
        this.facultyEmail = facultyEmail;
    }


    public Subject()
    {

    }
    public Subject(String subjectName,String subjectCode,String facultyEmail)
    {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.facultyEmail = facultyEmail;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String sub_name) {
        this.subjectName = sub_name;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String sub_code) {
        this.subjectCode = sub_code;
    }

}
